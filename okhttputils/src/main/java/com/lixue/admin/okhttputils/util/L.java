package com.lixue.admin.okhttputils.util;

import android.util.Log;

/**
 * LOG日志
 * Created by admin on 2015/12/9.
 */
public class L
{
    private static boolean debug = false;

    public static void e(String msg)
    {
        if (debug)
        {
            Log.e("OkHttp", msg);
        }
    }

}
