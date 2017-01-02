package com.zhaofliu.wechathelper.ui;

/**
 * Created by zhaofliu on 1/2/17.
 */

public class VersionInfo {
    String packageName;
    int versionCode;
    String versionName;
    String downloadUrl;

    @Override
    public String toString() {
        return "VersionInfo{" +
                "packageName='" + packageName + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", versionName='" + versionName + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}
