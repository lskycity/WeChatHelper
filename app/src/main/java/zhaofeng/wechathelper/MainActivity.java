package zhaofeng.wechathelper;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import zhaofeng.wechathelper.record.FetchRecordDbHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button openButton = (Button) findViewById(R.id.open_button);
        openButton.setOnClickListener(this);

        TextView mTextView = (TextView) findViewById(R.id.description);
        Cursor cursor = new FetchRecordDbHelper(this).query();
        StringBuilder builder = new StringBuilder();
        while (cursor.moveToNext()) {
            String amount = cursor.getString(1);
            long time = cursor.getLong(2);
            builder.append(amount);
            builder.append(getString(R.string.key_word_money_unit));
            builder.append(", ");
            builder.append(new Date(time).toLocaleString());
            builder.append(", ");
            builder.append(cursor.getString(3));
            builder.append(", ");
            builder.append(cursor.getString(4));
            builder.append("\n");
        }
        mTextView.setText(builder);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewGroup serviceTipPanel = (ViewGroup) findViewById(R.id.service_tip_panel);
        serviceTipPanel.setVisibility(isAccessibilityEnabled() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.open_button) {
            openServiceSetting();
        }
    }

    private void openServiceSetting() {
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }

    public boolean isAccessibilityEnabled(){
        int accessibilityEnabled = 0;
        final String ACCESSIBILITY_SERVICE_NAME = "zhaofeng.wechathelper/zhaofeng.wechathelper.FetchLuckyMoneyService";
        try {
            accessibilityEnabled = Settings.Secure.getInt(this.getContentResolver(),android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {
        }

        if (accessibilityEnabled==1){
            String settingValue = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(':');
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessibilityService = splitter.next();
                    if (accessibilityService.equalsIgnoreCase(ACCESSIBILITY_SERVICE_NAME)){
                        return true;
                    }
                }
            }

        }
        return false;
    }

}
