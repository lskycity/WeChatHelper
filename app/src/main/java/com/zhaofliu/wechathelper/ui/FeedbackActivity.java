package com.zhaofliu.wechathelper.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.zhaofliu.wechathelper.utils.DeviceUtils;
import com.zhaofliu.wechathelper.utils.IntentUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * collect feedback from user
 *
 * @author zhaofliu
 * @since 1/6/17
 */

public class FeedbackActivity extends BaseActivity implements TextWatcher, View.OnClickListener {

    private static final int MAX_TEXT_COUNT = 125;

    private EditText feedback;
    private TextView textCount;

    private long clickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback = (EditText) findViewById(R.id.feedback_content);
        feedback.addTextChangedListener(this);
        textCount = (TextView) findViewById(R.id.text_count);

        TextView checkUserGuide = (TextView) findViewById(R.id.check_user_guide);
        //link the check user guide activity.
        String checkUserGuideString = getString(R.string.check_normal_question_if_cannot_work);
        Spannable spannable = new SpannableString(checkUserGuideString);
        spannable.setSpan(new UnderlineSpan(), 0, checkUserGuideString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        checkUserGuide.setText(spannable);
        checkUserGuide.setOnClickListener(this);

        TextView checkNewVersion = (TextView) findViewById(R.id.check_new_version);
        //link the check user guide activity.
        String checkNewVersionString = getString(R.string.check_new_version);
        Spannable checkNewVersionSpannable = new SpannableString(checkNewVersionString);
        checkNewVersionSpannable.setSpan(new UnderlineSpan(), 0, checkNewVersionString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        checkNewVersion.setText(checkNewVersionSpannable);
        checkNewVersion.setOnClickListener(this);

        TextView gotoProtectPage = (TextView) findViewById(R.id.go_to_protect);
        String gotoProtectPageString = getString(R.string.tip_add_white_list);
        Spannable gotoProtectPageSpannable = new SpannableString(gotoProtectPageString);
        gotoProtectPageSpannable.setSpan(new UnderlineSpan(), 0, gotoProtectPageString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        gotoProtectPage.setText(gotoProtectPageSpannable);
        gotoProtectPage.setOnClickListener(this);

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
        long currentTime = System.currentTimeMillis();
        if(currentTime-clickTime<2000) {
            return;
        }

        clickTime = currentTime;

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
        Toast.makeText(FeedbackActivity.this, R.string.sending_feedback, Toast.LENGTH_SHORT).show();

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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.check_user_guide) {
            Intent intent = new Intent(this, UserGuideActivity.class);
            startActivity(intent);
        } else if(v.getId() == R.id.check_new_version) {
            IntentUtils.startUrl(this, Constants.WECHAT_VERSION_URL);
        } else if(v.getId() == R.id.go_to_protect) {
            IntentUtils.startUrl(this, Constants.PROTECT_URL);
        }
    }
}
