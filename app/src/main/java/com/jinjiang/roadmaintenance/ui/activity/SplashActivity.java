package com.jinjiang.roadmaintenance.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
import com.jinjiang.roadmaintenance.data.UserInfo;
import com.jinjiang.roadmaintenance.utils.ACache;

/**
 * 启动界面
 * (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 * (2)是，则进入GuideActivity；否，则进入MainActivity
 * (3)SPLASH_DELAY_MILLIS s后执行(2)操作
 */
public class SplashActivity extends BaseActivity {
    boolean isFirstIn = false;
    boolean isLogin = false;

    private static final int GO_GUIDE = 1000;
    private static final int GO_LOGIN = 1001;
    private static final int GO_HOME = 1002;
    // 延迟3秒
    private static final long SPLASH_DELAY_MILLIS = 3;
    private ACache mAcache;
    private UserInfo userInfo;
    /**
     * Handler:跳转到不同界面
     */
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_GUIDE:
                    goGuide();
                    break;
                case GO_LOGIN:
                    goLogin();
                    break;
                case GO_HOME:
                    goHome();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       /*set it to be full screen*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initUI() {
        mAcache = ACache.get(SplashActivity.this);
    }

    @Override
    protected void setAnim() {
    }

    @Override
    protected void initData() {

        userInfo = (UserInfo) mAcache.getAsObject("UserInfo");
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getUserId())) {
            isLogin = true;
        } else {
            isLogin = false;
        }

//        isFirstIn = (boolean) SPUtils.get(SplashActivity.this, "isFirstIn", true);

        // 判断如果是第一次运行则跳转到引导界面，否则跳转到主界面
//        if (isFirstIn) {
//            mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
//        } else
        if (isLogin) {
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_LOGIN, SPLASH_DELAY_MILLIS);
        }
    }


    /**
     * 进入引导页
     */
    private void goGuide() {
//		Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
//		SplashActivity.this.startActivity(intent);
//		SplashActivity.this.finish();
    }

    /**
     * 进入登陆页面
     */
    private void goLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

    /**
     * 进入首页
     */
    private void goHome() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }
}