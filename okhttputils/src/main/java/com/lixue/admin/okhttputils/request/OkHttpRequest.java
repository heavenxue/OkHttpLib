package com.lixue.admin.okhttputils.request;

import android.util.Pair;
import android.widget.ImageView;

import com.lixue.admin.okhttputils.OkHttpClientManager;
import com.lixue.admin.okhttputils.callback.ResultCallBack;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 *  基础抽象类
 *
 * Created by admin on 2015/12/9.
 */
public abstract class OkHttpRequest {
    protected OkHttpClientManager mOkHttpClientManager = OkHttpClientManager.getInstance();
    protected OkHttpClient mOkHttpClient;

    protected RequestBody requestBody;
    protected Request request;

    protected String url;
    protected Object tag;
    protected Map<String,String> params;
    protected Map<String,String> headers;

    public OkHttpRequest(String url,Object tag,Map<String, String> params, Map<String, String> headers) {
        mOkHttpClient = mOkHttpClientManager.getOkHttpClient();
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
    }

    protected abstract Request buildRequest();

    protected abstract RequestBody buildRequestBody();

    protected void prepareInvoked(ResultCallBack callBack){
        requestBody = buildRequestBody();
        requestBody = wrapRequestBody(requestBody, callBack);
        request = buildRequest();
    }
    //用作别的request重写
    protected RequestBody wrapRequestBody(RequestBody requestBody,final ResultCallBack callBack){
        return requestBody;
    }

    public void invokeAsyn(ResultCallBack callBack){
        prepareInvoked(callBack);
        mOkHttpClientManager.execute(request,callBack);
    }

    public <T> T invoke(Class<T> clazz) throws IOException {
        requestBody = buildRequestBody();
        Request request = buildRequest();
        return mOkHttpClientManager.execute(request,clazz);
    }

    protected void appendHeaders(Request.Builder builder,Map<String,String> headers){
        if (builder == null) throw new IllegalArgumentException("builder can not be empty!");
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;
        for (String key: headers.keySet()){
            headerBuilder.add(key,headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    public void cancle(){
        if (tag != null){
            mOkHttpClientManager.cancleTag(tag);
        }
    }

    public static class Builder {
        private String url;
        private Object tag;
        private Map<String, String> headers;
        private Map<String, String> params;
        private Pair<String, File>[] files;
        private String json;
        private MediaType mediaType;

        private String destFileDir;
        private String destFileName;

        private ImageView imageView;
        private int errorResId = -1;

        //for post
        private String content;
        private byte[] bytes;
        private File file;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }

        public Builder params(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public Builder addParams(String key, String val) {
            if (this.params == null) {
                params = new IdentityHashMap<>();
            }
            params.put(key, val);
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder addHeader(String key, String val) {
            if (this.headers == null) {
                headers = new IdentityHashMap<>();
            }
            headers.put(key, val);
            return this;
        }


        public Builder files(Pair<String, File>... files) {
            this.files = files;
            return this;
        }

        public Builder destFileName(String destFileName) {
            this.destFileName = destFileName;
            return this;
        }

        public Builder destFileDir(String destFileDir) {
            this.destFileDir = destFileDir;
            return this;
        }


        public Builder imageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Builder errResId(int errorResId) {
            this.errorResId = errorResId;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder mediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }
        public Builder json(String json){
            this.json = json;
            return this;
        }

        public <T> T get(Class<T> clazz) throws IOException {
            OkHttpRequest request = new OkHttpGetRequest(url, tag, params, headers);
            return request.invoke(clazz);
        }

        public OkHttpRequest get(ResultCallBack callback) {
            OkHttpRequest request = new OkHttpGetRequest(url, tag, params, headers);
            request.invokeAsyn(callback);
            return request;
        }

        public <T> T post(Class<T> clazz) throws IOException {
            OkHttpRequest request = new OkHttpPostRequest(url, tag, params, headers, mediaType, content, bytes, file,json);
            return request.invoke(clazz);
        }

        public OkHttpRequest post(ResultCallBack callback) {
            OkHttpRequest request = new OkHttpPostRequest(url, tag, params, headers, mediaType, content, bytes, file,json);
            request.invokeAsyn(callback);
            return request;
        }

        public OkHttpRequest upload(ResultCallBack callback) {
            OkHttpRequest request = new OkHttpUploadRequest(url, tag, params, headers, files);
            request.invokeAsyn(callback);
            return request;
        }

        public <T> T upload(Class<T> clazz) throws IOException {
            OkHttpRequest request = new OkHttpUploadRequest(url, tag, params, headers, files);
            return request.invoke(clazz);
        }


        public OkHttpRequest download(ResultCallBack callback) {
            OkHttpRequest request = new OkHttpDownloadRequest(url, tag, params, headers, destFileDir, destFileName);
            request.invokeAsyn(callback);
            return request;
        }

        public String download() throws IOException {
            OkHttpRequest request = new OkHttpDownloadRequest(url, tag, params, headers, destFileName, destFileDir);
            return request.invoke(String.class);
        }

        public void displayImage(ResultCallBack callback) {
            OkHttpRequest request = new OkHttpDisplayImgRequest(url, tag, params, headers, imageView, errorResId);
            request.invokeAsyn(callback);
        }


    }

}
