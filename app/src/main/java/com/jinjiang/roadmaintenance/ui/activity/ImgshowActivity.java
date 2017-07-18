package com.jinjiang.roadmaintenance.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ImgshowActivity extends BaseActivity {

    @BindView(R.id.imgshow_img)
    ImageView mImg;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_imgshow;
    }

    @Override
    protected void initUI() {

        url = getIntent().getStringExtra("url");


    }

    @Override
    protected void setAnim() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.setAnim();
    }

    @Override
    protected void initData() {
        LogUtils.d(mImg);
        LogUtils.d(url);
        Glide.with(ImgshowActivity.this).load(url).into(mImg);
    }

    @OnClick({R.id.eventdetail2_back, R.id.imgshow_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.eventdetail2_back:
                finish();
                break;
            case R.id.imgshow_img:
                finish();
                break;
        }
    }
}
