package com.zhaofliu.wechathelper.apputils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zhaofliu on 1/3/17.
 */

public class UpgradeUtils {
    private static final String APP_ORIGINAL_PACKAGE_NAME = "com.zhaofliu.wechathelper";
    private static final String APP_WANDOUJIA_PACKAGE_NAME = "zhaofeng.wechathelper";

    public static VersionInfo getJSONObjectFromURL() throws IOException {
        HttpURLConnection urlConnection = null;

        URL url = new URL(Constants.CHECK_VERSION_URL);

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);

        urlConnection.setDoOutput(true);

        urlConnection.connect();

        return readJsonStream(url.openStream());
    }

    private static VersionInfo readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readMessage(reader);
        } finally {
            reader.close();
        }
    }

    private static VersionInfo readMessage(JsonReader reader) throws IOException {
        VersionInfo info = new VersionInfo();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("package_name")) {
                info.packageName = reader.nextString();
            } else if (name.equals("version_code")) {
                info.versionCode = reader.nextInt();
            } else if (name.equals("version_name")) {
                info.versionName = reader.nextString();
            } else if(name.equals("url")){
                info.downloadUrl = reader.nextString();
            } else if(name.equals("url_wandoujia")) {
                info.urlForWandoujia = reader.nextString();
            }
        }
        reader.endObject();

        return info;
    }

    public static boolean isWandoujiaVersion(Context context) {
        return TextUtils.equals(context.getPackageName(), APP_WANDOUJIA_PACKAGE_NAME);
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
}
