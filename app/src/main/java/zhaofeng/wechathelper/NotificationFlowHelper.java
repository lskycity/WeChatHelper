package zhaofeng.wechathelper;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import zhaofeng.wechathelper.utils.Constans;
import zhaofeng.wechathelper.utils.NotificationUtils;
import zhaofeng.wechathelper.utils.PacketUtils;

/**
 * Created by liuzhaofeng on 2016/1/28.
 */
public class NotificationFlowHelper {

    private AccessibilityService mService;
    private int mState;
    public NotificationFlowHelper(AccessibilityService service) {
        mService = service;
        mState = 0;
    }

    public boolean onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                if (NotificationUtils.checkNotificationContains(event, mService.getString(R.string.key_word_notification))) {
                    Notification notification = NotificationUtils.getNotification(event);
                    if(notification != null) {
                        mState = NotificationUtils.openNotification(notification)?1:0;
                    }
                }
                return mState == 1;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                CharSequence className = event.getClassName();
                if (TextUtils.equals(className, Constans.WECHAT_LAUNCHER)) {
                    if(mState == 1) {
                        mState = fetchPacketAndClick() ? 2 : 0 ;
                        return mState==2;
                    } else if(mState ==3) {
                        mState = 0;
                        mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);  //back to chat list
                        return true;
                    } else {
                        mState = 0;
                        return false;
                    }
                } else if(TextUtils.equals(className, Constans.WECHAT_LUCKY_MONEY_DETAIL)) {
                    if (mState == 2) {
                        mState = 3;
                        mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);  //back to chat window
                        return true;
                    } else {
                        mState = 0;
                        return false;
                    }
                } else if(TextUtils.equals(className, Constans.WECHAT_LUCKY_MONEY_RECEIVER)) {
                    if(mState == 2) {
                        PacketUtils.openPacketInDetail(mService);
                        return true;
                    }
                }
                return false;
            default:
                if(mState == 1) {
                    mState = 0;
                }
                return false;
        }
    }

    public boolean isTryToFetchAndClick() {
        return mState == 2;
    }

    public int getState() {
        return mState;
    }

    private boolean fetchPacketAndClick() {
        AccessibilityNodeInfo nodeInfo = PacketUtils.getLastPacket(mService);
        return nodeInfo != null && PacketUtils.clickThePacketNode(nodeInfo);
    }
}
