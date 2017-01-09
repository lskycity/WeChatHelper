package com.zhaofliu.wechathelper;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.zhaofliu.wechathelper.apputils.Constants;
import com.zhaofliu.wechathelper.apputils.NotificationUtils;
import com.zhaofliu.wechathelper.apputils.PacketUtils;
import com.zhaofliu.wechathelper.ui.OpenAccessibilityActivity;
import com.zhaofliu.wechathelper.utils.AppUtils;

import java.lang.ref.WeakReference;

/**
 * Created by liuzhaofeng on 2016/1/28.
 *
 */
public class NotificationFlowHelper {

    private static WeakReference<NotificationFlowHelper> instance;

    private static void setInstance(NotificationFlowHelper ins) {
        instance = new WeakReference<NotificationFlowHelper>(ins);
    }

    private static NotificationFlowHelper getInstance() {
        return instance != null ? instance.get():null;
    }

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
    /*package*/ boolean serviceState = false;

    private FlowListener mFlowListener;
    private static final int WAKE_TIME_IN_SECONDS = 5;

    public NotificationFlowHelper(AccessibilityService service) {
        mService = service;
        mState = State.invalid;

        if(service instanceof FlowListener) {
            mFlowListener = (FlowListener) service;
        }
        setInstance(this);
    }

    private Handler fetchPacketAndClickHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            boolean success = fetchPacketAndClick();
            changeToState(success ? State.clickedInList : State.invalid);
        }
    };

    private Handler tryUnlockSwipeScreenHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //after 1000 ms, the state still is notification, so may be have swipe screen
            if(mState == State.notification && !AppUtils.isDeviceProtected(mService)) {
                OpenAccessibilityActivity.startForUnlockScreen(mService);
            }

        }
    };


    public static void onNotificationPostedFromNotificationListener(Notification notification) {
        NotificationFlowHelper instance = getInstance();
        if(instance != null && instance.serviceState) {
            instance.onNotificationPosted(notification);
        }
    }

    private String lastWord = "";
    private long lastOpenTime = 0;

    private void onNotificationPosted(Notification notification) {
        if(notification==null) {
            return;
        }

        CharSequence tickerText = notification.tickerText;

        if(tickerText==null) {
            return;
        }

        if(tickerText.toString().contains(mService.getString(R.string.key_word_notification))) {
            if(!TextUtils.equals(lastWord, tickerText) || (System.currentTimeMillis()-lastOpenTime)>1000) {
                boolean success = NotificationUtils.openNotification(notification);
                changeToState(success ? State.notification : State.invalid);

                if(!isScreenOn()){
                    lightScreen();
                }

                if(success) {
                    tryUnlockSwipeScreenHandler.sendEmptyMessageDelayed(1, 1000);
                }
            }
        }

        lastWord = tickerText.toString();
        lastOpenTime = System.currentTimeMillis();

    }

    public boolean onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                Notification notification = NotificationUtils.getNotification(event);
                if(notification != null) {
                    onNotificationPosted(notification);
                }
                return mState == State.notification;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                tryUnlockSwipeScreenHandler.removeMessages(1);
                CharSequence className = event.getClassName();
                if (TextUtils.equals(className, Constants.WECHAT_LAUNCHER)) {
                    if(mState == State.notification) {
                        boolean success = fetchPacketAndClick();
                        changeToState(success ? State.clickedInList : State.invalid);
                        if(!success) {
                            fetchPacketAndClickHandler.sendEmptyMessageDelayed(1, 100);
                        }
                        return mState==State.clickedInList;
                    } else {
                        changeToState(State.invalid);
                        return false;
                    }
                } else if(TextUtils.equals(className, Constants.WECHAT_LUCKY_MONEY_DETAIL)) {
                    if (mState == State.opened) {
                        changeToState(State.detail);
                        mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);  //back to chat window
                        changeToState(State.invalid);
                        return true;
                    } else {
                        changeToState(State.invalid);
                        return false;
                    }
                } else if(TextUtils.equals(className, Constants.WECHAT_LUCKY_MONEY_RECEIVER)) {
                    if(mState==State.clickedInList) {
                        boolean fetchSuccess = PacketUtils.openPacketInDetail(mService);
                        if (fetchSuccess) {
                            changeToState(State.opened);
                        } else {
                            // fetch failed, lucky money dispatch finished, back to chat window
                            changeToState(State.invalid);
                        }
                        return fetchSuccess;
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
        PowerManager powerManager = (PowerManager) mService.getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH)
        {
            return powerManager.isInteractive();
        }
        else
        {
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
