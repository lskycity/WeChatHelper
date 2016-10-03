package com.zhaofliu.wechathelper.app;

import android.app.Application;

/**
 * Created by liuzhaofeng on 2016/2/13.
 */
public class DroidApplication extends Application {
    public static DroidApplication instance;
    public DroidApplication() {
        instance = this;
    }
}
