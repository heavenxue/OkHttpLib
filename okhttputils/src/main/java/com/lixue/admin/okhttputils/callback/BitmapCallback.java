package com.lixue.admin.okhttputils.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Administrator on 2015/12/15.
 */
public abstract class BitmapCallback extends CallBack<Bitmap> {
    @Override
    public Bitmap parseNetworkRespose(Response response) throws IOException {
        return BitmapFactory.decodeStream(response.body().byteStream());
    }
}
