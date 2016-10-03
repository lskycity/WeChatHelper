package com.zhaofliu.wechathelper.app.commonui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zhaofeng.wechathelper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisclaimerFragment extends Fragment {


    public DisclaimerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.disclaimer);
        return inflater.inflate(R.layout.fragment_disclaimer, container, false);
    }

}
