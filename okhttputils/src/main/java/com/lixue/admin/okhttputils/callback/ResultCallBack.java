package com.lixue.admin.okhttputils.callback;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 响应结果回调
 * Created by lixue on 2015/12/9.
 */
public abstract class ResultCallBack<T> {
    public Type mType;
    public ResultCallBack(){
        mType = getSuperClassTypeParameter(getClass());
    }
    /**传入泛型类，处理**/
    static Type getSuperClassTypeParameter(Class<?> clazz){
       Type superclass =  clazz.getGenericSuperclass();
        if (superclass instanceof Class){
            throw new RuntimeException("Mis sing type parameter");
        }
        ParameterizedType parameterizedType = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
    }
    public void onAfter(){}
    public void onBefor(){}
    public void inProgress(float progress){}
    public abstract void onError(Request request,Exception e);
    public abstract void onResponse(T response);

    /**默认回调是以字符串返回结果**/
    public final static ResultCallBack<String> DEFAULT_RESULT_CALLBACK = new ResultCallBack<String>() {
        @Override
        public void onError(Request request, Exception e) {

        }

        @Override
        public void onResponse(String response) {

        }

    };
}
