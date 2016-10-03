package com.zhaofliu.wechathelper.record;

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

    public Record(String amount, long time, String sender, String desc) {
        this.amount = amount;
        this.time = time;
        this.desc = desc;
        this.sender = sender;
    }

    public Record() {
    }
}
