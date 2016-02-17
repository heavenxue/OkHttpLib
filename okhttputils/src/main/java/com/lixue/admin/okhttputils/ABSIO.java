package com.lixue.admin.okhttputils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;

/**
 * Created by Administrator on 2015/12/8.
 */
public abstract class ABSIO implements Serializable {
    private static final long serialVersionUID = -2162335062481229878L;

    public abstract void readFrom(JSONObject json) throws JSONException;

    public abstract JSONObject writeTo(JSONObject json) throws JSONException;

    @SuppressWarnings("unchecked")
    public static <T extends ABSIO> T[] decodeSchemaArray(Class<T> clazz, String key, JSONObject json) {

        if (json != null && json.has(key)) {
            try {
                JSONArray rsc = json.getJSONArray(key);
                T[] list = (T[]) Array.newInstance(clazz, rsc.length());
                for (int i = 0; i < rsc.length(); i++) {
                    list[i] = clazz.newInstance();
                    list[i].readFrom((JSONObject) rsc.get(i));
                }
                return list;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T extends ABSIO> T decodeSchema(Class<T> clazz, JSONObject datas) {
        if (null != datas && datas.length() > 0) {
            try {
                T t = clazz.newInstance();
                t.readFrom(datas);
                return t;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {

            }
        }

        return null;
    }
}
