package zhaofeng.wechathelper.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

/**
 * Created by liuzhaofeng on 2016/2/13.
 */
public class SharedPreUtils {
    public static void saveStringToSharedPreference(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveFloatToSharedPreference(Context context, String key, float value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static String readStringFromSharedPreference(Context context, String key) {
        SharedPreferences sharedPreference = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreference == null ? null : sharedPreference.getString(key, null);
    }

    public static float readFloatFromSharedPreference(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences == null ? 0.0f : sharedPreferences.getFloat(key, 0.0f);
    }

    public static boolean isSharedPreferenceExist(Context context, String sharedPreferenceFileName) {
        File file = new File("/data/data/" + context.getPackageName() + "/shared_prefs/" + sharedPreferenceFileName + ".xml");
        return file.exists();
    }

    public static boolean isMoneySharedPreferenceExist(Context context) {
        return isSharedPreferenceExist(context, Constants.SHARED_PREFERENCE_NAME);
    }

    public static boolean readBooleanSetting(Context context, String key, boolean defaultValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SETTING_FILE, Context.MODE_PRIVATE);
        return sharedPreferences == null? defaultValue:sharedPreferences.getBoolean(key,defaultValue);
    }
}
