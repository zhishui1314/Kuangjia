package com.anke.vehicle.kuangjia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import Utils.VolleyUtils;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private ImageView imageView;
    String getUrl = "https://api.github.com/repos/square/okhttp/contributors";
    String url = "http://172.16.100.100:8810/";
    String imgUrl = "https://www.baidu.com/img/bd_logo1.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = (TextView) findViewById(R.id.tvResult);
        imageView = (ImageView) findViewById(R.id.imgs);
    }
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
    public void vtu(View view){
        VolleyUtils.getInstance().imageMethod(imgUrl,imageView);

    }
}
