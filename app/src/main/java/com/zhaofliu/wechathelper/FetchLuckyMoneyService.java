package com.zhaofliu.wechathelper;

import android.accessibilityservice.AccessibilityService;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.zhaofliu.wechathelper.NotificationFlowHelper;
import com.zhaofliu.wechathelper.record.FetchRecordDbHelper;
import com.zhaofliu.wechathelper.record.Record;
import com.zhaofliu.wechathelper.apputils.Constants;
import com.zhaofliu.wechathelper.apputils.PacketUtils;
import com.zhaofliu.wechathelper.utils.SharedPreUtils;

import zhaofeng.wechathelper.BuildConfig;

/**
 * Created by liuzhaofeng on 1/6/16.
 */
public class FetchLuckyMoneyService extends AccessibilityService implements NotificationFlowHelper.FlowListener
{

    private NotificationFlowHelper mNotificationFlowHelper;

    private FetchRecordDbHelper mFetchRecordDbHelper;

    private String mCurrentUI = "";

    private boolean isOpenByService = false;

    private int mListCount = 0;

    private static final float BIG_MONEY = 5.0f;

    @Override
    protected void onServiceConnected()
    {
        super.onServiceConnected();
        mNotificationFlowHelper = new NotificationFlowHelper(this);
        mFetchRecordDbHelper = new FetchRecordDbHelper(this);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event)
    {
        if(BuildConfig.DEBUG) {
            Log.v("onAccessibility", "onAccessibilityEvent event type=0x"+Integer.toHexString(event.getEventType())+", class="+event.getClassName());
        }

        if (mNotificationFlowHelper.onAccessibilityEvent(event))
        {
            isOpenByService = false;
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
            {
                mCurrentUI = event.getClassName().toString();
            }
            return;
        }

        int eventType = event.getEventType();
        switch (eventType)
        {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                mCurrentUI = event.getClassName().toString();
                if (TextUtils.equals(mCurrentUI, Constants.WECHAT_LUCKY_MONEY_RECEIVER))
                {
                    boolean success = PacketUtils.openPacketInDetail(this);
                    if (isOpenByService && !success)
                    {
                        backToChatWindow();
                        isOpenByService = false;
                    }
                }
                else if (TextUtils.equals(mCurrentUI, Constants.WECHAT_LUCKY_MONEY_DETAIL))
                {
                    if (isOpenByService)
                    {
                        saveAmountAndTime();
                        backToChatWindow();
                        isOpenByService = false;
                    }
                }
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                if (Constants.WECHAT_LAUNCHER.equals(mCurrentUI) && isListViewScroll(event))
                {
                    int count = event.getItemCount();
                    int addCount = count - mListCount;
                    int listDisplayCount = event.getToIndex()-event.getFromIndex();
                    int diff = Math.min(Math.max(1, addCount), listDisplayCount);
                    if (!mNotificationFlowHelper.isTryToFetchAndClick() && (event.getToIndex() == count - 1)) {
                        checkLastMessageAndOpenLuckyMoney(diff);
                    }
                    mListCount = count;
                }
                break;
        }
    }

    private void saveAmountAndTime()
    {
        Record record = PacketUtils.getFetchAmountInDetail(this);
        if (record != null)
        {
            playMoneySound(record);
            updateTotalMoneyToSP(record.amount);
            mFetchRecordDbHelper.insert(record);
        }
    }

    private float getTotalMoneyFromSP()
    {
        return SharedPreUtils.getFloat(this, Constants.TOTAL_MONEY_KEY);
    }

    private boolean initSharedPreferenceMoneyData()
    {
        Cursor cursor = mFetchRecordDbHelper.query();
        float total = 0.0f;
        while (cursor.moveToNext())
        {
            String amount = cursor.getString(1);
            try
            {
                float fAmount = Float.valueOf(amount);
                total += fAmount;

            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }
        SharedPreUtils.putFloat(this, Constants.TOTAL_MONEY_KEY, total);
        return true;
    }

    private boolean updateTotalMoneyToSP(String amount)
    {
        // sharedpreference file does not exist, query database to create a new one
        if (SharedPreUtils.getFloat(this, Constants.TOTAL_MONEY_KEY, -1)<0)
        {
            return initSharedPreferenceMoneyData();
        }
        else
        {
            String update_money = amount;
            try
            {
                float money = Float.valueOf(update_money);
                float current_total_money = money + getTotalMoneyFromSP();
                saveTotalMoneyToSP(current_total_money);
                return true;

            }
            catch (NumberFormatException ex)
            {
                return false;
            }
        }

    }

    private void saveTotalMoneyToSP(float value)
    {
        SharedPreUtils.putFloat(this, Constants.TOTAL_MONEY_KEY, value);
    }

    private void playMoneySound(Record record)
    {
        if (isMoneySoundSwitchOn())
        {
            try
            {
                float receivedMoney = Float.parseFloat(record.amount);
                MediaPlayer player = null;
                if (Float.compare(receivedMoney, BIG_MONEY) >= 0)
                {
                    player = MediaPlayer.create(this, zhaofeng.wechathelper.R.raw.money_big);
                }
                else if (Float.compare(receivedMoney, 0f) > 0)
                {
                    player = MediaPlayer.create(this, zhaofeng.wechathelper.R.raw.money_small);
                }
                if (player != null)
                {
                    player.start();
                }
            }
            catch (NumberFormatException e)
            {
            }
        }
    }

    private boolean isListViewScroll(AccessibilityEvent event)
    {
        return TextUtils.equals(event.getClassName(), "android.widget.ListView");
    }

    private void checkLastMessageAndOpenLuckyMoney(int lastCount)
    {
        AccessibilityNodeInfo packet = PacketUtils.getLastPacket(this);
        if (packet != null && PacketUtils.isLastNodeInListView(packet, lastCount))
        {
            isOpenByService = PacketUtils.clickThePacketNode(this, packet);
        }
    }

    private void backToChatWindow()
    {
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    @Override
    public void onInterrupt()
    {
    }

    @Override
    public void onNotificationFlowStateChanged(NotificationFlowHelper.State state)
    {
        if (state == NotificationFlowHelper.State.detail)
        {
            saveAmountAndTime();
        }
        else if (state == NotificationFlowHelper.State.notification)
        {
        }
    }

    private boolean isMoneySoundSwitchOn()
    {
        return SharedPreUtils.getBoolean(this, Constants.SOUND_SWITCH_KEY, true);
    }

}
