package zhaofeng.wechathelper;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import zhaofeng.wechathelper.fragment.AboutUsFragment;

public class AboutUsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AboutUsFragment();
    }

}
