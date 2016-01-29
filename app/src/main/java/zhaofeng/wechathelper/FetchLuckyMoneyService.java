package zhaofeng.wechathelper;

import android.accessibilityservice.AccessibilityService;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import zhaofeng.wechathelper.utils.Constans;
import zhaofeng.wechathelper.utils.PacketUtils;

/**
 * Created by liuzhaofeng on 1/6/16.
 */
public class FetchLuckyMoneyService extends AccessibilityService {


    NotificationFlowHelper mNotificationFlowHelper;
    private String mCurrentUI = "";
    private boolean isOpenByService = false;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mNotificationFlowHelper = new NotificationFlowHelper(this);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if(mNotificationFlowHelper.onAccessibilityEvent(event)) {
            isOpenByService = false;
            if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
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
                if(TextUtils.equals(mCurrentUI, Constans.WECHAT_LUCKY_MONEY_RECEIVER)) {
                    boolean success = PacketUtils.openPacketInDetail(this);
                    if(isOpenByService && !success) {
                        backToChatWindow();
                        isOpenByService = false;
                    }
                } else if(TextUtils.equals(mCurrentUI, Constans.WECHAT_LUCKY_MONEY_DETAIL)) {
                    if (isOpenByService) {
                        backToChatWindow();
                        isOpenByService = false;
                    }
                }
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                if(Constans.WECHAT_LAUNCHER.equals(mCurrentUI) && !mNotificationFlowHelper.isTryToFetchAndClick() && isListViewBottom(event)) {
                    checkLastMessageAndOpenLuckyMoney();
                }
                break;
        }
    }

    private boolean isListViewBottom(AccessibilityEvent event) {
        if(TextUtils.equals(event.getClassName(), "android.widget.ListView")) {
            return event.getToIndex() == event.getItemCount()-1;
        }
        return false;
    }

    private void checkLastMessageAndOpenLuckyMoney() {
        AccessibilityNodeInfo packet = PacketUtils.getLastPacket(this);
        if(packet!=null && PacketUtils.isLastNodeInListView(packet)) {
            isOpenByService = PacketUtils.clickThePacketNode(this, packet);
        }
    }

    private void backToChatWindow() {
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    public String getAlreadyFetchString() {
        return "你领取了"+getRemoteName()+"的红包";
    }

    public CharSequence getRemoteName() {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();

        if (rootNode != null) {
            List<AccessibilityNodeInfo> nodeInfos = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ew");
            if(nodeInfos.size()<=0) {
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
}
