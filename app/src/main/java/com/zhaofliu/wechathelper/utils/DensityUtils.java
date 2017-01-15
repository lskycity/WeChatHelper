package com.zhaofliu.wechathelper.utils;

import android.content.Context;

/**
 * Created by liuzhaofeng on 5/30/16.
 */
public class DensityUtils {

    private DensityUtils(){}

    public static int pxTodp(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dpTopx(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, int sp){
        final float frontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * frontScale +0.5);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
