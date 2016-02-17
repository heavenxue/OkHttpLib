package com.lixue.admin.okhttputils.callback;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Administrator on 2015/12/15.
 */
public abstract class StringCallback extends CallBack<String> {
    @Override
    public String parseNetworkRespose(Response response) throws IOException {
        return response.body().string();
    }
}
