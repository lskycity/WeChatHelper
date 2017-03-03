package com.zhaofliu.wechathelper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.app.BaseActivity;
import com.zhaofliu.wechathelper.apputils.Constants;
import com.zhaofliu.wechathelper.apputils.ServiceUtils;
import com.zhaofliu.wechathelper.utils.SharedPreUtils;

/**
 * Created by zhaofliu on 2/22/17.
 *
 * @author zhaofliu
 * @since 2/22/17
 */

public class ServiceForegroundSettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_foreground_setting);

        SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.service_foreground_switch);
        switchCompat.setChecked(SharedPreUtils.getBoolean(this, Constants.SHARED_KEY_SERVICE_FOREGROUND, true));
        switchCompat.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId() == R.id.service_foreground_switch) {

            SharedPreUtils.putBoolean(this, Constants.SHARED_KEY_SERVICE_FOREGROUND, isChecked);

            if(ServiceUtils.isNotificationAccessed(this)) {

                Intent intent = new Intent();
                intent.setAction(Constants.KEY_SERVICE_FOREGROUND_STATE_CHANGED);
                intent.putExtra(Constants.KEY_SERVICE_FOREGROUND, isChecked);
                sendBroadcast(intent);

            }

        }
    }
}
