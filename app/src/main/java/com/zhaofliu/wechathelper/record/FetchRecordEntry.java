package com.zhaofliu.wechathelper.record;

import android.provider.BaseColumns;

/**
 * Created by liuzhaofeng on 2016/1/29.
 */
public class FetchRecordEntry implements BaseColumns{
    private static final String TEXT_TYPE = " TEXT";
    private static final String LONG_TYPE = " LONG";
    private static final String COMMA_SEP = ",";

    public static final String TABLE_NAME = "entry";
    public static final String COLUMN_NAME_AMOUNT = "fetch_amount";
    public static final String COLUMN_NAME_TIME = "fetch_time";
    public static final String COLUMN_NAME_SENDER = "fetch_sender";
    public static final String COLUMN_NAME_DESC = "fetch_description";


    /*package*/ static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_AMOUNT + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_TIME + LONG_TYPE + COMMA_SEP +
                    COLUMN_NAME_SENDER + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_DESC + TEXT_TYPE +
                    " )";
    /*package*/ static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
