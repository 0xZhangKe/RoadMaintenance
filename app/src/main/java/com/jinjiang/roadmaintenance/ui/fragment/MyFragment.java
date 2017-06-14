package com.jinjiang.roadmaintenance.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinjiang.roadmaintenance.R;


/**
 * 我的
 */
public class MyFragment extends Fragment {
    private static MyFragment fragment;

    public MyFragment() {
    }

    public static MyFragment newInstance() {

        if (fragment == null) {
            fragment = new MyFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {

    }

    private void initData() {

    }


}
