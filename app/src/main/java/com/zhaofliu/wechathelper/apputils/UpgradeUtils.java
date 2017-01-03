package com.zhaofliu.wechathelper.apputils;

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

    public static VersionInfo readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readMessage(reader);
        } finally {
            reader.close();
        }
    }

    public static VersionInfo readMessage(JsonReader reader) throws IOException {
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
            } else {
                info.downloadUrl = reader.nextString();
            }
        }
        reader.endObject();

        return info;
    }
}
