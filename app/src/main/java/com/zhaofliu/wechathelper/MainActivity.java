package com.zhaofliu.wechathelper;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zhaofliu.wechathelper.ui.AboutUsActivity;
import com.zhaofliu.wechathelper.record.FetchRecordDbHelper;
import com.zhaofliu.wechathelper.ui.SettingsActivity;
import com.zhaofliu.wechathelper.ui.adapter.LuckyMoneyCursorAdapter;
import com.zhaofliu.wechathelper.apputils.Constants;
import com.zhaofliu.wechathelper.utils.SharedPreUtils;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView mListView;
    private FetchRecordDbHelper mDbHelper;
    private LuckyMoneyCursorAdapter mAdapter;
    private TextView mTotalMoneyTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button openButton = (Button) findViewById(R.id.open_button);
        mListView = (ListView) findViewById(android.R.id.list);
        mAdapter = new LuckyMoneyCursorAdapter(this, null);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(findViewById(android.R.id.empty));
        mTotalMoneyTips = (TextView)findViewById(R.id.tips);
        openButton.setOnClickListener(this);
        mDbHelper = new FetchRecordDbHelper(this);

        TextView openServiceTip = (TextView) findViewById(R.id.open_service_tip);
        openServiceTip.setText(Html.fromHtml(getString(R.string.open_service_tip)));

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
        showTotalMoneyCollected();
        ViewGroup serviceTipPanel = (ViewGroup) findViewById(R.id.service_tip_panel);
        serviceTipPanel.setVisibility(isAccessibilityEnabled() ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_settings) {
            openSettingsActivity();
            return true;
        }
        return false;
    }

    private void openSettingsActivity() {
        startActivity(new Intent(this,SettingsActivity.class));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.open_button) {
            openServiceSetting();
        }
    }

    private void openServiceSetting() {
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }

    public boolean isAccessibilityEnabled() {
        int accessibilityEnabled = 0;
        final String ACCESSIBILITY_SERVICE_NAME = "com.zhaofliu.wechathelper/com.zhaofliu.wechathelper.FetchLuckyMoneyService";
        try {
            accessibilityEnabled = Settings.Secure.getInt(this.getContentResolver(), android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {
        }

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(':');
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessibilityService = splitter.next();
                    if (accessibilityService.equalsIgnoreCase(ACCESSIBILITY_SERVICE_NAME)) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private void refreshList() {
        if (mAdapter != null) {
            mAdapter.refreshData(mDbHelper.query());
        }
    }

    private void showTotalMoneyCollected() {
        mTotalMoneyTips.setText(String.format(Locale.getDefault(), "红包助手已为你抢到%.2f元.",calculateTotalMoney()));
    }

    private float calculateTotalMoney() {
        Cursor cursor = mDbHelper.query();
        float total = 0.0f;
        while (cursor.moveToNext()) {
            String amount = cursor.getString(1);
            try {
                float fAmount = Float.valueOf(amount);
                total += fAmount;

            } catch (NumberFormatException e) {
                Log.v("calculateTotalMoney", "calculate total money fail, value is noe float, value is " + amount);
            }
        }
        return total;
    }

}
