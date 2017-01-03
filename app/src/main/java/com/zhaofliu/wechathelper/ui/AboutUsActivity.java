package com.zhaofliu.wechathelper.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.app.BaseActivity;
import com.zhaofliu.wechathelper.app.SingleFragmentActivity;

import java.util.List;

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_about_us);
        findViewById(R.id.mail_link).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.mail_link:
                sendMail(getString(R.string.team_mail_address));
                break;
        }
    }

    private void sendMail(String address)
    {
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_content));

        PackageManager pManager = getPackageManager();
        List<ResolveInfo> info = pManager.queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if(info!=null)
        {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.mail_chooser_title)));
        }
    }
}
