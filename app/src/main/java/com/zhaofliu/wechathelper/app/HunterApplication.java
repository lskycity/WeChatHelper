package com.zhaofliu.wechathelper.app;

import android.app.Application;

/**
 *  main application
 *
 */
public class HunterApplication extends Application {
    public static HunterApplication instance;
    public HunterApplication() {
        instance = this;
    }

    public static HunterApplication get() {
        return  instance;
    }
}
