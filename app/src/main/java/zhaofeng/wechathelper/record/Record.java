package zhaofeng.wechathelper.record;

/**
 * Created by liuzhaofeng on 2016/1/30.
 */
public class Record {
    public String amount;
    public long time;
    public String sender;
    public String desc;

    @Override
    public String toString() {
        return amount+", "+time+", "+sender+", "+desc;
    }
}
