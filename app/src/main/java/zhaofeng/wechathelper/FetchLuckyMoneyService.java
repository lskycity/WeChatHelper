package zhaofeng.wechathelper;

import android.accessibilityservice.AccessibilityService;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import zhaofeng.wechathelper.record.FetchRecordDbHelper;
import zhaofeng.wechathelper.record.Record;
import zhaofeng.wechathelper.utils.Constans;
import zhaofeng.wechathelper.utils.PacketUtils;

/**
 * Created by liuzhaofeng on 1/6/16.
 */
public class FetchLuckyMoneyService extends AccessibilityService implements NotificationFlowHelper.FlowListener {


    private NotificationFlowHelper mNotificationFlowHelper;
    private FetchRecordDbHelper mFetchRecordDbHelper;
    private String mCurrentUI = "";
    private boolean isOpenByService = false;
    private static final double BIG_MONEY = 5.0f;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mNotificationFlowHelper = new NotificationFlowHelper(this);
        mFetchRecordDbHelper = new FetchRecordDbHelper(this);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (mNotificationFlowHelper.onAccessibilityEvent(event)) {
            isOpenByService = false;
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                mCurrentUI = event.getClassName().toString();
            }
            return;
        }

        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                mCurrentUI = event.getClassName().toString();
                if (TextUtils.equals(mCurrentUI, Constans.WECHAT_LUCKY_MONEY_RECEIVER)) {
                    boolean success = PacketUtils.openPacketInDetail(this);
                    if (isOpenByService && !success) {
                        backToChatWindow();
                        isOpenByService = false;
                    }
                } else if (TextUtils.equals(mCurrentUI, Constans.WECHAT_LUCKY_MONEY_DETAIL)) {
                    if (isOpenByService) {
                        saveAmountAndTime();
                        backToChatWindow();
                        isOpenByService = false;
                    }
                }
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                if (Constans.WECHAT_LAUNCHER.equals(mCurrentUI) && !mNotificationFlowHelper.isTryToFetchAndClick() && isListViewBottom(event)) {
                    checkLastMessageAndOpenLuckyMoney();
                }
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
        try {
            double receivedMoney = Double.valueOf(record.amount);
            MediaPlayer player = null;
            if (receivedMoney > BIG_MONEY) {
                player = MediaPlayer.create(this, R.raw.money_big);
            } else if (receivedMoney > 0.0f) {
                player = MediaPlayer.create(this, R.raw.money_small);
            }
            if (player != null) {
                player.start();
            }
        } catch (NumberFormatException e) {

        }
    }


    private boolean isListViewBottom(AccessibilityEvent event) {
        if (TextUtils.equals(event.getClassName(), "android.widget.ListView")) {
            return event.getToIndex() == event.getItemCount() - 1;
        }
        return false;
    }

    private void checkLastMessageAndOpenLuckyMoney() {
        AccessibilityNodeInfo packet = PacketUtils.getLastPacket(this);
        if (packet != null && PacketUtils.isLastNodeInListView(packet)) {
            isOpenByService = PacketUtils.clickThePacketNode(this, packet);
        }
    }

    private void backToChatWindow() {
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    public String getAlreadyFetchString() {
        return "你领取了" + getRemoteName() + "的红包";
    }

    public CharSequence getRemoteName() {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();

        if (rootNode != null) {
            List<AccessibilityNodeInfo> nodeInfos = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ew");
            if (nodeInfos.size() <= 0) {
                return "";
            } else {
                return nodeInfos.get(0).getText();
            }
        }
        return "";
    }

    public boolean isChatGroup(String title) {
        return title.matches("^.*(\\d+)$");
    }

    public String getChatGroupName(String title) {
        return title.substring(0, title.indexOf("("));
    }


    @Override
    public void onInterrupt() {
    }

    @Override
    public void onNotificationFlowStateChanged(NotificationFlowHelper.State state) {
        if (state == NotificationFlowHelper.State.detail) {
            saveAmountAndTime();
        } else if (state == NotificationFlowHelper.State.notification) {
        }
    }
}
