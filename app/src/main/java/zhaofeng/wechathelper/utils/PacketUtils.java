package zhaofeng.wechathelper.utils;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ListView;

import java.util.List;

import zhaofeng.wechathelper.R;

/**
 * Created by liuzhaofeng on 2016/1/28.
 */
public class PacketUtils {
    public static void openPacketInDetail(AccessibilityService service){
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByText(service.getString(R.string.key_word_receive_lucky_money));
            if(nodeInfos.size()<=0) {
                return;
            }
            AccessibilityNodeInfo textNode = nodeInfos.get(0);
            AccessibilityNodeInfo contentNode = textNode.getParent().getParent();
            AccessibilityNodeInfo openButtonNode = contentNode.getChild(contentNode.getChildCount()-1);
            openButtonNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    public static AccessibilityNodeInfo getLastPacket(AccessibilityService service) {
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        if(rootNode == null) {
            return null;
        }
        List<AccessibilityNodeInfo>  nodeInfos = rootNode.findAccessibilityNodeInfosByText(service.getString(R.string.key_word_fetch_lucky_money));
        int count = nodeInfos.size();
        if(count>0) {
            return nodeInfos.get(count-1);
        }
        return null;
    }

    public static boolean isLastNodeInListView(AccessibilityNodeInfo fetchLuckyMoneyNode) {
        AccessibilityNodeInfo listItem = fetchLuckyMoneyNode;
        try {
            AccessibilityNodeInfo v;
            while (!(v = listItem.getParent()).getClassName().equals("android.widget.ListView")) {
                listItem = v;
            }
        } catch (Exception e) {
            // We made it up to the window without find this list view
            return false;
        }

        AccessibilityNodeInfo listViewNode = listItem.getParent();
        int count = listViewNode.getChildCount();
        return listViewNode.getChild(count-1).equals(listItem);

    }

    public static boolean clickThePacketNode(AccessibilityNodeInfo fetchLuckyMoneyNode) {
        try {
            fetchLuckyMoneyNode.getParent().getParent().getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
