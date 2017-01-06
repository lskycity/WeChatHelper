package com.zhaofliu.wechathelper.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * base activity for common thing.
 *
 * @author zhaofliu
 * @since 2016/2/13
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if(!super.onSupportNavigateUp()) {
            supportFinishAfterTransition();
            return true;
        }
        return false;
    }
}
