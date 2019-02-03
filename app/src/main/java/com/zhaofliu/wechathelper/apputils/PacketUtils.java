package com.zhaofliu.wechathelper.apputils;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;

import com.lskycity.support.utils.SharedPreUtils;
import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.app.HunterApplication;
import com.zhaofliu.wechathelper.record.Record;

import java.util.List;

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


            AccessibilityNodeInfo openButtonNode = findOpenButton(nodeInfo);

            if(openButtonNode != null) {
                openButtonNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return true;
            }
        }
        return false;
    }

    public static AccessibilityNodeInfo findOpenButton(AccessibilityNodeInfo note) {
        if(note == null) {
            return null;
        }

        if("android.widget.Button".equals(note.getClassName().toString())) {
            return note;
        }

        if(note.getChildCount()<=0) {
            return null;
        }

        int childCount = note.getChildCount();
        for(int i=0; i< childCount; i++) {
            AccessibilityNodeInfo info = findOpenButton(note.getChild(i));
            if(info != null) {
                return info;
            }
        }

        return null;
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



    public static AccessibilityNodeInfo getLastNoHookPacket(AccessibilityService service) {
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        if(rootNode == null) {
            return null;
        }
        List<AccessibilityNodeInfo>  nodeInfos = rootNode.findAccessibilityNodeInfosByText(service.getString(R.string.key_word_wechat_lucky_money));
        int count = nodeInfos.size();

        if(count>0) {
            for(int i=count-1; i>=0; i--) {
                AccessibilityNodeInfo info = nodeInfos.get(i);
                if(checkNoHook(service, info)) {
                    return info.getParent().getParent();
                }
            }
        }

        return null;
    }

    private static boolean checkNoHook(Context context, AccessibilityNodeInfo fetchLuckyMoneyNode) {

        AccessibilityNodeInfo parentNote = fetchLuckyMoneyNode.getParent().getParent();
        List<AccessibilityNodeInfo> list = parentNote.findAccessibilityNodeInfosByText(context.getString(R.string.key_word_opened));
        return list.size()==0;


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

    public static boolean clickNode(AccessibilityNodeInfo fetchLuckyMoneyNode) {
        try {
            fetchLuckyMoneyNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            return true;
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
                AccessibilityNodeInfo amountNode = amountParentNode.getChild(1);
                record.amount = amountNode.getText().toString();

                record.time = System.currentTimeMillis();

                AccessibilityNodeInfo senderInfoNode = amountParentNode.getParent().getParent();
                AccessibilityNodeInfo senderNode = senderInfoNode.getChild(1).getChild(0).getChild(1).getChild(0);
                record.sender = senderNode.getText().toString();

                AccessibilityNodeInfo wishNode = senderInfoNode.getChild(1).getChild(1);
                record.desc = wishNode.getText().toString();
                System.out.println("111111 "+record.toString());
                return record;
            } catch (Exception e) {
                return null;
            }

        }
        return null;
    }

    /**
     * get Random Delay Time according setting
     *
     *
     * */
    public static int getRandomDelayTime() {
        int delay = SharedPreUtils.getInt(HunterApplication.get(), Constants.KEY_RANDOM_DELAY_TIME, 0);
        return (int)(Math.random()*delay*1000);
    }

}
