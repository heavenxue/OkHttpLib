package com.lixue.admin.okhttputils;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lixue.admin.okhttputils.builder.GetBuilder;
import com.lixue.admin.okhttputils.builder.PostFileBuilder;
import com.lixue.admin.okhttputils.builder.PostFormBuilder;
import com.lixue.admin.okhttputils.builder.PostJsonBuilder;
import com.lixue.admin.okhttputils.builder.PostStringBuilder;
import com.lixue.admin.okhttputils.callback.CallBack;
import com.lixue.admin.okhttputils.https.HttpsUtils;
import com.lixue.admin.okhttputils.request.RequestCall;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * Created by Administrator on 2015/12/7.
 */
public class OkHttpClientManager {
    public static final String TAG = "OKHttpClientManager";
    public static final long DEFAULT_MILLISECONDS = 10000;
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private Gson mGson;
    private boolean ifExposeAnnotation;//是否包含@Expose注解的字段

    private OkHttpClientManager(){
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mHandler = new Handler(Looper.getMainLooper());
        if (ifExposeAnnotation){
         mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        }else{
            final int sdk_version = Build.VERSION.SDK_INT;
            //android版本大于等于adnroid6.0
            if (sdk_version >= 23){
                GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL,
                        Modifier.TRANSIENT,Modifier.STATIC);
                mGson = gsonBuilder.create();
            }else {
                mGson = new Gson();
            }
        }
    }

    public static OkHttpClientManager getInstance(){
        if (mInstance == null){
            synchronized (OkHttpClientManager.class){
                if (mInstance == null){
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    public OkHttpClient getOkHttpClient(){
        return mOkHttpClient;
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public static PostJsonBuilder postJson(){return new PostJsonBuilder();}

    public Handler getHandler() {
        return mHandler;
    }

    public void execute(final RequestCall requestCall,CallBack callback){
        if (debug) {
            if (TextUtils.isEmpty(tag)) {
                tag = TAG;
            }
            Log.d(tag, "{method:" + requestCall.getRequest().method() + ", detail:" + requestCall.getOkHttpRequest().toString() + "}");
        }
        if (callback == null){
            callback = CallBack.DEFAULT_RESULT_CALLBACK;
        }
        final CallBack callBack = callback;
        requestCall.getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailResultCallback(request,e, callBack);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() >= 400 && response.code() <= 599){
                    try {
                        sendFailResultCallback(requestCall.getRequest(),new RuntimeException(response.body().string()), callBack);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    return;
                }
                Object o = callBack.parseNetworkRespose(response);
                sendSuccessResultCallback(o, callBack);
            }
        });
    }


    public void sendFailResultCallback(final Request request,final Exception e,final CallBack callback){
        if (callback == null) return;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, e);
                callback.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final CallBack callback){
        if (callback == null) return;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    public void cancelTag(Object o){
        mOkHttpClient.cancel(o);
    }

    /**设置证书**/
    public void setCertificates(InputStream... certificates){
        HttpsUtils.setCertificates(getOkHttpClient(), certificates, null, null);
    }
    private boolean debug;
    private String tag;

    public OkHttpClientManager debug(String tag) {
        debug = true;
        this.tag = tag;
        return this;
    }


}
