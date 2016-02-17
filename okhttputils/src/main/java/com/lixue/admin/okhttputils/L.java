package com.lixue.admin.okhttputils;

import android.util.Log;

/**
 * Created by Administrator on 2015/12/7.
 */
public class L {
    private static boolean debug = false;

    public static void e(String msg) {
        if (debug) {
            Log.e("OkHttp", msg);
        }
    }
}
