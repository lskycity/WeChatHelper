package com.zhaofliu.wechathelper.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.app.BaseActivity;
import com.zhaofliu.wechathelper.apputils.InformCheck;


/**
 * Created by zhaofliu on 2/5/17.
 *
 */

public class PublishInformActivity extends BaseActivity {

    private PublishInformActivity.InformReceiver informReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform);

        updateContent();

        IntentFilter filter = new IntentFilter(InformCheck.ACTION_INFORM_CHANGED);
        registerReceiver(informReceiver = new PublishInformActivity.InformReceiver(), filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(informReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.refresh) {
            Toast.makeText(this, R.string.refreshing, Toast.LENGTH_LONG).show();
            InformCheck.checkInform();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateContent() {
        InformCheck.Inform inform = InformCheck.getInformFromSharedPreference(this);

        TextView content = (TextView) findViewById(R.id.content);
        content.setText(inform.content);
        content.setAutoLinkMask(Linkify.ALL);
    }

    class InformReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            updateContent();

        }
    }
}
