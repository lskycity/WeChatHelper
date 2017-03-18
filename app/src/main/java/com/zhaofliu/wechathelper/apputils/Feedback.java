package com.zhaofliu.wechathelper.apputils;

import android.content.Context;
import android.os.Build;

import com.lskycity.support.utils.AppUtils;
import com.lskycity.support.utils.DeviceUtils;
import com.zhaofliu.wechathelper.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zhaofliu on 1/15/17.
 */

public class Feedback {
    public String deviceId;
    public String description;
    public String appVersion;
    public String deviceBrand;
    public String deviceMode;
    public String deviceVersion;
    public String feedbackTime;
    public String applicationId;

    public static Feedback obtain(JSONObject jsonObject) {
        Feedback object = new Feedback();
        try {
            object.deviceId = jsonObject.getString("device_id");
            object.description = jsonObject.getString("description");
            object.appVersion = jsonObject.getString("app_version");
            object.deviceBrand = jsonObject.getString("device_brand");
            object.deviceMode = jsonObject.getString("device_mode");
            object.deviceVersion = jsonObject.getString("device_version");
            object.feedbackTime = jsonObject.getString("feedback_time");
            object.applicationId = jsonObject.getString("application_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static Feedback obtain(Context context, String description) {
        Feedback object = new Feedback();
        object.deviceId = DeviceUtils.getDeviceId(context);
        object.description = description;
        object.appVersion = AppUtils.getVersionName(context);
        object.deviceBrand = Build.BRAND;
        object.deviceMode = Build.MODEL;
        object.deviceVersion = String.valueOf(Build.VERSION.SDK_INT);
        object.applicationId = BuildConfig.APPLICATION_ID;
        return object;
    }

    public JSONObject toJSONObject() {
        HashMap<String, String> map = new HashMap<>();
        map.put("device_id", deviceId);
        map.put("description", description);
        map.put("app_version", appVersion);
        map.put("device_brand", deviceBrand);
        map.put("device_mode", deviceMode);
        map.put("device_version", deviceVersion);
        map.put("application_id", applicationId);
        return new JSONObject(map);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "deviceId='" + deviceId + '\'' +
                ", description='" + description + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", deviceBrand='" + deviceBrand + '\'' +
                ", deviceMode='" + deviceMode + '\'' +
                ", deviceVersion='" + deviceVersion + '\'' +
                ", applicationId='" + applicationId + '\'' +
                '}';
    }
}
