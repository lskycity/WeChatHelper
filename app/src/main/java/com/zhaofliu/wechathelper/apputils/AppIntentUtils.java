package com.zhaofliu.wechathelper.apputils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.res.ResourcesCompat;

import com.zhaofliu.wechathelper.R;

/**
 * @author zhaofliu
 * @since 9/2/18.
 */
public class AppIntentUtils {
    public static void startUrlWithCustomTab(Context context, String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_arrow_back));
        builder.setToolbarColor(ResourcesCompat.getColor(context.getResources(), R.color.colorPrimary, context.getTheme()));
        builder.addDefaultShareMenuItem();
        builder.setShowTitle(true);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
