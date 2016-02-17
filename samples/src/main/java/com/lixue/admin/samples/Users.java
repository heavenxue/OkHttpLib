package com.lixue.admin.samples;

import com.lixue.admin.okhttputils.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/2/17.
 */
public class Users extends Response {
    public User[] users;

    @Override
    public void bodyReadFrom(JSONObject json) {

    }

    @Override
    public void bodyReadFrom(JSONArray jsonArray) {
        users = new User[jsonArray.length()];
        for (int i = 0;i < jsonArray.length();i++){
            try {
                users[i] = new User();
                users[i].username = jsonArray.getJSONObject(i).getString("username");
                users[i].password = jsonArray.getJSONObject(i).getString("password");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
