package com.lixue.admin.okhttputils.callback;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class CallBack<T>{
//    public Type mType;
    public CallBack(){
//        mType = getSuperclassTypeParameter(getClass());
    }
    static Type getSuperclassTypeParameter(Class<?> subclass){
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class){
            return null;
        }else {
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }
    }

    public Class<?> getTClass(){
        Class<?> tClass = (Class<?>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }
    /**UI thread**/
    public void onBefore(Request request){
    }
    /**UI thread**/
    public void onAfter() {
    }
    /**UI thread**/
    public void inProgress(float progress) {

    }
    /**Thread pool Thread**/
    public abstract T parseNetworkRespose(Response response) throws IOException;
    public abstract void onError(Request request, Exception e);

    public abstract void onResponse(T response);

    //默认回调方法
    public static final CallBack DEFAULT_RESULT_CALLBACK = new CallBack() {
        @Override
        public Object parseNetworkRespose(Response response) throws IOException {
            return null;
        }

        @Override
        public void onError(Request request, Exception e){

        }

        @Override
        public void onResponse(Object response) {

        }

    };
}