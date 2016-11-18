package com.anke.vehicle.kuangjia;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2016/11/18 0018.
 */
public class MyApplication extends Application {
    public static RequestQueue mQueue ;
    @Override
    public void onCreate() {
        super.onCreate();
        mQueue = Volley.newRequestQueue(this);
    }
    public static RequestQueue getRequestQueue(){
        return  mQueue;
    }
}
