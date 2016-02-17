package zhaofeng.wechathelper;

import android.support.v4.app.Fragment;

import zhaofeng.wechathelper.app.SingleFragmentActivity;
import zhaofeng.wechathelper.fragment.AboutUsFragment;

public class AboutUsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AboutUsFragment();
    }

}
