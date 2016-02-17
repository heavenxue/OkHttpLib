package com.lixue.admin.okhttputils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 基础响应类
 * Created by Administrator on 2015/12/10.
 */
public abstract class Response {
    protected final static String BODY = "body";
    public abstract void bodyReadFrom(JSONObject json);
    public abstract void bodyReadFrom(JSONArray jsonArray);

    public static <T extends Response> T decodeJson(Class<T> clazz,JSONObject json){
        if (json != null){
            T t = null;
            try {
                t = clazz.newInstance();
                t.readFrom(json);
                return t;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static <T extends Response> T decodeJsonArry(Class<T> clazz,JSONArray json){
        if (json != null){
            T t = null;
            try {
                t = clazz.newInstance();
                t.readFrom(json);
                return t;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void readFrom(JSONObject json){
        bodyReadFrom(json);
    }

    public void readFrom(JSONArray jsonArray){
        bodyReadFrom(jsonArray);
    }
}
