package zhaofeng.wechathelper;

import android.support.v4.app.Fragment;

import zhaofeng.wechathelper.app.SingleFragmentActivity;
import zhaofeng.wechathelper.fragment.DisclaimerFragment;

public class DisclaimerActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new DisclaimerFragment();
    }
}
