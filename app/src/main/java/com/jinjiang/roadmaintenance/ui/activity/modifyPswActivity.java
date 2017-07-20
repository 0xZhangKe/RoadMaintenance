package com.jinjiang.roadmaintenance.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
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

public class modifyPswActivity extends BaseActivity implements UIDataListener {

    @BindView(R.id.psw_phone)
    EditText mPhone;
    @BindView(R.id.eventadd_yanzhengEt)
    EditText mYanzhengEt;
    @BindView(R.id.psw_yanzheng)
    TextView mYanzheng;
    @BindView(R.id.psw_new)
    EditText mNew;
    private ACache mAcache;
    private Dialog dialog;
    private NetWorkRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_modify_psw;
    }

    @Override
    protected void initUI() {
        mAcache = ACache.get(modifyPswActivity.this);
        dialog = DialogProgress.createLoadingDialog(modifyPswActivity.this, "", this);
        request = new NetWorkRequest(modifyPswActivity.this, this);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.psw_back, R.id.psw_yanzheng, R.id.psw_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.psw_back:
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
                break;
            case R.id.psw_yanzheng:
                String phone = mPhone.getText().toString();
                if (TextUtils.isEmpty(phone)){
                    showToast("请输入手机号！");
                    return;
                }
                Map map =new HashMap();
                JSONObject object = new JSONObject();
                object.put("userTel", phone);
                object.put("codeType", "2");
                map.put("body",object.toJSONString());
                request.doPostRequest(0,true, Uri.sendSms,map);
                CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(modifyPswActivity.this,mYanzheng, 60000, 1000);
                mCountDownTimerUtils.start();
                break;
            case R.id.psw_send:
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
                String newpsd = mNew.getText().toString();
                if (TextUtils.isEmpty(newpsd)){
                    showToast("请输入新密码！");
                    return;
                }
                Map map2 =new HashMap();
                JSONObject object2 = new JSONObject();
                object2.put("userTel", phone2);
                object2.put("mobileCode", yanzheng);
                object2.put("newUserPass", newpsd);
                map2.put("body",object2.toJSONString());
                request.doPostRequest(1,true, Uri.updateUserPass,map2);
                break;
        }
    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (data != null) {
            if (code==0){
                showToast("获取成功！");
            }else if (code==1){
                showToast("密码重置成功,请登录！");
                finish();
            }

        }
    }

    @Override
    public void showToast(String message) {
        myToast.toast(modifyPswActivity.this, message);
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
