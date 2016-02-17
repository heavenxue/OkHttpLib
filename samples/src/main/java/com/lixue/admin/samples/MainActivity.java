package com.lixue.admin.samples;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lixue.admin.okhttputils.OkHttpClientManager;
import com.lixue.admin.okhttputils.callback.BitmapCallback;
import com.lixue.admin.okhttputils.callback.FileCallback;
import com.lixue.admin.okhttputils.callback.JsonCallback;
import com.lixue.admin.okhttputils.callback.StringCallback;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;

//lixue
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "AppCompatActivity";
    private TextView mTv;
    private ImageView mImageView;
    private ProgressBar mProgressBar;


    public class MyStringCallback extends StringCallback {
        @Override
        public void onBefore(Request request) {
            super.onBefore(request);
            setTitle("loading...");
        }

        @Override
        public void onAfter() {
            super.onAfter();
            setTitle("Sample-OkHttp");
        }

        @Override
        public void onError(Request request, Exception e) {
            mTv.setText("onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response) {
            mTv.setText("onResponse:" + response);
        }

        @Override
        public void inProgress(float progress) {
            super.inProgress(progress);
            Log.e(TAG, "inProgress:" + progress);
            mProgressBar.setProgress((int) (100 * progress));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTv = (TextView) findViewById(R.id.id_textview);
        mImageView = (ImageView) findViewById(R.id.id_imageview);
        mProgressBar = (ProgressBar) findViewById(R.id.id_progress);
        mProgressBar.setMax(100);
    }

    public void getUser(View view) {
        String url = "http://cj.weather.com.cn/ajax/citydatasource.aspx?query=%E5%AE%89%E5%BE%BD";
        OkHttpClientManager.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray Jas = new JSONArray(response);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < Jas.length(); i++) {
                        sb.append(Jas.get(i) + "\n");
                    }
                    mTv.setText(sb.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void getUsers(View view) {
//        String url = "http://cj.weather.com.cn/ajax/citydatasource.aspx?query=%E5%AE%89%E5%BE%BD";
//        OkHttpClientManager.get().url(url).build().execute(new StringCallback() {
//            @Override
//            public void onError(Request request, Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONArray Jas = new JSONArray(response);
//                    StringBuilder sb = new StringBuilder();
//                    for (int i = 0; i < Jas.length(); i++) {
//                        sb.append(Jas.get(i) + "\n");
//                    }
//                    mTv.setText(sb.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        String urls = "https://raw.githubusercontent.com/hongyangAndroid/okhttp-utils/master/users.gson";
        OkHttpClientManager.get().url(urls).build().execute(new JsonCallback<Users>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(Users response) {
                StringBuilder sb = new StringBuilder();

                if (response != null && response.users.length> 0){
                    for (User muser : response.users){
                        sb.append("”√ªß√˚:" + muser.username + ",").append("√‹¬Î:" + muser.password+",");
                    }
                }
                mTv.setText(sb.toString());
            }

        });
    }


    public void getHtml(View view) {
        //https://192.168.56.1:8443/
        //https://kyfw.12306.cn/otn/
        //https://192.168.187.1:8443/
        String url = "http://www.csdn.net/";
        OkHttpClientManager.get().url(url).build().execute(new MyStringCallback());
    }

    public void getHttpsHtml(View view) {
        String url ="https://kyfw.12306.cn/otn/";
        OkHttpClientManager.get().url(url).build().execute(new MyStringCallback());
    }

    public void getImage(View view) {
        mTv.setText("");
        String url = "http://images.csdn.net/20150817/1.jpg";
        OkHttpClientManager.get().url(url).build().execute(new BitmapCallback() {
            @Override
            public void onError(Request request, Exception e) {
                mTv.setText("onError:" + e.getMessage());
            }

            @Override
            public void onResponse(Bitmap response) {
                mImageView.setImageBitmap(response);
            }
        });
    }


    public void downloadFile(View view) {
        String url = "https://github.com/hongyangAndroid/okhttp-utils/blob/master/gson-2.2.1.jar?raw=true";
        OkHttpClientManager//
                .get()//
                .url(url)//
                .build()//
                .execute(new FileCallback(Environment.getExternalStorageDirectory().getAbsolutePath(), "gson-2.2.1.jar")//
                {

                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                    }

                    @Override
                    public void inProgress(float progress) {
                        mProgressBar.setProgress((int) (100 * progress));
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e(TAG, "onError :" + e.getMessage());
                    }

                    @Override
                    public void onResponse(File file) {
                        Log.e(TAG, "onResponse :" + file.getAbsolutePath());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        OkHttpClientManager.cancelTag(this);
    }
}
