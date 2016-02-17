package com.lixue.admin.okhttputils.request;

import com.lixue.admin.okhttputils.OkHttpClientManager;
import com.lixue.admin.okhttputils.callback.CallBack;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.Map;

/**
 * Created by Administrator on 2015/12/7.
 */
public abstract class OkHttpRequest {
    protected OkHttpClientManager mOkHttpClientManager = OkHttpClientManager.getInstance();
    protected OkHttpClient mOkHttpClient;

    protected RequestBody requestBody;
    protected Request request;

    protected String url;
    protected Object tag;
    protected Map<String, String> params;
    protected Map<String, String> headers;

    protected Request.Builder builder = new Request.Builder();



    protected OkHttpRequest(String url, Object tag,Map<String, String> params, Map<String, String> headers) {
        mOkHttpClient = mOkHttpClientManager.getOkHttpClient();
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
        if (url == null){
            new IllegalArgumentException("url can not be null");
        }
    }

    protected abstract RequestBody buildRequestBody();

    protected RequestBody wrapRequestBody(RequestBody requestBody,final CallBack callback){
        return requestBody;
    }

    protected abstract Request buildRequest(Request.Builder builder,RequestBody requestBody);

    public RequestCall build(){
        return new RequestCall(this);
    }

    public Request generateRequest(CallBack callback){
        RequestBody requestBody = wrapRequestBody(buildRequestBody(),callback);
        prepareBuilder();
        return buildRequest(builder,requestBody);
    }

    private void prepareBuilder(){
        builder.url(url).tag(tag);
        appendHeaders();
    }

    protected void appendHeaders() {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;

        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    public void cancel() {
        if (tag != null)
            mOkHttpClientManager.cancelTag(tag);
    }

    @Override
    public String toString() {
        return "OkHttpRequest{" +
                "url='" + url + '\'' +
                ", tag=" + tag +
                ", params=" + params +
                ", headers=" + headers +
                '}';
    }
}
