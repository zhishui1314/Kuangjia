package com.anke.vehicle.kuangjia;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import Utils.OkHttpUtils;
import Utils.VolleyUtils;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private ImageView imageView;
    String getUrl = "https://api.github.com/repos/square/okhttp/contributors";
    String url = "http://172.16.100.100:8810/";
    String imgUrl = "https://www.baidu.com/img/bd_logo1.png";
    String upUrl = "http://172.16.100.58:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = (TextView) findViewById(R.id.tvResult);
        imageView = (ImageView) findViewById(R.id.imgs);
    }

    /**
     * get请求
     * @param view
     */
    public void vget(View view){
        VolleyUtils.getInstance().getStringMethod(getUrl, new VolleyUtils.VolleyUtilsListener() {
            @Override
            public void onResult(String result) {
                     if (!TextUtils.isEmpty(result)){
                         tvResult.setText(result);
                     }
            }
        });
    }

    /**
     * post请求
     * @param view
     */
    public void vpost(View view){
        Map<String,String> map = new HashMap<String,String>();
        map.put("type", "QueryStop");
        VolleyUtils.getInstance().postStringMethod(url, map, new VolleyUtils.VolleyUtilsListener() {
            @Override
            public void onResult(String result) {
                if (!TextUtils.isEmpty(result)){
                    tvResult.setText(result);
                }
            }
        });
    }

    /**
     * 图片请求，可以缓存
     * @param view
     */
    public void vtu(View view){
        VolleyUtils.getInstance().imageMethod(imgUrl,imageView);

    }

    /**
     * okhttp
     * @param view
     */
    public void okget(View view){
        new Thread(){
            @Override
            public void run() {
                final String result = OkHttpUtils.getInstance(MainActivity.this).doGet(getUrl);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText(result);
                    }
                });
            }
        }.start();

    }
    public void okgetsync(View view){
        OkHttpUtils.getInstance(this).doGetAsync(getUrl, new OkHttpUtils.OnResultListener() {
            @Override
            public void onResult(String onResult) {
                if (!TextUtils.isEmpty(onResult)){
                    tvResult.setText(onResult);
                }
            }
        });
    }
    public void okpost(View view){
        final Map<String,String> map = new HashMap<String,String>();
        map.put("type", "QueryStop");
              new Thread(){
                  @Override
                  public void run() {
                      final String resullt = OkHttpUtils.getInstance(MainActivity.this).doPost(url, map);
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              tvResult.setText(resullt);
                          }
                      });
                  }
              }.start();
    }
    public void okpostsync(View view){
        Map<String,String> map = new HashMap<String,String>();
        map.put("type", "QueryStop");
        OkHttpUtils.getInstance(this).doPostAsync(url, map, new OkHttpUtils.OnResultListener() {
            @Override
            public void onResult(String onResult) {
                if (!TextUtils.isEmpty(onResult)){
                    tvResult.setText(onResult);
                }
            }
        });
    }

}
