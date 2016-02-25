package zhaofeng.wechathelper.app.commonui;

import android.support.v4.app.Fragment;

import zhaofeng.wechathelper.app.SingleFragmentActivity;

public class AboutUsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AboutUsFragment();
    }

}
