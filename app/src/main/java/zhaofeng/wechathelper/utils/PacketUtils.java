package zhaofeng.wechathelper.utils;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import zhaofeng.wechathelper.R;

/**
 * Created by liuzhaofeng on 2016/1/28.
 */
public class PacketUtils {
    public static boolean openPacketInDetail(AccessibilityService service){
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByText(service.getString(R.string.key_word_receive_lucky_money));
            if(nodeInfos.size()==0) {
                nodeInfos = nodeInfo.findAccessibilityNodeInfosByText(service.getString(R.string.key_word_receive_lucky_money_from_group));
            }
            if(nodeInfos.size()==0) {
                return false;
            }
            AccessibilityNodeInfo textNode = nodeInfos.get(0);
            AccessibilityNodeInfo contentNode = textNode.getParent().getParent();
            AccessibilityNodeInfo openButtonNode = contentNode.getChild(contentNode.getChildCount()-1);
            openButtonNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            return true;
        }
        return false;
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

    public static boolean clickThePacketNode(Context context, AccessibilityNodeInfo fetchLuckyMoneyNode) {
        try {
            AccessibilityNodeInfo note = fetchLuckyMoneyNode.getParent().getParent().getParent().getParent();
            List<AccessibilityNodeInfo> list = note.findAccessibilityNodeInfosByText(context.getString(R.string.key_word_wechat_lucky_money));
            // check it's a real packet instead of just a text message
            if(list.size() == 1) {
                note.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return true;
            } else {
                return false;
            }
        }catch (Exception e) {
            return false;
        }
    }
}
