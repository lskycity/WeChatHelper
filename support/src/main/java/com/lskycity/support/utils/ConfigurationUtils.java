/*
 * Copyright (c) 2016 Cisco. All rights reserved.
 *
 */

package com.lskycity.support.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

/**
 *
 * config related utils
 *
 * @author liuzhaofeng
 * @since 3/11/16.
 */
public class ConfigurationUtils {

    public static boolean isPortrait(Context context) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
    }

    public static boolean isLandScape(Context context) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    /**
     * using Application or Activity may return different value, since multi screen support
     * @param context using activity or an activity wrappred context
     * @return if a sw600dp size
     * */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().smallestScreenWidthDp >= 600);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getCurrentLocale(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return context.getResources().getConfiguration().getLocales().get(0);
        } else{
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }
}
