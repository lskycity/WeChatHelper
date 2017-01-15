package com.zhaofliu.wechathelper.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 *  main application
 *
 */
public class HunterApplication extends Application {
    public static HunterApplication instance;

    private RequestQueue mRequestQueue;

    public HunterApplication() {
        instance = this;
    }

    public static HunterApplication get() {
        return  instance;
    }

    public RequestQueue getRequestQueue() {
        if(mRequestQueue==null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }
        return mRequestQueue;
    }
}
