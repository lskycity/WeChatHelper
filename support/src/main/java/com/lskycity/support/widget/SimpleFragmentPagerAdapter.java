/*
 * Copyright (c) 2016 Cisco. All rights reserved.
 *
 */
package com.lskycity.support.widget;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 *
 * Created by liuzhaofeng on 3/10/16.
 *
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private final Activity mActivity;

    private final ArrayList<Class<? extends Fragment>> mFragmentList = new ArrayList<>();
    private final ArrayList<String> mFragmentTitleList = new ArrayList<>();
    private final SparseArray<String> fragmentTags = new SparseArray<>();
    private final FragmentManager fragmentManager;


    public SimpleFragmentPagerAdapter(Activity activity, FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
        mActivity = activity;
    }

    public void addFragment(Class<? extends Fragment> f, String title) {
        mFragmentList.add(f);
        mFragmentTitleList.add(title);
    }

    public void addFragment(Class<? extends Fragment> f, int titleId) {
        mFragmentList.add(f);
        mFragmentTitleList.add(mActivity.getString(titleId));
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment.instantiate(mActivity, mFragmentList.get(position).getName());
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public Fragment getFragment(int position) {
        return fragmentManager.findFragmentByTag(fragmentTags.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fragmentTags.put(position, fragment.getTag());
        return fragment;
    }
}
