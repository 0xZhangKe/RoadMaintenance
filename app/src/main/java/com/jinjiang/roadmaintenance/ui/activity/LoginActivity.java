package com.jinjiang.roadmaintenance.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
import com.jinjiang.roadmaintenance.data.UserInfo;
import com.jinjiang.roadmaintenance.model.NetWorkRequest;
import com.jinjiang.roadmaintenance.model.UIDataListener;
import com.jinjiang.roadmaintenance.ui.view.DialogProgress;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.jinjiang.roadmaintenance.utils.ACache;
import com.jinjiang.roadmaintenance.utils.Uri;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements UIDataListener {

    @BindView(R.id.login_userName)
    EditText mUserName;
    @BindView(R.id.login_psw)
    EditText mPsw;
    private NetWorkRequest request;
    private Dialog dialog;
    private ACache mAcache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    protected void setAnim() {
    }

    @Override
    protected void initUI() {
        mAcache = ACache.get(LoginActivity.this);
        dialog = DialogProgress.createLoadingDialog(LoginActivity.this, "", this);
    }

    @Override
    protected void initData() {
        request = new NetWorkRequest(LoginActivity.this, this);


    }
    @OnClick({R.id.login_loginBt, R.id.login_modifypsw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_loginBt:
                String username = mUserName.getText().toString();
                String psw = mPsw.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    showToast("请输入用户名！");
                    return;
                }
                if (TextUtils.isEmpty(psw)) {
                    showToast("请输入密码！");
                    return;
                }
                Map map = new HashMap();
                map.put("userTel", username);
                map.put("userPass", psw);
                request.doPostRequest(0, true, Uri.loginUrl, map);
                break;
            case R.id.login_modifypsw:
                startActivity(new Intent(LoginActivity.this,modifyPswActivity.class));
                break;
        }
    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (data != null) {
            UserInfo userInfo = JSON.parseObject(data.toString(), UserInfo.class);
            mAcache.put("UserInfo", userInfo);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        } else {
            showToast("登录失败！");
        }
    }

    @Override
    public void showToast(String message) {
        myToast.toast(LoginActivity.this, message);
    }

    @Override
    public void showDialog() {
        if (dialog != null && !isFinishing())
            dialog.show();
    }

    @Override
    public void dismissDialog() {
        if (dialog != null && !isFinishing())
            dialog.dismiss();
    }

    @Override
    public void onError(String errorCode, String errorMessage) {
        showToast(errorMessage);
    }

    @Override
    public void cancelRequest() {
        request.CancelPost();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
