package com.zhaofliu.wechathelper.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.utils.DateUtils;

/**
 * Created by kevinbest on 16/1/30.
 */
public class LuckyMoneyCursorAdapter extends CursorAdapter {

    private class ViewHolder {
        TextView money;
        TextView time;
        TextView sender;
        TextView desc;
    }

    public LuckyMoneyCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(context);
        View convertView = inflater.inflate(R.layout.lucky_money_record_item, parent, false);
        holder.desc = (TextView) convertView.findViewById(R.id.description);
        holder.money = (TextView) convertView.findViewById(R.id.amount);
        holder.sender = (TextView) convertView.findViewById(R.id.sender);
        holder.time = (TextView) convertView.findViewById(R.id.time);
        convertView.setTag(holder);
        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (cursor != null) {
            String amount = cursor.getString(1) + context.getString(R.string.key_word_money_unit);
            long time = cursor.getLong(2);
            String sender = cursor.getString(3);
            String description = cursor.getString(4);
            holder.desc.setText(description);
            holder.time.setText(DateUtils.getTimeString(time));
            holder.money.setText(amount);
            holder.sender.setText(sender);
        }
    }

    public void refreshData(Cursor cursor) {
        changeCursor(cursor);
    }

}
