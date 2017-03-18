package com.zhaofliu.wechathelper.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lskycity.support.utils.AppUtils;
import com.lskycity.support.utils.IntentUtils;
import com.zhaofliu.wechathelper.MainActivity;
import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.app.BaseActivity;
import com.zhaofliu.wechathelper.apputils.Constants;
import com.zhaofliu.wechathelper.apputils.ServiceUtils;


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

        TextView tv = (TextView) findViewById(R.id.display_text);
        Button userGuide = (Button) findViewById(R.id.user_guide);
        Button manPage = (Button) findViewById(R.id.main_page);
        Button shared = (Button) findViewById(R.id.share_to_friend);


        if(!ServiceUtils.isAccessibilityEnabled(this)) {
            ServiceUtils.openAccServiceSetting(this);
            finish();
        } else {
            userGuide.setOnClickListener(this);
            manPage.setOnClickListener(this);
            shared.setOnClickListener(this);
        }

        boolean unlockFlag = getIntent().getBooleanExtra(KEY_UNLOCK, false);
        if(unlockFlag) {
            tv.setText(R.string.unlocking_screen);
            userGuide.setVisibility(View.GONE);
            manPage.setVisibility(View.GONE);

            if(getSupportActionBar()!=null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            }

            AppUtils.unlockScreen(this, true);
            finishHandler.sendEmptyMessageDelayed(1, 1000);
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
        } else if(v.getId() == R.id.share_to_friend) {
            IntentUtils.shareText(this, getString(R.string.share_to_friend), Constants.WECHAT_VERSION_URL);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}
