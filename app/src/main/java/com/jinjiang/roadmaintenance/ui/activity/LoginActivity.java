package com.jinjiang.roadmaintenance.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {

        return R.layout.activity_login;
    }

    @Override
    protected void initUI() {

        findViewById(R.id.login_loginBt).setOnClickListener(this);

    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_loginBt:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
            default:
                break;
        }
    }
}
