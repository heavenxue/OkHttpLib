package com.lixue.admin.samples;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lixue.admin.okhttputils.callback.ResultCallBack;
import com.lixue.admin.okhttputils.request.OkHttpRequest;
import com.squareup.okhttp.Request;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
//lixue
public class MainActivity extends AppCompatActivity {
    private TextView mTv;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    public abstract class MyResultCallback<T> extends ResultCallBack<T> {
        @Override
        public void onBefor() {
            super.onBefor();
            setTitle("loading...");
        }

        @Override
        public void onAfter() {
            super.onAfter();
            setTitle("Sample-okHttp");
        }
    }

    private ResultCallBack<String> stringResultCallback = new MyResultCallback<String>() {
        @Override
        public void onError(Request request, Exception e) {
            Log.e("TAG", "onError , e = " + e.getMessage());
        }

        @Override
        public void onResponse(String response) {
            Log.e("TAG", "onResponse , response = " + response);
            mTv.setText("operate success");
        }

        @Override
        public void inProgress(float progress) {
            mProgressBar.setProgress((int) (100 * progress));
        }
    };

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

        String url = "https://raw.githubusercontent.com/hongyangAndroid/okhttp-utils/master/user.gson";
//        url = "http://192.168.56.1:8080/test/user.do?action=login&username=fusheng&password=123";
        new OkHttpRequest.Builder()
                .url(url)
                .get(new MyResultCallback<User>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("TAG", "onError , e = " + e.getMessage());
                    }

                    @Override
                    public void onResponse(User response) {
                        Log.e("TAG", "onResponse , user = " + response);
                        mTv.setText(response.username);
                    }
                });

//        new Thread()
//        {
//            @Override
//            public void run()
//            {
//                try
//                {
//                    User u = new OkHttpRequest.Builder().url(url).get(User.class);
//                    Log.e("TAG", "syn u = " + u);
//                } catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//
//            }
//        }.start();


    }


    public void getUsers(View view) {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("name", "zhy");
//        String url = "https://raw.githubusercontent.com/hongyangAndroid/okhttp-utils/master/users.gson";
//        new OkHttpRequest.Builder().url(url).params(params).post(new MyResultCallback<List<User>>() {
//            @Override
//            public void onError(Request request, Exception e) {
//                Log.e("TAG", "onError , e = " + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(List<User> users) {
//                Log.e("TAG", "onResponse , users = " + users);
//                mTv.setText(users.get(0).toString());
//            }
//        });
//        Map<String,String> params = new HashMap<>();
//        params.put("uid","12451");
//        params.put("region","US");
//        params.put("cfgVersion","0");
//        params.put("language","EN");
//        params.put("aid","3000669142");
//        params.put("clientVersion","3.1.3_global");
//        params.put("clientType","1");
        String js = "{'uid:'12451','region':'US','cfgVersion':0,'language':'EN','aid':'3000669142','clientVersion':'3.1.3_global','clientType':1}";
        String url = "https://pay.halodigit.com/overseas/survey/get";
        new OkHttpRequest.Builder().url(url).json(js).post(new MyResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                mTv.setText(response);
            }
        });


    }

    public void getSimpleString(View view) {
        String url = "https://raw.githubusercontent.com/hongyangAndroid/okhttp-utils/master/user.gson";

        new OkHttpRequest.Builder().url(url)
                .get(new MyResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("TAG", "onError , e = " + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        mTv.setText(response);
                    }
                });

    }

    public void getHtml(View view) {
        //https://192.168.56.1:8443/
        //https://kyfw.12306.cn/otn/
        //https://192.168.187.1:8443/
        String url = "http://www.baidu.com/";
        new OkHttpRequest.Builder().url(url).get(new MyResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.e("TAG", "onError" + e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                mTv.setText(response);
            }
        });
    }

    public void getHttpsHtml(View view) {
        String url = "https://kyfw.12306.cn/otn/";
        new OkHttpRequest.Builder().url(url).get(new MyResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.e("TAG", "onError" + e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                mTv.setText(response);
            }
        });
    }

    public void getImage(View view) {
        String url = "http://images.csdn.net/20150817/1.jpg";
        mTv.setText("");
        new OkHttpRequest.Builder().url(url).imageView(mImageView).displayImage(null);
    }


    public void uploadFile(View view) {
//
//        File file = new File(Environment.getExternalStorageDirectory(), "messenger_01.png");
//        if (!file.exists()) {
//            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
//            return;
//        }
        Map<String, String> params = new HashMap<>();
        params.put("username", "张鸿洋");
        params.put("password", "123");

        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");

        String url = "http://192.168.56.1:8080/okHttpServer/fileUpload";
        new OkHttpRequest.Builder()//
                .url(url)//
                .params(params)
                .headers(headers)
//                .files(new Pair<String, File>("mFile", file))//
                .upload(stringResultCallback);
    }


    public void multiFileUpload(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "messenger_01.png");
        File file2 = new File(Environment.getExternalStorageDirectory(), "test1.txt");
        if (!file.exists()) {
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("username", "张鸿洋");
        params.put("password", "123");

        String url = "http://192.168.1.103:8080/okHttpServer/mulFileUpload";
        new OkHttpRequest.Builder()//
                .url(url)//
                .params(params)
                .files(new Pair<String, File>("mFile", file), new Pair<String, File>("mFile", file2))//
                .upload(stringResultCallback);


    }


    public void downloadFile(View view) {
        String url = "https://github.com/hongyangAndroid/okhttp-utils/blob/master/gson-2.2.1.jar?raw=true";
        new OkHttpRequest.Builder()
                .url(url)
                .destFileDir(Environment.getExternalStorageDirectory().getAbsolutePath())
                .destFileName("gson-2.2.1.jar")
                .download(stringResultCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        OkHttpClientManager.cancelTag(this);
    }
}
