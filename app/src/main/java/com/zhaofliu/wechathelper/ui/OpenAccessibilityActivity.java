package com.zhaofliu.wechathelper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhaofliu.wechathelper.MainActivity;
import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.app.BaseActivity;

/**
 * Created by zhaofliu on 1/6/17.
 */

public class OpenAccessibilityActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_open_acc);

        if(!MainActivity.isAccessibilityEnabled(this)) {
            MainActivity.openServiceSetting(this);
            finish();
        } else {
            findViewById(R.id.user_guide).setOnClickListener(this);
            findViewById(R.id.main_page).setOnClickListener(this);
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
