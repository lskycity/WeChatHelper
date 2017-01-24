package com.zhaofliu.wechathelper.apputils;

/**
 * Created by zhaofliu on 1/2/17.
 */

public class VersionInfo {
    public String packageName;
    public int versionCode;
    public String versionName;
    public String downloadUrl;
    public String urlForWandoujia;

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
