package com.zhaofliu.wechathelper.apputils;

import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Map;

/**
 * Created by liuzhaofeng on 2016/1/30.
 */
public class AccNodeUtils {
    public void searchNodeByDescription(AccessibilityNodeInfo info, Map<String,AccessibilityNodeInfo> map, String... matchTexts) {
        if (info != null) {
            if (info.getChildCount() == 0) {
                CharSequence desc = info.getContentDescription();
                if(desc != null) {
                    for(String text : matchTexts) {
                        if(TextUtils.equals(desc, text)) {
                            map.put(text, info);
                        }
                    }
                }
            } else {
                int size = info.getChildCount();
                for (int i = 0; i < size; i++) {
                    AccessibilityNodeInfo childInfo = info.getChild(i);
                    if (childInfo != null) {
                        searchNodeByDescription(childInfo, map, matchTexts);
                    }
                }
            }
        }
    }
}
