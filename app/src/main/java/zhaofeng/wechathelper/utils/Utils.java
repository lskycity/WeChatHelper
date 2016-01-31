package zhaofeng.wechathelper.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.util.Date;

/**
 * Created by kevinbest on 16/1/30.
 */
public class Utils {
    private static final String SHARED_PREFERENCE_NAME = "MONEY_STATISTICS";

    public static String getTimeString(long time) {
        return new Date(time).toLocaleString();
    }

    public static void saveStringToSharedPreference(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveFloatToSharedPreference(Context context, String key, float value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static String readStringFromSharedPreference(Context context, String key) {
        SharedPreferences sharedPreference = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreference == null ? null : sharedPreference.getString(key, null);
    }

    public static float readFloatFromSharedPreference(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences == null ? 0.0f : sharedPreferences.getFloat(key, 0.0f);
    }

    public static boolean isSharedPreferenceExist(Context context, String sharedPreferenceFileName) {
        File file = new File("/data/data/" + context.getPackageName() + "/shared_prefs/" + sharedPreferenceFileName + ".xml");
        return file.exists();
    }

    public static boolean isMoneySharedPreferenceExist(Context context) {
        return isSharedPreferenceExist(context, SHARED_PREFERENCE_NAME);
    }
}
