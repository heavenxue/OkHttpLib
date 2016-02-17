package com.lixue.admin.okhttputils.builder;


import com.lixue.admin.okhttputils.request.RequestCall;

import java.util.Map;

/**
 * Created by Administrator on 2015/12/15.
 */
public abstract class OkHttpRequestBuilder {
    protected String url;
    protected Object tag;
    protected Map<String,String> headers;
    protected Map<String,String> params;

    public abstract OkHttpRequestBuilder url(String url);
    public abstract OkHttpRequestBuilder tag(Object tag);
    public abstract OkHttpRequestBuilder params(Map<String,String> params);
    public abstract OkHttpRequestBuilder addParams(String key,String value);
    public abstract OkHttpRequestBuilder headers(Map<String,String> headers);
    public abstract OkHttpRequestBuilder addHeader(String key,String value);
    public abstract RequestCall build();


}
