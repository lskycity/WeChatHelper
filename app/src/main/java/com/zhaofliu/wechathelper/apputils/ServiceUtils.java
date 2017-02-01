package com.zhaofliu.wechathelper.apputils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.zhaofliu.wechathelper.BuildConfig;
import com.zhaofliu.wechathelper.FetchLuckyMoneyService;

import java.util.Set;

/**
 * Created by zhaofliu on 1/6/17.
 *
 */

public class ServiceUtils {

    public static void openNotificationServiceSetting(Context context) {
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        context.startActivity(intent);
    }

    public static boolean isNotificationAccessed(Context context) {
        Set<String>  packageNames = NotificationManagerCompat.getEnabledListenerPackages(context);
        return packageNames.contains(context.getPackageName());
    }


    public static void openAccServiceSetting(Context context) {
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        context.startActivity(intent);
    }

    public static boolean isAccessibilityEnabled(Context context) {
        int accessibilityEnabled = 0;
        final String ACCESSIBILITY_SERVICE_NAME = BuildConfig.APPLICATION_ID+"/"+ FetchLuckyMoneyService.class.getName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {
        }

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(':');
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessibilityService = splitter.next();
                    if (accessibilityService.equalsIgnoreCase(ACCESSIBILITY_SERVICE_NAME)) {
                        return true;
                    }
                }
            }

        }
        return false;
    }
}
