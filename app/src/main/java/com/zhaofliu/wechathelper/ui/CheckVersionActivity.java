package com.zhaofliu.wechathelper.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.app.BaseActivity;
import com.zhaofliu.wechathelper.apputils.Constants;
import com.zhaofliu.wechathelper.apputils.UpgradeUtils;
import com.zhaofliu.wechathelper.apputils.VersionInfo;
import com.zhaofliu.wechathelper.utils.AppUtils;
import com.zhaofliu.wechathelper.utils.DateUtils;
import com.zhaofliu.wechathelper.utils.SharedPreUtils;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by zhaofliu on 1/2/17.
 * @author zhaofliu
 */

public class CheckVersionActivity extends BaseActivity implements View.OnClickListener {

    private TextView lastDateTextView;
    TextView newVersionTipText;
    private Button download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_version);

        TextView version = (TextView) findViewById(R.id.version_info);
        version.setText(AppUtils.getVersionName(this));

        findViewById(R.id.check_version).setOnClickListener(this);
        findViewById(R.id.forward_to_website).setOnClickListener(this);

        download = (Button) findViewById(R.id.new_version_download);
        download.setOnClickListener(this);

        lastDateTextView = (TextView) findViewById(R.id.check_version_information);
        newVersionTipText = (TextView) findViewById(R.id.new_version_text);

        setupNewVersionArea(true);
    }

    @SuppressLint("SetTextI18n")
    private void setupNewVersionArea(boolean firstSetup) {
        String lastDate = SharedPreUtils.getString(this, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION);
        if(!TextUtils.isEmpty(lastDate)) {
            lastDateTextView.setText(getString(R.string.last_check_date) + DateUtils.getTimeString(Long.valueOf(lastDate)));
            int versionCode = SharedPreUtils.getInt(this, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_CODE);
            if(versionCode > AppUtils.getVersionCode(this)) {
                newVersionTipText.setVisibility(View.VISIBLE);
                download.setVisibility(View.VISIBLE);

                String versionName = SharedPreUtils.getString(this, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_NAME);

                newVersionTipText.setText(getString(R.string.have_new_version, versionName));

                String downloadUrl = SharedPreUtils.getString(this, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_URL);
                download.setTag(downloadUrl);

            } else if(firstSetup) {
                newVersionTipText.setVisibility(View.GONE);
                download.setVisibility(View.GONE);
            } else {
                newVersionTipText.setVisibility(View.VISIBLE);
                newVersionTipText.setText(R.string.no_new_version);
                download.setVisibility(View.GONE);
            }
        } else {
            lastDateTextView.setText(getString(R.string.last_check_date) + getString(R.string.no_check_date));
            newVersionTipText.setVisibility(View.GONE);
            download.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.check_version) {
            new FetchDataTask().execute();
        } else if(v.getId() == R.id.new_version_download) {
            String url = (String) v.getTag();
            try {
                Intent i = Intent.parseUri(url, 0);
                startActivity(i);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if(v.getId() == R.id.forward_to_website) {
            try {
                Intent i = Intent.parseUri(Constants.WECHAT_VERSION_URL, 0);
                startActivity(i);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    class FetchDataTask extends AsyncTask<Object, Object, VersionInfo> {

        @Override
        protected VersionInfo doInBackground(Object... params) {
            try {
                return UpgradeUtils.getJSONObjectFromURL();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(VersionInfo messages) {
            SharedPreUtils.putString(CheckVersionActivity.this, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION, String.valueOf(System.currentTimeMillis()));
            SharedPreUtils.putInt(CheckVersionActivity.this, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_CODE, messages.versionCode);
            SharedPreUtils.putString(CheckVersionActivity.this, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_NAME, messages.versionName);
            SharedPreUtils.putString(CheckVersionActivity.this, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_URL, messages.downloadUrl);
            setupNewVersionArea(false);
        }
    }

}
