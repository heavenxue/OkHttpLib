package com.lixue.admin.okhttputils.callback;

import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2015/12/15.
 */
public abstract class JsonCallback<T> extends CallBack<T> {
    @Override
    public T parseNetworkRespose(Response response) throws IOException {
        final String string = response.body().string();
            Class<?> tClass = getTClass();
            if (tClass != null) {
                try {
                    //调用指定方法并获取返回值为Object类型
                    Object obj = tClass.newInstance();
                    Object[] params = new Object[2];
                    params[0] = tClass;
                    if (string.startsWith("[") && string.endsWith("]")){
                        Class paramTypes[] = getParamTypes(tClass.getSuperclass(), "decodeJsonArry");
                        Method method = tClass.getSuperclass().getMethod("decodeJsonArry", paramTypes);
                        method.setAccessible(true);

                        JSONArray jsonArray = new JSONArray(string);
                        params[1] = jsonArray;

                        Object o = method.invoke(obj, params);
                        return (T)o;
                    }else{
                        Class paramTypes[] = getParamTypes(tClass.getSuperclass(), "decodeJson");
                        Method method = tClass.getSuperclass().getMethod("decodeJson", paramTypes);
                        method.setAccessible(true);

                        JSONObject jsonObject = new JSONObject(string);
                        params[1] = jsonObject;
                        Object o = method.invoke(obj, params);
                        return (T)o;
                    }



                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return null;
    }
    /**
     * 获取参数类型，返回值保存在Class[]中
     */
    public Class[] getParamTypes(Class cls, String mName) {
        Class[] cs = null;
        /*
        * Note: 由于我们一般通过反射机制调用的方法，是非public方法
        * 所以在此处使用了getDeclaredMethods()方法
         */
        Method[] mtd = cls.getDeclaredMethods();
        for (int i = 0; i < mtd.length; i++) {
            if (!mtd[i].getName().equals(mName)) { // 不是我们需要的参数，则进入下一次循环
                continue;
            }
            cs = mtd[i].getParameterTypes();
        }
        return cs;
    }
}
