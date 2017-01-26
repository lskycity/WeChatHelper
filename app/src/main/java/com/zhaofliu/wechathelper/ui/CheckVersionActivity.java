package com.zhaofliu.wechathelper.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.app.BaseActivity;
import com.zhaofliu.wechathelper.app.HunterApplication;
import com.zhaofliu.wechathelper.apputils.Constants;
import com.zhaofliu.wechathelper.apputils.UpgradeUtils;
import com.zhaofliu.wechathelper.apputils.VersionInfo;
import com.zhaofliu.wechathelper.utils.AppUtils;
import com.zhaofliu.wechathelper.utils.IntentUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhaofliu on 1/2/17.
 * @author zhaofliu
 */

public class CheckVersionActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener {

    private TextView lastDateTextView;
    private TextView newVersionTipText;
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
        download.setOnLongClickListener(this);

        lastDateTextView = (TextView) findViewById(R.id.check_version_information);
        newVersionTipText = (TextView) findViewById(R.id.new_version_text);

        setupNewVersionArea(true);
    }

    @SuppressLint("SetTextI18n")
    private void setupNewVersionArea(boolean firstSetup) {
        VersionInfo versionInfo = UpgradeUtils.getVersionInfoFromSharedPreference(this);
        if(!TextUtils.isEmpty(versionInfo.checkTime)) {
            lastDateTextView.setText(getString(R.string.last_check_date) + versionInfo.getFormatCheckTime());
            if(versionInfo.versionCode > AppUtils.getVersionCode(this)) {

                newVersionTipText.setVisibility(View.VISIBLE);

                download.setVisibility(View.VISIBLE);
                download.setTag(versionInfo.downloadUrl);

                newVersionTipText.setText(getString(R.string.have_new_version, versionInfo.versionName));

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
            fetchLatestVersion();
        } else if(v.getId() == R.id.new_version_download) {
            IntentUtils.startUrl(this, (String) v.getTag());
        } else if(v.getId() == R.id.forward_to_website) {
            IntentUtils.startUrl(this, Constants.WECHAT_VERSION_URL);
        }
    }

    private void fetchLatestVersion() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.CHECK_VERSION_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    VersionInfo versionInfo = UpgradeUtils.getVersionInfo(jsonObject);
                    UpgradeUtils.putToSharedPre(CheckVersionActivity.this, versionInfo);
                    setupNewVersionArea(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        HunterApplication.get().getRequestQueue().add(jsonObjectRequest);

    }


    @Override
    public boolean onLongClick(View v) {
        if(v.getId() == R.id.new_version_download) {
            Toast.makeText(this, (String)v.getTag(), Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}
