package com.zhaofliu.wechathelper.ui;

import android.support.v4.app.Fragment;

import com.zhaofliu.wechathelper.app.SingleFragmentActivity;

public class UserGuideActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new UserGuideFragment();
    }
}
