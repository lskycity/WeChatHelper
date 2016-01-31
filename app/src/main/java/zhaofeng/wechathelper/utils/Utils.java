package zhaofeng.wechathelper.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kevinbest on 16/1/30.
 */
public class Utils {
    private static SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static String getTimeString(long time) {
        return FORMATTER.format(new Date(time));
    }
}
