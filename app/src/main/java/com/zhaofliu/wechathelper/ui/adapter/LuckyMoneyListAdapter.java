package com.zhaofliu.wechathelper.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.record.Record;
import com.zhaofliu.wechathelper.utils.DateUtils;

/**
 * Created by kevinbest on 16/1/30.
 */
public class LuckyMoneyListAdapter extends BaseAdapter {
    private List<Record> mData;
    private Context mContext;

    private class ViewHolder {
        TextView money;
        TextView time;
        TextView sender;
        TextView desc;
    }

    public LuckyMoneyListAdapter(List<Record> mData, Context cxt) {
        this.mData = mData;
        mContext = cxt;
    }

    public LuckyMoneyListAdapter(Context cxt) {
        mContext = cxt;
    }

    public void updateData(List<Record> data)
    {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lucky_money_record_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.desc = (TextView)convertView.findViewById(R.id.description);
            holder.money = (TextView)convertView.findViewById(R.id.amount);
            holder.sender = (TextView)convertView.findViewById(R.id.sender);
            holder.time = (TextView)convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder)convertView.getTag();
        Record item = (Record)getItem(position);
        holder.desc.setText(item.desc);
        holder.time.setText(DateUtils.getTimeString(item.time));
        holder.money.setText(item.amount);
        holder.sender.setText(item.sender);
        return convertView;
    }


    private void refreshData(List<Record> newData) {
        mData = newData;
        notifyDataSetChanged();
    }
}
