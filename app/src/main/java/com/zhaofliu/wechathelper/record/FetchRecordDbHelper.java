package com.zhaofliu.wechathelper.record;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by liuzhaofeng on 2016/1/29.
 */
public class FetchRecordDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FetchRecord.db";

    public FetchRecordDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FetchRecordEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FetchRecordEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FetchRecordEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void insert(String amount, long time, String sender, String desc) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FetchRecordEntry.COLUMN_NAME_AMOUNT, amount);
        values.put(FetchRecordEntry.COLUMN_NAME_TIME, time);
        values.put(FetchRecordEntry.COLUMN_NAME_SENDER, sender);
        values.put(FetchRecordEntry.COLUMN_NAME_DESC, desc);

        db.insert(FetchRecordEntry.TABLE_NAME, null, values);
    }
    public void insert(Record record) {
        insert(record.amount, record.time, record.sender, record.desc);
    }

    public Cursor query() {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                FetchRecordEntry._ID,
                FetchRecordEntry.COLUMN_NAME_AMOUNT,
                FetchRecordEntry.COLUMN_NAME_TIME,
                FetchRecordEntry.COLUMN_NAME_SENDER,
                FetchRecordEntry.COLUMN_NAME_DESC
        };
        return db.query(FetchRecordEntry.TABLE_NAME, projection, null, null, null, null, FetchRecordEntry.COLUMN_NAME_TIME+" DESC");
    }
}
