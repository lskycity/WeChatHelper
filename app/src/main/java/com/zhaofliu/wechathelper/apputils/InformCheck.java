package com.zhaofliu.wechathelper.apputils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zhaofliu.wechathelper.app.HunterApplication;
import com.zhaofliu.wechathelper.utils.CharsetJsonRequest;
import com.zhaofliu.wechathelper.utils.SharedPreUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * check inform from server
 *
 * @author zhaofliu
 * @since 2/5/17
 */

public class InformCheck {

    public static final long TIME_GREP_CHECK_INFORM = 1000*60*60*24*2;
    public static final String ACTION_INFORM_CHANGED = "com.lskycity.androidtools.action_inform_changed";


    public static boolean shouldCheckInform(Context context) {
        String lastCheckTime = SharedPreUtils.getString(context, Constants.SHARED_KEY_INFORM_CHECK_TIME);

        return TextUtils.isEmpty(lastCheckTime)
                || (System.currentTimeMillis() - Long.parseLong(lastCheckTime)) > TIME_GREP_CHECK_INFORM;
    }

    public static void checkInform() {
        CharsetJsonRequest jsonObjectRequest = new CharsetJsonRequest(Constants.INFORM_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Inform latest = getInformFromSharedPreference(HunterApplication.get());

                    Inform info = getInform(jsonObject);

                    putToSharedPre(HunterApplication.get(), info);

                    if(!TextUtils.equals(latest.content, info.content)) {
                        sendBroadcast();
                    }

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


    public static Inform getInform(JSONObject jsonObject) throws JSONException {
        Inform info = new Inform();
        info.id = jsonObject.getString("inform_id");
        info.content = jsonObject.getString("inform_content");
        info.checkTime = String.valueOf(System.currentTimeMillis());
        return info;
    }

    public static void putToSharedPre(Context context, Inform info) {
        SharedPreUtils.putString(context, Constants.SHARED_KEY_INFORM_CHECK_TIME, info.checkTime);
        SharedPreUtils.putString(context, Constants.SHARED_KEY_INFORM_ID, info.id);
        SharedPreUtils.putString(context, Constants.SHARED_KEY_INFORM_CONTENT, info.content);
    }

    public static Inform getInformFromSharedPreference(Context context) {
        Inform info = new Inform();
        info.id = SharedPreUtils.getString(context, Constants.SHARED_KEY_INFORM_ID);
        info.content = SharedPreUtils.getString(context, Constants.SHARED_KEY_INFORM_CONTENT);
        info.checkTime = SharedPreUtils.getString(context, Constants.SHARED_KEY_INFORM_CHECK_TIME);
        return info;
    }

    private static void sendBroadcast() {
        Intent intent=new Intent();
        intent.setAction(ACTION_INFORM_CHANGED);
//        intent.putExtra(ACTION_INFORM_CHANGED, true);
        HunterApplication.get().sendBroadcast(intent);

    }


    public static class Inform {
        public String id;
        public String content;
        public String checkTime;

        @Override
        public String toString() {
            return "Inform{" +
                    "id='" + id + '\'' +
                    ", content='" + content + '\'' +
                    ", checkTime='" + checkTime + '\'' +
                    '}';
        }
    }
}
