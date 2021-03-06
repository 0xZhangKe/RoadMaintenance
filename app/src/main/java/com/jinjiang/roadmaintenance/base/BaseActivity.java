package com.jinjiang.roadmaintenance.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.apkfuns.logutils.LogUtils;
import com.jinjiang.roadmaintenance.R;

import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpCycleContext;

public abstract class BaseActivity extends AppCompatActivity implements HttpCycleContext{

    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setAnim();
        ExitUtils.getInstance().addActivity(this);
        // 去标题栏
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(getLayoutResID());

        //打印打开的activity的类名和所在的包
        LogUtils.i(getClass().getName());
        initUI();
        ButterKnife.bind( this ) ;
        initData();
    }

    /**
     * getContentView ID
     *
     * @return
     */
    protected abstract int getLayoutResID();

    protected void setAnim() {
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    ;

    /**
     * 初始化UI
     */
    protected abstract void initUI();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.base_slide_out);
        super.onBackPressed();
    }
    @Override
    public String getHttpTaskKey() {
        return HTTP_TASK_KEY;
    }
}
