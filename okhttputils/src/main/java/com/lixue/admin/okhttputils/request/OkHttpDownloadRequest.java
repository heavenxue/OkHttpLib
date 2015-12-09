package com.lixue.admin.okhttputils.request;

import com.lixue.admin.okhttputils.OkHttpClientManager;
import com.lixue.admin.okhttputils.callback.ResultCallBack;
import com.lixue.admin.okhttputils.util.L;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 下载请求
 * Created by admin on 2015/12/9.
 */
public class OkHttpDownloadRequest extends OkHttpGetRequest {
    private String destFileDir;
    private String destFileName;

    public OkHttpDownloadRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers
    ,String destFileDir,String destFileName) {
        super(url, tag, params, headers);
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }

    @Override
    public void invokeAsyn(final ResultCallBack callBack) {
        prepareInvoked(callBack);
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mOkHttpClientManager.sendFailResultCallback(request, e, callBack);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String filePath = saveFile(response, callBack);
                    OkHttpClientManager.getInstance().sendSuccessResultCallback(filePath, callBack);
                } catch (IOException e) {
                    e.printStackTrace();
                    OkHttpClientManager.getInstance().sendFailResultCallback(response.request(), e, callBack);
                }
            }
        });
    }

    @Override
    public <T> T invoke(Class<T> clazz) throws IOException {
        final Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();
        return (T) saveFile(response, null);
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    public String saveFile(Response response, final ResultCallBack callback) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;

            L.e(total + "");

            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);

                if (callback != null) {
                    final long finalSum = sum;
                    mOkHttpClientManager.getHandler().post(new Runnable() {
                        @Override
                        public void run() {

                            callback.inProgress(finalSum * 1.0f / total);
                        }
                    });
                }
            }
            fos.flush();

            return file.getAbsolutePath();

        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
    }
}
