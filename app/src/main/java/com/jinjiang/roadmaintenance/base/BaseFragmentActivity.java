package com.jinjiang.roadmaintenance.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;


import com.jinjiang.roadmaintenance.R;

import cn.finalteam.okhttpfinal.HttpCycleContext;

public class BaseFragmentActivity extends FragmentActivity implements HttpCycleContext {

    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
        ExitUtils.getInstance().addActivity(this);

        // 去标题栏
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(0, R.anim.base_slide_out);
        finish();
    }

    @Override
    public String getHttpTaskKey() {
        return HTTP_TASK_KEY;
    }
}