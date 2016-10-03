package com.zhaofliu.wechathelper.ui;


import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.utils.SharedPreUtils;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {



    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        getPreferenceManager().setSharedPreferencesName(SharedPreUtils.SHARED_PREFERENCE_NAME);
    }


}
