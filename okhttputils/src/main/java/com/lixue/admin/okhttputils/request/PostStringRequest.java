package com.lixue.admin.okhttputils.request;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.Map;

/**
 * Created by Administrator on 2015/12/15.
 */
public class PostStringRequest extends OkHttpRequest {
    private static MediaType MEDIA_TYPE_PLAN = MediaType.parse("text/plain;charset=utf-8");
    private String content;
    private MediaType mediaType;

    public PostStringRequest(String url,Object tag,Map<String,String> params,Map<String,String> headers,String content,
                             MediaType mediaType){
        super(url,tag,params,headers);
        this.content = content;
        this.mediaType = mediaType;
        if (this.content == null){
            new IllegalArgumentException("the content can not be null !");
        }
        if (this.mediaType == null){
            this.mediaType = MEDIA_TYPE_PLAN;
        }
    }

    @Override
    protected RequestBody buildRequestBody() {
        return RequestBody.create(mediaType,content);
    }

    @Override
    protected Request buildRequest(Request.Builder builder, RequestBody requestBody) {
        return builder.post(requestBody).build();
    }
}
