package com.zhaofliu.wechathelper.utils;

import android.view.View;

/**
 * Created by zhaofliu on 1/5/17.
 */

public class ViewUtils {

    public static void setVisible(View view, Boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public static boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

}
