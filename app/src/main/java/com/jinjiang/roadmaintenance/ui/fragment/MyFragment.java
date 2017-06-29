package com.jinjiang.roadmaintenance.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.data.UserInfo;
import com.jinjiang.roadmaintenance.ui.activity.LoginActivity;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.jinjiang.roadmaintenance.utils.ACache;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 我的
 */
public class MyFragment extends Fragment {
    private static MyFragment fragment;
    @BindView(R.id.personalCenter_img)
    SimpleDraweeView mImg;
    @BindView(R.id.personalCenter_name)
    TextView mName;
    @BindView(R.id.personalCenter_job)
    TextView mJob;
    @BindView(R.id.personalCenter_mobilephone)
    TextView mMobilephone;
    @BindView(R.id.personalCenter_mail)
    TextView mMail;
    @BindView(R.id.personalCenter_company)
    TextView mCompany;
    Unbinder unbinder;
    private UserInfo userInfo;

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
        unbinder = ButterKnife.bind(this, view);

        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {

        ACache mAcache = ACache.get(getActivity());
        userInfo = (UserInfo) mAcache.getAsObject("UserInfo");

        if (userInfo == null || TextUtils.isEmpty(userInfo.getAppSid())) {
            myToast.toast(getActivity(), "登录状态已过期，请重新登录！");
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }

    private void initData() {
        mName.setText(userInfo.getRealName());
        mMobilephone.setText(userInfo.getUserTel());
        mJob.setText(userInfo.getUserRoleName());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.personalCenter_exit)
    public void onViewClicked() {
    }
}
