package com.lixue.admin.okhttputils.request;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.Map;

/**
 * Created by Administrator on 2015/12/7.
 */
public class GetRequest extends OkHttpRequest {
    public GetRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers) {
        super(url, tag, params, headers);
    }

    @Override
    protected RequestBody buildRequestBody() {
        return null;
    }

    @Override
    protected Request buildRequest(Request.Builder builder, RequestBody requestBody) {
        return builder.get().build();
    }

}
