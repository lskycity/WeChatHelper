package com.zhaofliu.wechathelper.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.app.BaseActivity;
import com.zhaofliu.wechathelper.utils.HttpUtils;

/**
 * collect feedback from user
 *
 * @author zhaofliu
 * @since 1/6/17
 */

public class FeedbackActivity extends BaseActivity {

    private EditText feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback = (EditText) findViewById(R.id.feedback_content);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_send) {
            sendFeedback(feedback.getText().toString());
            return true;
        }
        return false;
    }

    private void sendFeedback(String content) {
        HttpUtils.doGet("http://www.lskycity.com/wechathelper/feedback?content="+content);
    }

}
