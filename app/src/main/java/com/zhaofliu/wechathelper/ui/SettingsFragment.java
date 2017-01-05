package com.zhaofliu.wechathelper.ui;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;

import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.utils.AppUtils;
import com.zhaofliu.wechathelper.utils.SharedPreUtils;


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
        Preference version = getPreferenceScreen().findPreference("app_version");
        version.setSummary(AppUtils.getVersionName(getActivity()));

        int versionCode = SharedPreUtils.getInt(getActivity(), SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_CODE);
        if(versionCode > AppUtils.getVersionCode(getActivity())) {
            String versionName = SharedPreUtils.getString(getActivity(), SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_NAME);
            version.setSummary(getString(R.string.current_version_and_have_new_version, AppUtils.getVersionName(getActivity()), versionName));

        }

    }


}
