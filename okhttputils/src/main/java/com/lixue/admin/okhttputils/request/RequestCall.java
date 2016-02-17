package com.lixue.admin.okhttputils.request;

import com.lixue.admin.okhttputils.OkHttpClientManager;
import com.lixue.admin.okhttputils.callback.CallBack;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/12/15.
 */
public class RequestCall {
    private OkHttpRequest okHttpRequest;
    private Request request;
    private Call call;

    private long readTimeOut;
    private long writeTimeOut;
    private long connTimeOut;

    private OkHttpClient client;

    public RequestCall(OkHttpRequest request){
        this.okHttpRequest = request;
    }

    public RequestCall readTimeOut(long readTimeOut){
        this.readTimeOut = readTimeOut;
        return this;
    }

    public RequestCall writeTimeOut(long writeTimeOut){
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    public RequestCall connTimeOut(long connTimeOut){
        this.connTimeOut = connTimeOut;
        return this;
    }

    public Call generateCall(CallBack callback){
        request = generateRequest(callback);
        if (readTimeOut > 0 || writeTimeOut > 0 || connTimeOut > 0){
            cloneClient();
            readTimeOut = readTimeOut > 0 ? readTimeOut : OkHttpClientManager.DEFAULT_MILLISECONDS;
            writeTimeOut = writeTimeOut > 0 ? writeTimeOut : OkHttpClientManager.DEFAULT_MILLISECONDS;
            connTimeOut = connTimeOut > 0 ? connTimeOut : OkHttpClientManager.DEFAULT_MILLISECONDS;

            client.setReadTimeout(readTimeOut, TimeUnit.MILLISECONDS);
            client.setWriteTimeout(writeTimeOut,TimeUnit.MILLISECONDS);
            client.setConnectTimeout(connTimeOut,TimeUnit.MILLISECONDS);

            call = client.newCall(request);
        }else{
            call = OkHttpClientManager.getInstance().getOkHttpClient().newCall(request);
        }
        return call;
    }

    public Request generateRequest(CallBack callback){
        return okHttpRequest.generateRequest(callback);
    }

    public void execute(CallBack callback){
        generateCall(callback);
        if (callback != null){
            callback.onBefore(request);
        }
        OkHttpClientManager.getInstance().execute(this,callback);
    }

    public Call getCall() {
        return call;
    }

    public Request getRequest() {
        return request;
    }

    public OkHttpRequest getOkHttpRequest() {
        return okHttpRequest;
    }

    public Response execute() throws IOException{
        generateCall(null);
        return call.execute();
    }

    private void cloneClient(){
        client = OkHttpClientManager.getInstance().getOkHttpClient().clone();
    }

    public void cancel(){
        if (call != null){
            call.cancel();
        }
    }
}
