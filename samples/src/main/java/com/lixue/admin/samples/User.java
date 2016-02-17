package com.lixue.admin.samples;

import com.lixue.admin.okhttputils.ABSIO;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lixue on 2015/12/9.
 */
public class User extends ABSIO {

    public String username ;
    public String password  ;


    @Override
    public void readFrom(JSONObject json) throws JSONException {
        this.username = json.getString("username");
        this.password = json.getString("password");
    }

    @Override
    public JSONObject writeTo(JSONObject json) throws JSONException {
        return null;
    }
}