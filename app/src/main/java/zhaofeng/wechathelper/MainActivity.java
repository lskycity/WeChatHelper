package zhaofeng.wechathelper;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Date;

import zhaofeng.wechathelper.record.FetchRecordDbHelper;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.description);

        Cursor cursor = new FetchRecordDbHelper(this).query();
        StringBuilder builder = new StringBuilder();
        while (cursor.moveToNext()) {
            String amount = cursor.getString(1);
            long time = cursor.getLong(2);
            builder.append(amount);
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

}
