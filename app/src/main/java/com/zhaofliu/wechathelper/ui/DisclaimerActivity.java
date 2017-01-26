package com.zhaofliu.wechathelper.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.app.BaseActivity;
import com.zhaofliu.wechathelper.utils.AppUtils;
import com.zhaofliu.wechathelper.utils.SharedPreUtils;
import com.zhaofliu.wechathelper.utils.ViewUtils;


public class DisclaimerActivity extends BaseActivity implements View.OnClickListener {

    public static boolean shouldStartDisclaimerActivity(Context context) {
        int versionCode = SharedPreUtils.getInt(context, SharedPreUtils.KEY_LATEST_APP_VERSION_CODE);
        int currentCode = AppUtils.getVersionCode(context);
        return currentCode > versionCode;
    }

    public static void startDisclaimerActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, DisclaimerActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);

        LinearLayout buttonBar = (LinearLayout) findViewById(R.id.buttonBar);
        Button agreeButton = (Button) findViewById(R.id.agree);
        Button disagreeButton = (Button) findViewById(R.id.disagree);

        agreeButton.setOnClickListener(this);
        disagreeButton.setOnClickListener(this);

        int versionCode = SharedPreUtils.getInt(this, SharedPreUtils.KEY_LATEST_APP_VERSION_CODE);
        int currentCode = AppUtils.getVersionCode(this);
        ViewUtils.setVisible(buttonBar, (currentCode > versionCode));

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null && currentCode > versionCode) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        int versionCode = SharedPreUtils.getInt(this, SharedPreUtils.KEY_LATEST_APP_VERSION_CODE);
        int currentCode = AppUtils.getVersionCode(this);
        if(versionCode == currentCode) {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.agree) {
            SharedPreUtils.putInt(this, SharedPreUtils.KEY_LATEST_APP_VERSION_CODE, AppUtils.getVersionCode(this));
            setResult(RESULT_OK);
            supportFinishAfterTransition();
        } else if(v.getId() == R.id.disagree){
            setResult(RESULT_CANCELED);
            supportFinishAfterTransition();
        }
    }
}
