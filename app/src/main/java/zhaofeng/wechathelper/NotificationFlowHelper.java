package zhaofeng.wechathelper;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
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

    public enum State{
        invalid,
        notification,
        clickedInList,
        opened,
        detail,
    }

    interface FlowListener {
        void onNotificationFlowStateChanged(State state);
    }

    private AccessibilityService mService;
    private State mState = State.invalid;
    private PowerManager.WakeLock mWakeLock = null;

    private FlowListener mFlowListener;
    private static final int WAKE_TIME_IN_SECONDS = 5;

    public NotificationFlowHelper(AccessibilityService service) {
        mService = service;
        mState = State.invalid;

        if(service instanceof FlowListener) {
            mFlowListener = (FlowListener) service;
        }
    }

    public boolean onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                if (NotificationUtils.checkNotificationContains(event, mService.getString(R.string.key_word_notification))) {
                    if(!isScreenOn()){
                        lightScreen();
                    }
                    Notification notification = NotificationUtils.getNotification(event);
                    if(notification != null) {
                        boolean success = NotificationUtils.openNotification(notification);
                        changeToState(success ? State.notification : State.invalid);
                    }
                }
                return mState == State.notification;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                CharSequence className = event.getClassName();
                if (TextUtils.equals(className, Constans.WECHAT_LAUNCHER)) {
                    if(mState == State.notification) {
                        boolean success = fetchPacketAndClick();
                        changeToState(success ? State.clickedInList : State.invalid);
                        return mState==State.clickedInList;
                    } else {
                        changeToState(State.invalid);
                        return false;
                    }
                } else if(TextUtils.equals(className, Constans.WECHAT_LUCKY_MONEY_DETAIL)) {
                    if (mState == State.opened) {
                        changeToState(State.detail);
                        mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);  //back to chat window
                        changeToState(State.invalid);
                        return true;
                    } else {
                        changeToState(State.invalid);
                        return false;
                    }
                } else if(TextUtils.equals(className, Constans.WECHAT_LUCKY_MONEY_RECEIVER)) {
                    if(mState==State.clickedInList) {
                        boolean fetchSuccess = PacketUtils.openPacketInDetail(mService);
                        if (fetchSuccess) {
                            changeToState(State.opened);
                        } else {
                            // fetch failed, lucky money dispatch finished, back to chat window
                            mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);  //back to chat window
                            changeToState(State.invalid);
                        }
                        return true;
                    }
                }
                return false;
            default:
                if(mState == State.notification) {
                    changeToState(State.invalid);
                }
                return false;
        }
    }

    private boolean isState(State state) {
        return mState == state;
    }

    private void changeToState(State state) {
        if(mState != state) {
            mState = state;
            if(mFlowListener !=null) {
                mFlowListener.onNotificationFlowStateChanged(state);
            }
        }

    }

    public boolean isTryToFetchAndClick() {
        return mState == State.clickedInList;
    }

    public State getState() {
        return mState;
    }

    private boolean fetchPacketAndClick() {
        AccessibilityNodeInfo nodeInfo = PacketUtils.getLastPacket(mService);
        return nodeInfo != null && PacketUtils.clickThePacketNode(mService, nodeInfo);
    }

    private boolean isScreenOn() {
        PowerManager powerManager = (PowerManager)mService.getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return powerManager.isInteractive();
        } else {
            return powerManager.isScreenOn();
        }
    }

    private void lightScreen(){
        acquireWakeLock();
    }

    private void acquireWakeLock(){
        PowerManager powerManager = (PowerManager)mService.getSystemService(Context.POWER_SERVICE);
        if(mWakeLock==null) {
            mWakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.FULL_WAKE_LOCK, "LuckyMoneyWakeLock");
        }
        mWakeLock.acquire(WAKE_TIME_IN_SECONDS*1000);
    }
}
