package com.zhaofliu.wechathelper.apputils;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.record.Record;

/**
 * Created by liuzhaofeng on 2016/1/28.
 *
 */
public class PacketUtils {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static boolean openPacketInDetail(final AccessibilityService service){

        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();

        if (nodeInfo != null) {

            // try to find open button, english version have open button
            List<AccessibilityNodeInfo> buttonNodeInfos = nodeInfo.findAccessibilityNodeInfosByText(service.getString(R.string.key_word_button_open));

            if(buttonNodeInfos.size()>0) {
                AccessibilityNodeInfo buttonNode = buttonNodeInfos.get(0);
                buttonNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return true;
            }

            List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByText(service.getString(R.string.key_word_receive_lucky_money));

            if(nodeInfos.size()==0) {
                nodeInfos = nodeInfo.findAccessibilityNodeInfosByText(service.getString(R.string.key_word_receive_lucky_money_from_group));
            }

            if(nodeInfos.size()==0) {
                return false;
            }
            AccessibilityNodeInfo textNode = nodeInfos.get(0);

            AccessibilityNodeInfo contentNode = textNode.getParent().getParent();

            if(contentNode == null) {
                return false;
            }

            AccessibilityNodeInfo openButtonNode = contentNode.getChild(contentNode.getChildCount()-1);
            if("android.widget.Button".equals(openButtonNode.getClassName())) {
                openButtonNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            return true;
        }
        return false;
    }

    public static boolean checkLoading(AccessibilityService service){
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();

        if (nodeInfo != null) {

            List<AccessibilityNodeInfo> buttonNodeInfos = nodeInfo.findAccessibilityNodeInfosByText(service.getString(R.string.key_word_loading));
            return buttonNodeInfos.size()>0;
        }
        return false;
    }

    public static boolean checkNoLuckyMoney(AccessibilityService service){

        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo != null) {

            List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByText(service.getString(R.string.key_word_no_lucky_money));

            return nodeInfos.size()>0;
        }
        return false;
    }

    public static boolean checkLuckyMoneyOver24Hour(AccessibilityService service){

        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();

        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByText(service.getString(R.string.key_word_lucky_money_over_24_hour));
            return nodeInfos.size()>0;
        }
        return false;
    }


    public static void travelNode(AccessibilityNodeInfo nodeInfo, String sp) {

        System.out.println(sp + "1111111111 " + nodeInfo.getClassName());

        int count = nodeInfo.getChildCount();
        if(count == 0) {
            return;
        }

        for(int i=0; i<count; i++) {
            travelNode(nodeInfo.getChild(i), sp+" ");
        }
    }

    public static void travelParent(AccessibilityNodeInfo nodeInfo, String sp) {

        AccessibilityNodeInfo parent = nodeInfo.getParent();
        if(parent != null) {
            travelParent(parent, sp);
        }

        System.out.println(sp + "1111111111 " + nodeInfo.getClassName());
    }


    public static boolean isInMainScreen(AccessibilityService service) {
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        if(rootNode == null) {
            return false;
        }
        List<AccessibilityNodeInfo>  titleNodeInfos = rootNode.findAccessibilityNodeInfosByText(service.getString(R.string.key_word_wechat));
        List<AccessibilityNodeInfo>  backNodeInfos = rootNode.findAccessibilityNodeInfosByText(service.getString(R.string.key_word_back));

        return titleNodeInfos.size()>0 && backNodeInfos.size()==0;
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

    public static CharSequence getChatScreenTitle(AccessibilityService service) {
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        if(rootNode == null) {
            return null;
        }
        List<AccessibilityNodeInfo>  chatInfos = rootNode.findAccessibilityNodeInfosByText(service.getString(R.string.key_word_chat_info));
        List<AccessibilityNodeInfo>  backNodeInfos = rootNode.findAccessibilityNodeInfosByText(service.getString(R.string.key_word_back));


        if(backNodeInfos.size()>0 && chatInfos.size()>0) {
            try{
                AccessibilityNodeInfo backNodeInfo = backNodeInfos.get(0).getParent().getParent();
                AccessibilityNodeInfo titleNode = backNodeInfo.getChild(1).getChild(0);
                return titleNode.getText();
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }

    public static boolean isLastNodeInListView(AccessibilityNodeInfo fetchLuckyMoneyNode, int lastCount) {
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
        if(lastCount>count) {
            lastCount = count;
        }
        for(int i=count-1; i>count-lastCount-1; i--) {
            if(listViewNode.getChild(i).equals(listItem)) {
                return true;
            }
        }
        return false;

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

    public static Record getFetchAmountInDetail(AccessibilityService service){
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> unitNodeInfos = nodeInfo.findAccessibilityNodeInfosByText(service.getString(R.string.key_word_money_unit));
            if(unitNodeInfos.size()==0) {
                return null;
            }

            try {
                Record record = new Record();
                AccessibilityNodeInfo unitNode = unitNodeInfos.get(0);
                AccessibilityNodeInfo amountParentNode = unitNode.getParent();
                AccessibilityNodeInfo amountNode = amountParentNode.getChild(0);
                record.amount = amountNode.getText().toString();

                record.time = System.currentTimeMillis();

                AccessibilityNodeInfo senderInfoNode = amountParentNode.getParent().getParent();
                AccessibilityNodeInfo senderNode = senderInfoNode.getChild(0).getChild(2).getChild(0);
                record.sender = senderNode.getText().toString();

                AccessibilityNodeInfo wishNode = senderInfoNode.getChild(0).getChild(3);
                record.desc = wishNode.getText().toString();
                return record;
            } catch (Exception e) {
                return null;
            }

        }
        return null;
    }

}
