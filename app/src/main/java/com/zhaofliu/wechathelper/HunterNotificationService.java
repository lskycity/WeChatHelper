package com.zhaofliu.wechathelper;

import android.annotation.TargetApi;
import android.app.Notification;
import android.os.Build;
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

    private static final String TAG = HunterNotificationService.class.getSimpleName();


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

//    static void ensureCollectorRunning(Context context) {
//        ComponentName collectorComponent = new ComponentName(context, /*NotificationListenerService Inheritance*/ HunterNotificationService.class);
//        Log.v(TAG, "ensureCollectorRunning collectorComponent: " + collectorComponent);
//        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        boolean serviceRunning = false;
//        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
//        if (runningServices == null ) {
//            Log.w(TAG, "ensureCollectorRunning() runningServices is NULL");
//            return;
//        }
//        for (ActivityManager.RunningServiceInfo service : runningServices) {
//            if (service.service.equals(collectorComponent)) {
//                Log.w(TAG, "ensureCollectorRunning service - pid: " + service.pid + ", currentPID: " + Process.myPid() + ", clientPackage: " + service.clientPackage + ", clientCount: " + service.clientCount
//                        + ", clientLabel: " + ((service.clientLabel == 0) ? "0" : "(" + context.getString(service.clientLabel) + ")"));
//                if (service.pid == Process.myPid() /*&& service.clientCount > 0 && !TextUtils.isEmpty(service.clientPackage)*/) {
//                    serviceRunning = true;
//                }
//            }
//        }
//        if (serviceRunning) {
//            Log.d(TAG, "ensureCollectorRunning: collector is running");
//            return;
//        }
//        Log.d(TAG, "ensureCollectorRunning: collector not running, reviving...");
//        toggleNotificationListenerService(context);
//    }
//
//    private static void toggleNotificationListenerService(Context context) {
//        Log.d(TAG, "toggleNotificationListenerService() called");
//        ComponentName thisComponent = new ComponentName(context, /*getClass()*/ HunterNotificationService.class);
//        PackageManager pm = context.getPackageManager();
//        pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//        pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//
//    }
}
