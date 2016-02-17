package com.lixue.admin.okhttputils.request;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.Map;

/**
 * Created by Administrator on 2015/12/15.
 */
public class PostJsonRequest extends OkHttpRequest {
    private final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    private MediaType mediaType;
    private String json;

    public PostJsonRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, String json,MediaType mediaType) {
        super(url, tag, params, headers);
        this.json = json;
        this.mediaType = mediaType;
        if (mediaType == null){
            mediaType = MEDIA_TYPE_JSON;
        }
    }

    @Override
    protected RequestBody buildRequestBody() {
        return RequestBody.create(mediaType,json);
    }

    @Override
    protected Request buildRequest(Request.Builder builder, RequestBody requestBody) {
        return builder.post(requestBody).build();
    }
}
