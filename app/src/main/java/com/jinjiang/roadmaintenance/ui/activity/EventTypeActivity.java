package com.jinjiang.roadmaintenance.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
import com.jinjiang.roadmaintenance.data.EventAttr;
import com.jinjiang.roadmaintenance.data.EventType;
import com.jinjiang.roadmaintenance.data.EventTypeBase;
import com.jinjiang.roadmaintenance.data.UserInfo;
import com.jinjiang.roadmaintenance.model.NetWorkRequest;
import com.jinjiang.roadmaintenance.model.UIDataListener;
import com.jinjiang.roadmaintenance.ui.view.DialogProgress;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.jinjiang.roadmaintenance.utils.ACache;
import com.jinjiang.roadmaintenance.utils.Uri;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;

public class EventTypeActivity extends BaseActivity implements UIDataListener {

    @BindView(R.id.eventType_tv)
    TextView mTypeTv;
    @BindView(R.id.eventtype_content)
    EditText mContent;
    @BindView(R.id.eventtype_name1)
    TextView mName1;
    @BindView(R.id.eventtype_value1)
    EditText mValue1;
    @BindView(R.id.eventtype_attr1)
    LinearLayout mAttr1;
    @BindView(R.id.eventtype_name2)
    TextView mName2;
    @BindView(R.id.eventtype_value2)
    EditText mValue2;
    @BindView(R.id.eventtype_attr2)
    LinearLayout mAttr2;
    @BindView(R.id.eventtype_name3)
    TextView mName3;
    @BindView(R.id.eventtype_value3)
    EditText mValue3;
    @BindView(R.id.eventtype_attr3)
    LinearLayout mAttr3;
    private ACache mAcache;
    private Dialog dialog;
    private NetWorkRequest request;
    private UserInfo userInfo;
    private ArrayList<EventType> mEventTypeList;
    private ArrayList<EventAttr> mEventAttrList;
    private OptionPicker picker_Type;
    private int eventTypeposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_event_type;
    }

    @Override
    protected void initUI() {
        mAcache = ACache.get(EventTypeActivity.this);
        dialog = DialogProgress.createLoadingDialog(EventTypeActivity.this, "", this);
        request = new NetWorkRequest(EventTypeActivity.this, this);

        userInfo = (UserInfo) mAcache.getAsObject("UserInfo");

        if (userInfo == null || TextUtils.isEmpty(userInfo.getUserId())) {
            myToast.toast(EventTypeActivity.this, "登录状态已过期，请重新登录！");
            startActivity(new Intent(EventTypeActivity.this, LoginActivity.class));
            EventTypeActivity.this.finish();
        }
    }

    @Override
    protected void initData() {
        Map map = new HashMap();
        map.put("userId", userInfo.getUserId());
        map.put("appSid", userInfo.getAppSid());
        map.put("orderType", "1");
        request.doPostRequest(0, true, Uri.getDiseaseType, map);

        mValue3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    String m1 = mValue1.getText().toString();
                    String m2 = mValue2.getText().toString();
                    if (!TextUtils.isEmpty(m1)&&!TextUtils.isEmpty(m2)){
                        float m3 = Float.parseFloat(m1)*Float.parseFloat(m2);
                        DecimalFormat fnum = new DecimalFormat("##0.00");
                        String dd=fnum.format(m3);
                        mValue3.setText(dd);
                    }
                }
            }
        });
    }

    @OnClick({R.id.eventtype_back, R.id.eventadd_eventType_know, R.id.eventType_tv_ll, R.id.eventtype_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.eventtype_back:
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
                break;
            case R.id.eventadd_eventType_know:
                break;
            case R.id.eventType_tv_ll:
                picker_Type.show();
                break;
            case R.id.eventtype_send:
                int size = mEventAttrList.size();
                if (size==1){
                    String s1 = mValue1.getText().toString();
                    if (TextUtils.isEmpty(s1)||s1.equals("0")){
                        showToast("请输入长度！");
                        return;
                    }
                    mEventAttrList.get(0).setDefaultVal(s1);
                }else if (size==2){
                    String s1 = mValue1.getText().toString();
                    String s2 = mValue2.getText().toString();
                    if (TextUtils.isEmpty(s1)||s1.equals("0")){
                        showToast("请输入长度！");
                        return;
                    }
                    if (TextUtils.isEmpty(s2)||s2.equals("0")){
                        showToast("请输入宽度！");
                        return;
                    }
                    mEventAttrList.get(0).setDefaultVal(s1);
                    mEventAttrList.get(1).setDefaultVal(s2);
                }else if (size==3){
                    String s1 = mValue1.getText().toString();
                    String s2 = mValue2.getText().toString();
                    String s3 = mValue3.getText().toString();
                    if (TextUtils.isEmpty(s1)||s1.equals("0")){
                        showToast("请输入长度！");
                        return;
                    }
                    if (TextUtils.isEmpty(s2)||s2.equals("0")){
                        showToast("请输入宽度！");
                        return;
                    }
                    if (TextUtils.isEmpty(s3)||s3.equals("0")){
                        showToast("请输入面积！");
                        return;
                    }
                    mEventAttrList.get(0).setDefaultVal(s1);
                    mEventAttrList.get(1).setDefaultVal(s2);
                    mEventAttrList.get(2).setDefaultVal(s3);
                }
                LogUtils.d(mEventAttrList);
                EventTypeBase base = new EventTypeBase();
                base.setEventAttrsList(mEventAttrList);
                base.setEventType(mEventTypeList.get(eventTypeposition));
                Intent intent = new Intent();
                intent.putExtra("EventTypeBase",base);
                setResult(Activity.RESULT_OK,intent);
                finish();
                break;
        }
    }


    /**
     * 初始化类型选择器
     */
    private void initOptionPicker(ArrayList<String> type_list) {

        picker_Type = null;
        picker_Type = new OptionPicker(EventTypeActivity.this, type_list);
        picker_Type.setOffset(1);
        picker_Type.setCycleDisable(true);
        picker_Type.setSelectedIndex(1);
        picker_Type.setTextSize(15);
        picker_Type.setLineColor(EventTypeActivity.this.getResources().getColor(R.color.blue));
        picker_Type.setTextColor(EventTypeActivity.this.getResources().getColor(R.color.text_black));
        picker_Type.setCancelText("取消");
        picker_Type.setCancelTextColor(EventTypeActivity.this.getResources().getColor(R.color.gray_deep));
        picker_Type.setSubmitText("确定");
        picker_Type.setSubmitTextColor(EventTypeActivity.this.getResources().getColor(R.color.blue));
        picker_Type.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int position, String option) {
                eventTypeposition = position;
                mTypeTv.setText(option);
                Map map = new HashMap();
                map.put("userId", userInfo.getUserId());
                map.put("appSid", userInfo.getAppSid());
                map.put("id", mEventTypeList.get(position).getId());
                request.doPostRequest(1, true, Uri.getDiseaseAttr, map);
            }
        });
    }

    @Override
    public void loadDataFinish(int code, Object data) {

        if (code == 0) {
            if (data != null) {
                mEventTypeList = JSON.parseObject(data.toString(), new TypeReference<ArrayList<EventType>>() {
                });
                if (mEventTypeList != null && mEventTypeList.size() > 0) {
                    ArrayList<String> type_list = new ArrayList<>();
                    for (EventType e : mEventTypeList) {
                        type_list.add(e.getName());
                    }
                    initOptionPicker(type_list);
                    mTypeTv.setText(mEventTypeList.get(0).getName());
                    Map map = new HashMap();
                    map.put("userId", userInfo.getUserId());
                    map.put("appSid", userInfo.getAppSid());
                    map.put("id", mEventTypeList.get(0).getId());
                    request.doPostRequest(1, true, Uri.getDiseaseAttr, map);
                }
            }
        } else if (code == 1) {
            if (data != null) {
                mEventAttrList = JSON.parseObject(data.toString(), new TypeReference<ArrayList<EventAttr>>() {
                });
                if (mEventAttrList != null && mEventAttrList.size() > 0) {
                    if (mEventAttrList.size()==1){
                        mName1.setText(mEventAttrList.get(0).getName());
                        mValue1.setText(mEventAttrList.get(0).getDefaultVal());
                        mAttr2.setVisibility(View.GONE);
                        mAttr3.setVisibility(View.GONE);
                    }else if (mEventAttrList.size()==2){
                        mName1.setText(mEventAttrList.get(0).getName());
                        mValue1.setText(mEventAttrList.get(0).getDefaultVal());
                        mName2.setText(mEventAttrList.get(1).getName());
                        mValue2.setText(mEventAttrList.get(1).getDefaultVal());
                        mAttr3.setVisibility(View.GONE);
                    }else if (mEventAttrList.size()==3){
                        mName1.setText(mEventAttrList.get(0).getName());
                        mValue1.setText(mEventAttrList.get(0).getDefaultVal());
                        mName2.setText(mEventAttrList.get(1).getName());
                        mValue2.setText(mEventAttrList.get(1).getDefaultVal());
                        mName3.setText(mEventAttrList.get(2).getName());
                        mValue3.setText(mEventAttrList.get(2).getDefaultVal());
                    }
                }
            }
        }

    }

    @Override
    public void showToast(String message) {
        myToast.toast(EventTypeActivity.this, message);
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
