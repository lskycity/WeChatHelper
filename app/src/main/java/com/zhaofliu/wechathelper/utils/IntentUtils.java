package com.zhaofliu.wechathelper.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.zhaofliu.wechathelper.apputils.Constants;

import java.net.URISyntaxException;

/**
 * Created by zhaofliu on 1/26/17.
 */

public class IntentUtils {

    public static boolean startUrl(Context context, String url) {
        if(TextUtils.isEmpty(url)) {
           return false;
        }
        try {
            Intent i = Intent.parseUri(url, 0);
            context.startActivity(i);
            return true;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }
}
