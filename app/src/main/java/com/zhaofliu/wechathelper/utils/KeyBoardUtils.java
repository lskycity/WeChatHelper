package com.zhaofliu.wechathelper.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by liuzhaofeng on 2015/10/7.
 */
public class KeyBoardUtils {

    public static void openKeybord(Activity activity) {
        final View focusView;
        if ((focusView = activity.getCurrentFocus()) != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(focusView, 0);
        }
    }


    /**
     * open key bord
     *
     */
    public static void openKeybord(EditText editText) {
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * close key bord
     *
     */
    public static void closeKeybord(Activity activity) {
        final View view;
        if((view = activity.getCurrentFocus()) != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    /**
     * close key bord
     *
     */
    public static void closeKeybord(EditText editText) {
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }

}
