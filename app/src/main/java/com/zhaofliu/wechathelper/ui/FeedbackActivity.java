package com.zhaofliu.wechathelper.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.app.BaseActivity;
import com.zhaofliu.wechathelper.app.HunterApplication;
import com.zhaofliu.wechathelper.apputils.Constants;
import com.zhaofliu.wechathelper.apputils.Feedback;
import com.zhaofliu.wechathelper.utils.AppUtils;
import com.zhaofliu.wechathelper.utils.DeviceUtils;
import com.zhaofliu.wechathelper.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * collect feedback from user
 *
 * @author zhaofliu
 * @since 1/6/17
 */

public class FeedbackActivity extends BaseActivity implements TextWatcher {

    private static final int MAX_TEXT_COUNT = 125;

    private EditText feedback;
    private TextView textCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback = (EditText) findViewById(R.id.feedback_content);
        feedback.addTextChangedListener(this);
        textCount = (TextView) findViewById(R.id.text_count);
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
        } else if(item.getItemId() == R.id.menu_fetch) {
            fetchMyFeedback();
            return true;
        }
        return false;
    }

    private void fetchMyFeedback() {
        String deviceId = DeviceUtils.getDeviceId(this);
        if(TextUtils.isEmpty(deviceId)){
            return;
        }
        String realUrl = Constants.POST_FEEDBACK_URL+"?action=filter&keys=device_id&values="+deviceId;
        JsonArrayRequest request = new JsonArrayRequest(realUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                try {
                    showMyFeedbackDialog(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },

        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(FeedbackActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        HunterApplication.get().getRequestQueue().add(request);
    }

    private void showMyFeedbackDialog(JSONArray jsonArray) throws JSONException {

        int count = jsonArray.length();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.feedback);

        if(count>0) {
            String[] dess = new String[count];
            for(int i=0; i<count; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Feedback back = Feedback.obtain(jsonObject);
                dess[i] = back.description+"  \n"+back.feedbackTime;
            }
            builder.setItems(dess, null);
        } else {
            builder.setMessage(R.string.no_feedback);
        }

        builder.setNegativeButton(android.R.string.cancel, null);

        builder.show();

    }

    private void sendFeedback(String content) {

        if(TextUtils.isEmpty(content.trim())) {
            Toast.makeText(FeedbackActivity.this, R.string.send_feedback_empty, Toast.LENGTH_LONG).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.POST_FEEDBACK_URL, Feedback.obtain(this, content).toJSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Toast.makeText(FeedbackActivity.this, R.string.send_feedback_success, Toast.LENGTH_LONG).show();
                supportFinishAfterTransition();
            }
        },

        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(FeedbackActivity.this, getString(R.string.send_feedback_fail)+", "+volleyError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        HunterApplication.get().getRequestQueue().add(request);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void afterTextChanged(Editable s) {
        textCount.setText(s.length()+"/125");
        if(s.length() == MAX_TEXT_COUNT) {
            textCount.setTextColor(Color.RED);
        } else {
            textCount.setTextColor(getResources().getColor(R.color.text_color));
        }
    }
}
