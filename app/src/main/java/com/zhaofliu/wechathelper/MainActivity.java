package com.zhaofliu.wechathelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zhaofliu.wechathelper.apputils.InformCheck;
import com.zhaofliu.wechathelper.apputils.ServiceUtils;
import com.zhaofliu.wechathelper.apputils.UpgradeUtils;
import com.zhaofliu.wechathelper.record.FetchRecordDbHelper;
import com.zhaofliu.wechathelper.ui.DisclaimerActivity;
import com.zhaofliu.wechathelper.ui.SettingsActivity;
import com.zhaofliu.wechathelper.ui.adapter.LuckyMoneyCursorAdapter;
import com.zhaofliu.wechathelper.utils.ViewUtils;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_AGREEMENT_FOR_DISCLAIM = 121;

    private FetchRecordDbHelper mDbHelper;
    private LuckyMoneyCursorAdapter mAdapter;
    private TextView mTotalMoneyTips;

    private InformReceiver informReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button openButton = (Button) findViewById(R.id.open_button);

        ListView mListView = (ListView) findViewById(android.R.id.list);
        mAdapter = new LuckyMoneyCursorAdapter(this, null);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(findViewById(android.R.id.empty));

        mTotalMoneyTips = (TextView)findViewById(R.id.tips);
        openButton.setOnClickListener(this);

        findViewById(R.id.open_notification_button).setOnClickListener(this);

        mDbHelper = new FetchRecordDbHelper(this);

        TextView openServiceTip = (TextView) findViewById(R.id.open_service_tip);
        openServiceTip.setText(Html.fromHtml(getString(R.string.open_service_tip)));

        findViewById(R.id.uninstall_old_version_button).setOnClickListener(this);

        if(DisclaimerActivity.shouldStartDisclaimerActivity(this)) {
            DisclaimerActivity.startDisclaimerActivity(this, REQUEST_AGREEMENT_FOR_DISCLAIM);
        } else {
            if(InformCheck.shouldCheckInform(this)) {
                InformCheck.checkInform();
            }
        }

        UpgradeUtils.checkVersionIfTimeOut();

        IntentFilter filter = new IntentFilter(InformCheck.ACTION_INFORM_CHANGED);
        registerReceiver(informReceiver = new InformReceiver(), filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(informReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_AGREEMENT_FOR_DISCLAIM && resultCode == RESULT_OK) {
            if(InformCheck.shouldCheckInform(this)) {
                InformCheck.checkInform();
            }

        } else if(requestCode == REQUEST_AGREEMENT_FOR_DISCLAIM) {
            supportFinishAfterTransition();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
        showTotalMoneyCollected();
        ViewGroup serviceTipPanel = (ViewGroup) findViewById(R.id.service_tip_panel);
        ViewUtils.setVisible(serviceTipPanel, !ServiceUtils.isAccessibilityEnabled(this));

        ViewGroup notificationServiceTipPanel = (ViewGroup) findViewById(R.id.notification_service_tip_panel);
        ViewUtils.setVisible(notificationServiceTipPanel, !ServiceUtils.isNotificationAccessed(this));

        ViewGroup uninstallOldVersionTipPanel = (ViewGroup) findViewById(R.id.uninstall_old_version_tip_panel);
        ViewUtils.setVisible(uninstallOldVersionTipPanel, !UpgradeUtils.isWandoujiaVersion(this) && UpgradeUtils.isInstalledWandoujiaVersion(this));
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
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void openNotificationSettingsActivity() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.open_button) {
            ServiceUtils.openAccServiceSetting(this);
        } else if(v.getId() == R.id.uninstall_old_version_button) {
            UpgradeUtils.uninstallPrePackage(this);
        } else if(v.getId() == R.id.open_notification_button) {
            ServiceUtils.openNotificationServiceSetting(this);
        }
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

    class InformReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            InformCheck.Inform inform = InformCheck.getInformFromSharedPreference(MainActivity.this);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(inform.content);
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.show();


        }
    }

}
