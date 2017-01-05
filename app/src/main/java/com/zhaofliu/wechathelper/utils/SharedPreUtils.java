package com.zhaofliu.wechathelper.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by liuzhaofeng on 2016/2/13.
 */
public class SharedPreUtils {

    public static final String KEY_LAST_DATE_CHECK_VERSION = "key_last_date_check_version";
    public static final String KEY_LAST_DATE_CHECK_VERSION_CODE = "key_last_date_check_version_code";
    public static final String KEY_LAST_DATE_CHECK_VERSION_NAME = "key_last_date_check_version_name";
    public static final String KEY_LAST_DATE_CHECK_VERSION_URL = "key_last_date_check_version_url";
    public static final String KEY_LATEST_APP_VERSION_CODE = "key_latest_app_version_code";

    public static final String SHARED_PREFERENCE_NAME = "shard_preference_name";

    public static void putString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void putFloat(Context context, String key, float value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void putLong(Context context, String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void putStringSet(Context context, String key, Set<String> value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sharedPreference = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreference == null ? defValue : sharedPreference.getString(key, defValue);
    }

    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences == null ? defaultValue : sharedPreferences.getFloat(key, defaultValue);
    }

    public static float getFloat(Context context, String key) {
        return getFloat(context, key, 0f);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences == null ? defaultValue : sharedPreferences.getInt(key, defaultValue);
    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, 0);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences == null ? defaultValue : sharedPreferences.getLong(key, defaultValue);
    }

    public static long getLong(Context context, String key) {
        return getLong(context, key, 0);
    }

    public static Set<String> getStringSet(Context context, String key, Set<String> defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences == null ? defaultValue : sharedPreferences.getStringSet(key, defaultValue);
    }

    public static Set<String> getStringSet(Context context, String key) {
        return getStringSet(context, key, null);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences == null? defaultValue:sharedPreferences.getBoolean(key, defaultValue);
    }

    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

}
