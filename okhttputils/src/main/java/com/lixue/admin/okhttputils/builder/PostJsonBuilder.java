package com.lixue.admin.okhttputils.builder;

import com.lixue.admin.okhttputils.request.RequestCall;
import com.squareup.okhttp.MediaType;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/15.
 */
public class PostJsonBuilder extends OkHttpRequestBuilder {
    private String json;
    private MediaType mediaType;

    public OkHttpRequestBuilder json(String json){
        this.json = json;
        return this;
    }

    public OkHttpRequestBuilder mediaType(MediaType mediaType){
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public PostJsonBuilder url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public PostJsonBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public PostJsonBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public PostJsonBuilder addParams(String key, String value) {
        if (params == null){
            params = new IdentityHashMap<>();
        }
        params.put(key,value);
        return this;
    }

    @Override
    public PostJsonBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public PostJsonBuilder addHeader(String key, String value) {
        if (this.headers == null){
            headers = new IdentityHashMap<>();
        }
        headers.put(key,value);
        return this;
    }

    @Override
    public RequestCall build() {
        return null;
    }
}
