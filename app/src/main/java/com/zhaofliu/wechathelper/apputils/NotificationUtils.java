package com.zhaofliu.wechathelper.apputils;

import android.app.Notification;
import android.app.PendingIntent;
import android.os.Parcelable;
import android.view.accessibility.AccessibilityEvent;

import java.util.List;

/**
 * Created by liuzhaofeng on 2016/1/28.
 */
public class NotificationUtils {
    public static boolean checkNotificationContains(AccessibilityEvent event, CharSequence str) {
        List<CharSequence> texts = event.getText();
        if (texts.isEmpty()) {
            return false;
        }
        for(CharSequence text : texts) {
            if(text.toString().contains(str)) {
                return true;
            }
        }
        return false;
    }

    public static Notification getNotification(AccessibilityEvent event) {
        Parcelable parcelable = event.getParcelableData();
        if(parcelable instanceof Notification) {
            return (Notification) parcelable;
        }
        return null;
    }

    public static boolean openNotification(Notification notification) {
        PendingIntent pendingIntent = notification.contentIntent;
        try {
            pendingIntent.send();
            return true;
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void cancelNotification(Notification notification) {
        PendingIntent pendingIntent = notification.contentIntent;
        pendingIntent.cancel();
    }
}
