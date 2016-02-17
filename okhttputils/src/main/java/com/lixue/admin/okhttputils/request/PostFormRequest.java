package com.lixue.admin.okhttputils.request;

import com.lixue.admin.okhttputils.OkHttpClientManager;
import com.lixue.admin.okhttputils.builder.PostFormBuilder;
import com.lixue.admin.okhttputils.callback.CallBack;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/15.
 */
public class PostFormRequest extends OkHttpRequest {
    private List<PostFormBuilder.FileInput> files = new ArrayList<>();

    public PostFormRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, List<PostFormBuilder.FileInput> files) {
        super(url, tag, params, headers);
        this.files = files;
    }

    @Override
    protected RequestBody buildRequestBody() {
        if (files == null) {
            FormEncodingBuilder builder = new FormEncodingBuilder();
            addParams(builder);
            return builder.build();

        } else {
            MultipartBuilder multipartbuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
            addParams(multipartbuilder);

            for (int i = 0; i < files.size(); i++) {
                PostFormBuilder.FileInput fileInput = files.get(i);
                RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileInput.filename)), fileInput.file);
                multipartbuilder.addPart(fileBody);
//                multipartbuilder.addFormDataPart(fileInput.key, fileInput.filename, fileBody);
            }
            return multipartbuilder.build();
        }
    }

    @Override
    protected RequestBody wrapRequestBody(RequestBody requestBody, final CallBack callback) {
        if (callback == null) return requestBody;
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength) {

                OkHttpClientManager.getInstance().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        callback.inProgress(bytesWritten * 1.0f / contentLength);
                    }
                });

            }
        });
        return countingRequestBody;
    }

    @Override
    protected Request buildRequest(Request.Builder builder, RequestBody requestBody) {
        return builder.post(requestBody).build();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private void addParams(MultipartBuilder builder) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));

            }
        }
    }

    private void addParams(FormEncodingBuilder builder) {
        if (params == null || !params.isEmpty()) {
            new IllegalArgumentException("params in PostFormRequest can not be empty.");
        }

        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }
    }
}
