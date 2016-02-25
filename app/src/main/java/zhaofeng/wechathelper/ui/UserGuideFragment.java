package zhaofeng.wechathelper.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zhaofeng.wechathelper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserGuideFragment extends Fragment {


    public UserGuideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.user_guide);
        return inflater.inflate(R.layout.fragment_user_guide, container, false);
    }

}
