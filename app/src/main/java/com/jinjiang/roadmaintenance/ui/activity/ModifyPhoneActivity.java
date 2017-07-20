package com.jinjiang.roadmaintenance.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
import com.jinjiang.roadmaintenance.data.UserInfo;
import com.jinjiang.roadmaintenance.model.NetWorkRequest;
import com.jinjiang.roadmaintenance.model.UIDataListener;
import com.jinjiang.roadmaintenance.ui.view.DialogProgress;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.jinjiang.roadmaintenance.utils.ACache;
import com.jinjiang.roadmaintenance.utils.CountDownTimerUtils;
import com.jinjiang.roadmaintenance.utils.Uri;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ModifyPhoneActivity extends BaseActivity implements UIDataListener {

    @BindView(R.id.modifyphone_phone)
    EditText mPhone;
    @BindView(R.id.eventadd_yanzhengEt)
    EditText mYanzhengEt;
    @BindView(R.id.modifyphone_yanzheng)
    TextView mYanzheng;
    private Dialog dialog;
    private NetWorkRequest request;
    private ACache mAcache;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_modify_phone;
    }

    @Override
    protected void initUI() {
        dialog = DialogProgress.createLoadingDialog(ModifyPhoneActivity.this, "", this);
        request = new NetWorkRequest(ModifyPhoneActivity.this, this);

    }

    @Override
    protected void initData() {
        mAcache = ACache.get(ModifyPhoneActivity.this);
        userInfo = (UserInfo) mAcache.getAsObject("UserInfo");

        if (userInfo == null || TextUtils.isEmpty(userInfo.getUserId())) {
            myToast.toast(ModifyPhoneActivity.this, "登录状态已过期，请重新登录！");
            startActivity(new Intent(ModifyPhoneActivity.this, LoginActivity.class));
            ModifyPhoneActivity.this.finish();
        }
    }

    @OnClick({R.id.modifyphone_back, R.id.modifyphone_send, R.id.modifyphone_yanzheng})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.modifyphone_back:
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
                break;
            case R.id.modifyphone_yanzheng:
                String phone = mPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    showToast("请输入手机号！");
                    return;
                }
                Map map = new HashMap();
                map.put("userId", userInfo.getUserId());
                map.put("appSid", userInfo.getAppSid());
                JSONObject object = new JSONObject();
                object.put("userTel", phone);
                object.put("codeType", "2");
                map.put("body", object.toJSONString());
                request.doPostRequest(0, true, Uri.sendSms2, map);
                CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(ModifyPhoneActivity.this, mYanzheng, 60000, 1000);
                mCountDownTimerUtils.start();
                break;
            case R.id.modifyphone_send:
                String phone2 = mPhone.getText().toString();
                if (TextUtils.isEmpty(phone2)){
                    showToast("请输入手机号！");
                    return;
                }
                String yanzheng = mYanzhengEt.getText().toString();
                if (TextUtils.isEmpty(yanzheng)){
                    showToast("请输入验证码！");
                    return;
                }
                Map map2 = new HashMap();
                map2.put("userId", userInfo.getUserId());
                map2.put("appSid", userInfo.getAppSid());
                JSONObject object2 = new JSONObject();
                object2.put("userTel", phone2);
                object2.put("mobileCode", yanzheng);
                map2.put("body", object2.toJSONString());
                request.doPostRequest(1, true, Uri.updateUserTel, map2);
                break;
        }
    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (data != null) {
            if (code == 0) {
                showToast("获取成功！");
            } else if (code == 1) {
                showToast("修改成功！");
                finish();
            }

        }
    }

    @Override
    public void showToast(String message) {
        myToast.toast(ModifyPhoneActivity.this, message);
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
}
