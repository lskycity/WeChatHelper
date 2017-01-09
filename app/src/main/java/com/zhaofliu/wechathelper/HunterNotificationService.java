package com.zhaofliu.wechathelper;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Service;
import android.os.Build;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import com.zhaofliu.wechathelper.apputils.Constants;

/**
 * Created by zhaofliu on 1/8/17.
 *
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class HunterNotificationService extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        if(TextUtils.equals(sbn.getPackageName(), Constants.WECHAT_PACKAGENAME)) {
            Notification notification = sbn.getNotification();
            NotificationFlowHelper.onNotificationPostedFromNotificationListener(notification);
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
