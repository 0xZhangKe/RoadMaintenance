package com.jinjiang.roadmaintenance.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinjiang.roadmaintenance.R;


/**
 * 进度
 */
public class RateFragment extends Fragment {
    private static RateFragment fragment;

    public RateFragment() {
    }

    public static RateFragment newInstance() {

        if (fragment == null) {
            fragment = new RateFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rate, container, false);

        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {

    }

    private void initData() {

    }

}
