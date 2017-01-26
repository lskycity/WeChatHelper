package com.zhaofliu.wechathelper.apputils;

import com.zhaofliu.wechathelper.utils.DateUtils;

/**
 * Created by zhaofliu on 1/2/17.
 */

public class VersionInfo {
    public String packageName;
    public int versionCode;
    public String versionName;
    public String downloadUrl;
    public String checkTime;

    @Override
    public String toString() {
        return "VersionInfo{" +
                "packageName='" + packageName + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", versionName='" + versionName + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }

    public void setCheckTime(long time) {
        checkTime = String.valueOf(time);
    }

    public void setCheckTime(String time) {
        checkTime = time;
    }

    public void setCurrentTimeToCheckTime() {
        checkTime = String.valueOf(System.currentTimeMillis());
    }

    public String getFormatCheckTime() {
        return DateUtils.getTimeString(Long.valueOf(checkTime));
    }

}
