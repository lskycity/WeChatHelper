package com.zhaofliu.wechathelper.apputils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

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
        String notificationListenerString = Settings.Secure.getString(context.getContentResolver(),"enabled_notification_listeners");
        return Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2 && notificationListenerString != null && notificationListenerString.contains(context.getPackageName());
    }


    public static void openAccServiceSetting(Context context) {
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        context.startActivity(intent);
    }

    public static boolean isAccessibilityEnabled(Context context) {
        int accessibilityEnabled = 0;
        final String ACCESSIBILITY_SERVICE_NAME = "com.zhaofliu.wechathelper/com.zhaofliu.wechathelper.FetchLuckyMoneyService";
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
