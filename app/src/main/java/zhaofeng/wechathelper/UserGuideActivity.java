package zhaofeng.wechathelper;

import android.support.v4.app.Fragment;

import zhaofeng.wechathelper.app.SingleFragmentActivity;
import zhaofeng.wechathelper.fragment.UserGuideFragment;

public class UserGuideActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new UserGuideFragment();
    }
}
