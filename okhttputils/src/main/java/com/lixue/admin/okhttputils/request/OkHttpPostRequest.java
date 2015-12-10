package com.lixue.admin.okhttputils.request;

import android.text.TextUtils;

import com.lixue.admin.okhttputils.callback.ResultCallBack;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.Map;

/**
 * post请求
 * Created by lixue on 2015/12/9.
 */
public class OkHttpPostRequest extends OkHttpRequest {
    //mediaType, content, bytes, file
    private MediaType mediaType;
    private String content;
    private byte[] bytes;
    private File file;
    private String json;

    private int type = 0;
    private static final int TYPE_PARAMS = 1;
    private static final int TYPE_STRING = 2;
    private static final int TYPE_BYTES = 3;
    private static final int TYPE_FILE = 4;
    private static final int TYPE_JSON = 5;

    private final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
    private final MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");
    private final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    public OkHttpPostRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers
    ,MediaType mediaType,String content,byte[] byteps,File file,String json) {
        super(url, tag, params, headers);
        this.mediaType = mediaType;
        this.content = content;
        this.bytes = byteps;
        this.file = file;
        this.json = json;
    }

    @Override
    protected Request buildRequest() {
        if (TextUtils.isEmpty(url)){
            throw new IllegalArgumentException("url can not be empty!");
        }
        Request.Builder builder = new Request.Builder();
        appendHeaders(builder,headers);
        builder.url(url).tag(tag).post(requestBody);
        return builder.build();
    }

    @Override
    protected RequestBody buildRequestBody() {
        validParams();
        RequestBody requestBody = null;
        switch (type){
            case TYPE_STRING:
                requestBody = RequestBody.create(mediaType != null ? mediaType :MEDIA_TYPE_STRING,content);
                break;
            case TYPE_PARAMS:
                FormEncodingBuilder builder = new FormEncodingBuilder();
                addParams(builder,params);
                requestBody = builder.build();
                break;
            case TYPE_BYTES:
                requestBody = RequestBody.create(mediaType != null ? mediaType : MEDIA_TYPE_STREAM, bytes);
                break;
            case TYPE_FILE:
                requestBody = RequestBody.create(mediaType != null ? mediaType :MEDIA_TYPE_STREAM,file);
                break;
            case TYPE_JSON:
                requestBody = RequestBody.create(mediaType != null ? mediaType : MEDIA_TYPE_JSON,json);
                break;

        }
        return requestBody;
    }

    @Override
    protected RequestBody wrapRequestBody(RequestBody requestBody, final ResultCallBack callBack) {
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength) {
                mOkHttpClientManager.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.inProgress(bytesWritten * 1.0f /contentLength);
                    }
                });
            }
        });
        return super.wrapRequestBody(requestBody, callBack);
    }

    /**验证参数只能有一个**/
    protected void validParams(){
        int count = 0;
        if (params != null && !params.isEmpty()){
            type = TYPE_PARAMS;
            count ++;
        }
        if (content != null){
            type = TYPE_STRING;
            count ++;
        }
        if (bytes != null){
            type = TYPE_BYTES;
            count ++;
        }
        if (file != null){
            type = TYPE_FILE;
            count ++;
        }
        if (json != null){
            type = TYPE_JSON;
            count++;
        }
        if (count <= 0 || count > 1){
            throw new IllegalArgumentException("the params , content , file , bytes must has one and only one .");
        }
    }

    private void addParams(FormEncodingBuilder builder, Map<String, String> params) {
        if (builder == null) {
            throw new IllegalArgumentException("builder can not be null .");
        }

        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
    }
}
