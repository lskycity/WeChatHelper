package com.zhaofliu.wechathelper.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.zhaofliu.wechathelper.MainActivity;
import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.app.BaseActivity;
import com.zhaofliu.wechathelper.apputils.ServiceUtils;
import com.zhaofliu.wechathelper.utils.AppUtils;


/**
 * Created by zhaofliu on 1/6/17.
 *
 *
 */

public class OpenAccessibilityActivity extends BaseActivity implements View.OnClickListener {

    private static final String KEY_UNLOCK = "key_unlock";

    public static void startForUnlockScreen(Context context) {
        Intent intent = new Intent(context, OpenAccessibilityActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_UNLOCK, true);
        context.startActivity(intent);
    }

    private Handler finishHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_open_acc);

        if(!ServiceUtils.isAccessibilityEnabled(this)) {
            ServiceUtils.openServiceSetting(this);
            finish();
        } else {
            findViewById(R.id.user_guide).setOnClickListener(this);
            findViewById(R.id.main_page).setOnClickListener(this);
        }

        boolean unlockFlag = getIntent().getBooleanExtra(KEY_UNLOCK, false);
        if(unlockFlag) {
            AppUtils.unlockScreen(this, true);
            finishHandler.sendEmptyMessageDelayed(1, 2000);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.user_guide) {
            Intent intent = new Intent(this, UserGuideActivity.class);
            startActivity(intent);
            finish();
        } else if(v.getId() == R.id.main_page) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
