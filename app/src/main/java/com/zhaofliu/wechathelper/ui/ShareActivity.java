package com.zhaofliu.wechathelper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhaofliu.wechathelper.MainActivity;
import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.app.BaseActivity;
import com.zhaofliu.wechathelper.apputils.Constants;
import com.zhaofliu.wechathelper.utils.IntentUtils;


/**
 * Created by zhaofliu on 2/1/17.
 *
 *
 */

public class ShareActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_open_acc);

        TextView tv = (TextView) findViewById(R.id.display_text);
        tv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.download_url,0,0);
        tv.setText(R.string.let_your_friend_download);

        Button userGuide = (Button) findViewById(R.id.user_guide);
        Button manPage = (Button) findViewById(R.id.main_page);
        Button shared = (Button) findViewById(R.id.share_to_friend);

        userGuide.setOnClickListener(this);
        manPage.setOnClickListener(this);
        shared.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.user_guide) {
            Intent intent = new Intent(this, UserGuideActivity.class);
            startActivity(intent);
            finish();
        } else if(v.getId() == R.id.main_page) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if(v.getId() == R.id.share_to_friend) {
            IntentUtils.shareText(this, getString(R.string.share_to_friend), Constants.WECHAT_VERSION_URL);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}
