package com.lixue.admin.okhttputils;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.lixue.admin.okhttputils.callback.ResultCallBack;
import com.lixue.admin.okhttputils.util.L;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * HttpClient管理器
 * Created by admin on 2015/12/9.
 */
public class OkHttpClientManager {
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private Gson mGson;

    private static OkHttpClientManager instance;

    private OkHttpClientManager(){
        mOkHttpClient = new OkHttpClient();
        //设置cookie可用
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));

        mHandler = new Handler(Looper.getMainLooper());
        if (Build.VERSION.SDK_INT >= 23){
            GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL,
                    Modifier.TRANSIENT,Modifier.STATIC);
            mGson = gsonBuilder.create();
        }else{
            mGson = new Gson();
        }

    }

    public static OkHttpClientManager getInstance(){
        if (instance == null){
            synchronized (OkHttpClientManager.class){
                instance = new OkHttpClientManager();
            }
        }
        return instance;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public Handler getHandler() {
        return mHandler;
    }

    /**异步执行，访问网络**/
    public void execute(final Request request,ResultCallBack callBack){
        if (callBack == null) callBack = ResultCallBack.DEFAULT_RESULT_CALLBACK;
        final ResultCallBack finalCallBack = callBack;
        finalCallBack.onBefor();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
              sendFailResultCallback(request,e, finalCallBack);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() >= 400 && response.code() <= 599){
                    sendFailResultCallback(request, new RuntimeException(response.body().toString()), finalCallBack);
                    return;
                }
               try {
                   String str = response.body().string();
                   String s = response.message();
                   L.e("收到的响应串："+str);
                   if (finalCallBack.mType == String.class){
                       sendSuccessResultCallback(str,finalCallBack);
                   }else{
                       Object o = mGson.fromJson(str,finalCallBack.mType);
                       sendSuccessResultCallback(o,finalCallBack);
                   }
               } catch (JsonParseException e){
                   sendFailResultCallback(request,e,finalCallBack);
               }
            }
        });

    }
    /**同步执行，访问网络**/
    public <T> T execute(Request request,Class<T> clazz) throws IOException {
        Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();
        String str = response.body().string();
        return mGson.fromJson(str,clazz);

    }

    public void sendFailResultCallback(final Request request, final Exception e, final ResultCallBack callback) {
        if (callback == null) return;

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, e);
                callback.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final ResultCallBack callback) {
        if (callback == null) return;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    /**取消请求**/
    public void cancleTag(Object tag){
        mOkHttpClient.cancel(tag);
    }
    /**设置证书**/
    public void setCertificates(InputStream... certificates){
        setCertificates(certificates, null, null);
    }

    public void setCertificates(InputStream[] certificates,InputStream bksFile,String password){
        try {
            TrustManager[] trustManagers = prepareTrustManager(certificates);//准备证书
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);//准备秘钥
            SSLContext sslContext = SSLContext.getInstance("TLS");

            sslContext.init(keyManagers,new TrustManager[]{new MyTrustManager(chooseTrustManager(trustManagers))},new SecureRandom());
            mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

    }

    private TrustManager[] prepareTrustManager(InputStream... cetificates){
        if (cetificates == null || cetificates.length == 0) return null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream i: cetificates){
                String cetificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(cetificateAlias,certificateFactory.generateCertificate(i));
                if (i != null){
                    i.close();
                }
            }
            TrustManagerFactory trustManagerFactory = null;
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            return trustManagers;
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private KeyManager[] prepareKeyManager(InputStream bksFile, String pwd){
        if (bksFile == null || pwd == null) return null;
        try {
            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(bksFile, pwd.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore,pwd.toCharArray());
            return keyManagerFactory.getKeyManagers();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    private X509TrustManager chooseTrustManager(TrustManager[] trustManagers){
        for (TrustManager tm: trustManagers){
            if (tm instanceof X509TrustManager){
                return (X509TrustManager) tm;
            }
        }
        return null;
    }

    private class MyTrustManager implements X509TrustManager{
        private X509TrustManager defaultTrustManager;
        private X509TrustManager localTrustManager;

        public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var4.init((KeyStore) null);
            defaultTrustManager = chooseTrustManager(var4.getTrustManagers());
            this.localTrustManager = localTrustManager;
        }


        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException ce) {
                localTrustManager.checkServerTrusted(chain, authType);
            }
        }


        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
