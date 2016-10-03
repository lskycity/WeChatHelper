package com.zhaofliu.wechathelper.app.commonui;

import android.support.v4.app.Fragment;

import com.zhaofliu.wechathelper.app.SingleFragmentActivity;

public class AboutUsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AboutUsFragment();
    }

}
