package com.lixue.admin.okhttputils.request;

import android.text.TextUtils;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.Map;

/**
 * Get请求
 * Created by admin on 2015/12/9.
 */
public class OkHttpGetRequest extends OkHttpRequest {
    public OkHttpGetRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers) {
        super(url, tag, params, headers);
    }

    @Override
    protected Request buildRequest() {
        if (TextUtils.isEmpty(url)){
            throw new IllegalArgumentException("url can not be empty!");
        }
        //append params,if nesseary
        if (params != null) url =  appendPrams(url,params);

        Request.Builder builder = new Request.Builder();
        //add headers,if nesseary
        appendHeaders(builder,headers);
        builder.url(url).tag(tag);
        return builder.build();
    }

    @Override
    protected RequestBody buildRequestBody() {
        return null;
    }

    private String appendPrams(String url,Map<String,String> params){
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        for (String key: params.keySet()){
            sb.append(key).append("=").append(params.get(key)).append("&");
        }
        sb = sb.deleteCharAt(sb.length() -1);//截掉最后一个&
        return sb.toString();
    }
}
