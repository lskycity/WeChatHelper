package zhaofeng.wechathelper.utils;

import java.util.Date;

/**
 * Created by kevinbest on 16/1/30.
 */
public class Utils {
    public static String getTimeString(long time)
    {
        return new Date(time).toLocaleString();
    }
}
