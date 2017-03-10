package com.zhaofliu.wechathelper.ui;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.zhaofliu.wechathelper.R;
import com.zhaofliu.wechathelper.apputils.Constants;
import com.zhaofliu.wechathelper.utils.AppUtils;
import com.zhaofliu.wechathelper.utils.SharedPreUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {



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

        ListPreference randomPreference = (ListPreference) getPreferenceScreen().findPreference("random_delay");

        randomPreference.setOnPreferenceChangeListener(this);
        String randomValue = SharedPreUtils.getString(getActivity(), randomPreference.getKey(), "0");
        randomPreference.setValue(randomValue);
        randomPreference.setSummary(randomPreference.getEntry());
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if(TextUtils.equals(preference.getKey(), "random_delay")) {
            setListPreferenceSummary((ListPreference)preference, (String) newValue);
            SharedPreUtils.putInt(getActivity(), Constants.KEY_RANDOM_DELAY_TIME, Integer.parseInt((String) newValue));
            return true;
        }
        return false;
    }

    private void setListPreferenceSummary(ListPreference listPreference, String newValue) {
        CharSequence[] entries = listPreference.getEntries();
        int index = listPreference.findIndexOfValue(newValue);
        listPreference.setSummary(entries[index]);
    }


}
