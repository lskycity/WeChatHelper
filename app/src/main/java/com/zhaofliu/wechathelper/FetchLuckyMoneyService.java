package com.zhaofliu.wechathelper;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.lskycity.support.utils.SharedPreUtils;
import com.zhaofliu.wechathelper.apputils.Constants;
import com.zhaofliu.wechathelper.apputils.PacketUtils;
import com.zhaofliu.wechathelper.record.FetchRecordDbHelper;
import com.zhaofliu.wechathelper.record.Record;
import com.zhaofliu.wechathelper.ui.ServiceForegroundSettingActivity;


/**
 * this is fetch money service, help to monitor the screen and click lucky money for you.
 *
 * @author zhaofliu
 * @since 1/6/16
 */
public class FetchLuckyMoneyService extends AccessibilityService implements NotificationFlowHelper.FlowListener {

    private NotificationFlowHelper mNotificationFlowHelper;

    private FetchRecordDbHelper mFetchRecordDbHelper;

//    private ForegroundReceiver foregroundReceiver;

    private String mCurrentUI = "";

    private boolean isOpenByService = false;

    private boolean isOpenedSuccess = false;

    private int mListCount = 0;

    private boolean init = false;

    private static final float BIG_MONEY = 5.0f;

    private long timeForClickedInNoChatScreen = 0;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        if(init && mNotificationFlowHelper != null) {
            mNotificationFlowHelper.serviceState = true;
            return;
        }

        mNotificationFlowHelper = new NotificationFlowHelper(this);
        mFetchRecordDbHelper = new FetchRecordDbHelper(this);
        setService();
        init = true;
        mNotificationFlowHelper.serviceState = true;

        if(SharedPreUtils.getBoolean(this, Constants.SHARED_KEY_SERVICE_FOREGROUND, false)) {
            showForegroundNotification();
        }

//        IntentFilter filter = new IntentFilter(Constants.KEY_SERVICE_FOREGROUND_STATE_CHANGED);
//        registerReceiver(foregroundReceiver = new ForegroundReceiver(), filter);
    }

    //start foreground notification.
    private void showForegroundNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.app_logo);
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(getString(R.string.foreground_notification_description));

        final Intent notificationIntent = new Intent(this, ServiceForegroundSettingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);

        startForeground(0x11, builder.build());
    }

    private void dismissForegroundNotification() {
        stopForeground(true);
    }

    private Handler openHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            boolean success = PacketUtils.openPacketInDetail(FetchLuckyMoneyService.this);
            isOpenedSuccess = success;
            if(!success && TextUtils.equals(mCurrentUI, Constants.WECHAT_LUCKY_MONEY_RECEIVER)
                    && !PacketUtils.checkLuckyMoneyOver24Hour(FetchLuckyMoneyService.this)
                    && !PacketUtils.checkNoLuckyMoney(FetchLuckyMoneyService.this)
                    ) {
                openHandler.sendEmptyMessageDelayed(1, 50);
            } else if(isOpenByService &&
                    (PacketUtils.checkLuckyMoneyOver24Hour(FetchLuckyMoneyService.this)
                            || PacketUtils.checkNoLuckyMoney(FetchLuckyMoneyService.this))){
                isOpenByService = false;
                backToChatWindow();
            }

        }
    };

    private Handler defineChatScreen = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(PacketUtils.isInMainScreen(FetchLuckyMoneyService.this)) {
                mListCount = 0;
            }
        }
    };

    private void setService() {

        AccessibilityServiceInfo info = getServiceInfo();
        if(info == null) {
            info = new AccessibilityServiceInfo();
        }
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType |= AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.flags |= AccessibilityServiceInfo.DEFAULT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            info.flags |= AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
            info.flags |= AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
            info.flags |= AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
            info.flags |= AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            info.flags |= AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
        }
        info.notificationTimeout = 100;

        setServiceInfo(info);

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event)  {
        if(BuildConfig.DEBUG) {
            Log.v("onAccessibility", "onAccessibilityEvent event type=0x"+Integer.toHexString(event.getEventType())+", class="+event.getClassName());
        }

        if (mNotificationFlowHelper.onAccessibilityEvent(event)) {
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                mCurrentUI = event.getClassName().toString();
            }
            return;
        }

        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                mCurrentUI = event.getClassName().toString();
                if (TextUtils.equals(mCurrentUI, Constants.WECHAT_LUCKY_MONEY_RECEIVER)) {

                    int delay = PacketUtils.getRandomDelayTime();

                    if(delay == 0) {

                        boolean success = PacketUtils.openPacketInDetail(this);
                        isOpenedSuccess = success;
                        if(!success) {
                            openHandler.sendEmptyMessageDelayed(1, 50);
                        }
                    } else {
                        Toast.makeText(this, "Random delay " + delay+"ms", Toast.LENGTH_LONG).show();
                        openHandler.sendEmptyMessageDelayed(1, 50 + delay);

                    }
                }
                else if (TextUtils.equals(mCurrentUI, Constants.WECHAT_LUCKY_MONEY_DETAIL)) {
                    if (isOpenByService) {
                        if(isOpenedSuccess) {
                            saveAmountAndTime();
                        }
                        backToChatWindow();
                        isOpenByService = false;
                    }
                } else if(!TextUtils.equals(mCurrentUI, Constants.WECHAT_FETCH_LUCKY_MONEY_TRANSITION)) {
                    isOpenByService = false;
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                if(Constants.WECHAT_LAUNCHER.equals(mCurrentUI)) {
                    defineChatScreen.removeMessages(1);
                    defineChatScreen.sendEmptyMessageDelayed(1, 500);

                }
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                if (Constants.WECHAT_LAUNCHER.equals(mCurrentUI) && isListViewScroll(event)) {
                    int count = event.getItemCount();
                    int addCount = count - mListCount;
                    int listDisplayCount = event.getToIndex()-event.getFromIndex();
                    int diff = Math.min(Math.max(1, addCount), listDisplayCount);
                    long currentTime = System.currentTimeMillis();
                    if (!mNotificationFlowHelper.isTryToFetchAndClick() && (event.getToIndex() == count - 1) && (currentTime-timeForClickedInNoChatScreen)>500) {
                        isOpenByService = checkLastMessageAndOpenLuckyMoney(diff);
                    }
                    mListCount = count;
                }
                break;
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                if(PacketUtils.getChatScreenTitle(FetchLuckyMoneyService.this) == null) {
                    timeForClickedInNoChatScreen = System.currentTimeMillis();
                }
            default:
                break;
        }
    }

    private void saveAmountAndTime() {
        Record record = PacketUtils.getFetchAmountInDetail(this);
        if (record != null) {
            playMoneySound(record);
            mFetchRecordDbHelper.insert(record);
        }
    }

    private void playMoneySound(Record record) {
        if (isMoneySoundSwitchOn()) {
            try {
                float receivedMoney = Float.parseFloat(record.amount);
                MediaPlayer player = null;
                if (Float.compare(receivedMoney, BIG_MONEY) >= 0) {
                    player = MediaPlayer.create(this, R.raw.money_big);
                } else if (Float.compare(receivedMoney, 0f) > 0) {
                    player = MediaPlayer.create(this, R.raw.money_small);
                }
                if (player != null) {
                    player.start();
                }
            } catch (NumberFormatException e) {
            }
        }
    }

    private boolean isListViewScroll(AccessibilityEvent event) {
        return TextUtils.equals(event.getClassName(), "android.widget.ListView");
    }

    private boolean checkLastMessageAndOpenLuckyMoney(int lastCount) {
        AccessibilityNodeInfo packet = PacketUtils.getLastPacket(this);
        if (packet != null && PacketUtils.isLastNodeInListView(packet, lastCount)) {
            return PacketUtils.clickThePacketNode(this, packet);
        }
        return false;
    }

    private void backToChatWindow() {
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    @Override
    public void onInterrupt() {
        init = false;
        mNotificationFlowHelper.serviceState = false;
//        unregisterReceiver(foregroundReceiver);
    }

    @Override
    public void onNotificationFlowStateChanged(NotificationFlowHelper.State state) {
        if (state == NotificationFlowHelper.State.detail) {
            saveAmountAndTime();
            isOpenByService = false;
        } else if (state == NotificationFlowHelper.State.clickedInList) {
            isOpenByService = true;
        } else if (state == NotificationFlowHelper.State.notification) {
        }
    }

    private boolean isMoneySoundSwitchOn() {
        return SharedPreUtils.getBoolean(this, Constants.SOUND_SWITCH_KEY, true);
    }

    private class ForegroundReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean foreground = intent.getBooleanExtra(Constants.KEY_SERVICE_FOREGROUND, false);
            if(foreground) {
                showForegroundNotification();
            } else {
                dismissForegroundNotification();
            }

        }
    }
}
