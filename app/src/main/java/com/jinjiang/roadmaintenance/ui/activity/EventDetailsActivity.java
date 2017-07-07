package com.jinjiang.roadmaintenance.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
import com.jinjiang.roadmaintenance.data.Task;
import com.jinjiang.roadmaintenance.data.TaskDetails;
import com.jinjiang.roadmaintenance.data.TaskState;
import com.jinjiang.roadmaintenance.data.UserInfo;
import com.jinjiang.roadmaintenance.model.NetWorkRequest;
import com.jinjiang.roadmaintenance.model.UIDataListener;
import com.jinjiang.roadmaintenance.ui.view.DialogProgress;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.jinjiang.roadmaintenance.utils.ACache;
import com.jinjiang.roadmaintenance.utils.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 病害详情
 */
public class EventDetailsActivity extends BaseActivity implements UIDataListener{


    @BindView(R.id.eventdetails_num)
    TextView mNum;
    @BindView(R.id.eventdetails_name)
    TextView mName;
    @BindView(R.id.eventdetails_roadName)
    TextView mRoadName;
    @BindView(R.id.eventdetails_date)
    TextView mDate;
    @BindView(R.id.eventdetails_long)
    EditText mLong;
    @BindView(R.id.eventdetails_wide)
    EditText mWide;
    @BindView(R.id.eventdetails_area)
    EditText mArea;
    private Task mTask;
    private ACache mAcache;
    private Dialog dialog;
    private NetWorkRequest request;
    private UserInfo userInfo;
    private TaskDetails mTaskDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_event_details;
    }

    @Override
    protected void initUI() {
        Intent intent = getIntent();
        if (intent.hasExtra("Task")){
            mTask = (Task) intent.getSerializableExtra("Task");
        }
        mAcache = ACache.get(EventDetailsActivity.this);
        dialog = DialogProgress.createLoadingDialog(EventDetailsActivity.this, "", this);
        request = new NetWorkRequest(EventDetailsActivity.this, this);
        userInfo = (UserInfo) mAcache.getAsObject("UserInfo");
        if (userInfo == null || TextUtils.isEmpty(userInfo.getUserId())) {
            myToast.toast(EventDetailsActivity.this, "登录状态已过期，请重新登录！");
            startActivity(new Intent(EventDetailsActivity.this, LoginActivity.class));
            finish();
        }

        if (mTask!=null){

            Map map = new HashMap();
            map.put("userId", userInfo.getUserId());
            map.put("appSid", userInfo.getAppSid());
            JSONObject object = new JSONObject();
            object.put("workOrderId", mTask.getWorkOrderId());
            object.put("taskId", mTask.getTaskId());
            map.put("body", object.toJSONString());
            request.doPostRequest(0, true, Uri.getOrderWork, map);
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.eventdetails_back, R.id.eventdetails_map})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.eventdetails_back:
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
                break;
            case R.id.eventdetails_map:
                startActivity(new Intent(EventDetailsActivity.this,NavigationActivity.class));
                break;
        }
    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (code == 0) {
            if (data != null) {
                mTaskDetails = JSON.parseObject(data.toString(), new TypeReference<TaskDetails>() {
                });
                if (mTaskDetails != null) {
                }
            }
        }
    }


    @Override
    public void showToast(String message) {
        myToast.toast(EventDetailsActivity.this, message);
    }

    @Override
    public void showDialog() {
        if (dialog != null)
            dialog.show();
    }

    @Override
    public void dismissDialog() {
        if (dialog != null)
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
