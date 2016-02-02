package zhaofeng.wechathelper;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import zhaofeng.wechathelper.fragment.UserGuideFragment;

public class UserGuideActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new UserGuideFragment();
    }
}
