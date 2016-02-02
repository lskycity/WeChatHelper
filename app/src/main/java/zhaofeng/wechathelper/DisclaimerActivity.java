package zhaofeng.wechathelper;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import zhaofeng.wechathelper.fragment.DisclaimerFragment;

public class DisclaimerActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new DisclaimerFragment();
    }
}
