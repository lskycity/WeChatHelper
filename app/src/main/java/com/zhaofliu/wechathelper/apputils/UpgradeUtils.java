package com.zhaofliu.wechathelper.apputils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.zhaofliu.wechathelper.BuildConfig;
import com.zhaofliu.wechathelper.utils.SharedPreUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhaofliu on 1/3/17.
 */

public class UpgradeUtils {
    private static final String APP_ORIGINAL_PACKAGE_NAME = "com.zhaofliu.wechathelper";
    private static final String APP_WANDOUJIA_PACKAGE_NAME = "zhaofeng.wechathelper";

//    public static VersionInfo getJSONObjectFromURL() throws IOException {
//        HttpURLConnection urlConnection = null;
//
//        URL url = new URL(Constants.CHECK_VERSION_URL);
//
//        urlConnection = (HttpURLConnection) url.openConnection();
//
//        urlConnection.setRequestMethod("GET");
//        urlConnection.setReadTimeout(10000 /* milliseconds */);
//        urlConnection.setConnectTimeout(15000 /* milliseconds */);
//
//        urlConnection.setDoOutput(true);
//
//        urlConnection.connect();
//
//        return readJsonStream(url.openStream());
//    }
//
//    private static VersionInfo readJsonStream(InputStream in) throws IOException {
//        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
//        try {
//            return readMessage(reader);
//        } finally {
//            reader.close();
//        }
//    }
//
//    private static VersionInfo readMessage(JsonReader reader) throws IOException {
//        VersionInfo info = new VersionInfo();
//        reader.beginObject();
//        while (reader.hasNext()) {
//            String name = reader.nextName();
//            if (name.equals("package_name")) {
//                info.packageName = reader.nextString();
//            } else if (name.equals("version_code")) {
//                info.versionCode = reader.nextInt();
//            } else if (name.equals("version_name")) {
//                info.versionName = reader.nextString();
//            } else if(name.equals(BuildConfig.KEY_DOWNLOAD_URL)){
//                info.downloadUrl = reader.nextString();
//            } else {
//                reader.nextString();
//            }
//        }
//        reader.endObject();
//
//        return info;
//    }

    public static VersionInfo getVersionInfo(JSONObject jsonObject) throws JSONException {
        VersionInfo info = new VersionInfo();
        info.packageName = jsonObject.getString("package_name");
        info.versionCode = jsonObject.getInt("version_code");
        info.versionName = jsonObject.getString("version_name");
        info.downloadUrl = jsonObject.getString(BuildConfig.KEY_DOWNLOAD_URL);
        info.setCurrentTimeToCheckTime();
        return info;
    }

    public static VersionInfo getVersionInfoFromSharedPreference(Context context) {
        VersionInfo info = new VersionInfo();
        info.packageName = BuildConfig.APPLICATION_ID;
        info.versionCode = SharedPreUtils.getInt(context, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_CODE);
        info.versionName = SharedPreUtils.getString(context, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_NAME);
        info.downloadUrl = SharedPreUtils.getString(context, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_URL);
        info.checkTime = SharedPreUtils.getString(context, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION);
        return info;
    }

    public static boolean isWandoujiaVersion(Context context) {
        return TextUtils.equals(BuildConfig.APPLICATION_ID, APP_WANDOUJIA_PACKAGE_NAME);
    }

    public static boolean isInstalledWandoujiaVersion(Context context) {
        try {
            return (null != context.getPackageManager().getLaunchIntentForPackage(APP_WANDOUJIA_PACKAGE_NAME));
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isInstalledOriginalVersion(Context context) {
        try {
            return (null != context.getPackageManager().getLaunchIntentForPackage(APP_ORIGINAL_PACKAGE_NAME));
        } catch (Exception exception) {
            return false;
        }
    }
    public static void uninstallPrePackage(Context context) {
        Uri packageURI = Uri.parse("package:" + APP_WANDOUJIA_PACKAGE_NAME);
        Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageURI);
        context.startActivity(uninstallIntent);
    }

    public static void putToSharedPre(Context context, VersionInfo versionInfo) {
        SharedPreUtils.putString(context, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION, versionInfo.checkTime);
        SharedPreUtils.putInt(context, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_CODE, versionInfo.versionCode);
        SharedPreUtils.putString(context, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_NAME, versionInfo.versionName);
        SharedPreUtils.putString(context, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_URL, versionInfo.downloadUrl);
    }
}
