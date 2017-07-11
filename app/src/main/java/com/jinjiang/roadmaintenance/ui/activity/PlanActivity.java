package com.jinjiang.roadmaintenance.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
import com.jinjiang.roadmaintenance.data.Plan;
import com.jinjiang.roadmaintenance.data.UserInfo;
import com.jinjiang.roadmaintenance.model.NetWorkRequest;
import com.jinjiang.roadmaintenance.model.UIDataListener;
import com.jinjiang.roadmaintenance.ui.view.DialogProgress;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.jinjiang.roadmaintenance.utils.ACache;
import com.jinjiang.roadmaintenance.utils.Uri;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PlanActivity extends BaseActivity implements UIDataListener {

    @BindView(R.id.plan_listView)
    ListView mListView;
    @BindView(R.id.plan_editText)
    EditText mEditText;
    private ACache mAcache;
    private Dialog dialog;
    private NetWorkRequest request;
    private UserInfo userInfo;
    private int orderType;
    private ArrayList<Plan> mPlanList;
    private ArrayList<Plan> mSelectPlanList;
    private CommonAdapter<Plan> mPlanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_plan;
    }

    @Override
    protected void initUI() {
        mPlanList = new ArrayList<>();
        mSelectPlanList = new ArrayList<>();
        mAcache = ACache.get(PlanActivity.this);
        dialog = DialogProgress.createLoadingDialog(PlanActivity.this, "", this);
        request = new NetWorkRequest(PlanActivity.this, this);

        userInfo = (UserInfo) mAcache.getAsObject("UserInfo");

        if (userInfo == null || TextUtils.isEmpty(userInfo.getUserId())) {
            myToast.toast(PlanActivity.this, "登录状态已过期，请重新登录！");
            startActivity(new Intent(PlanActivity.this, LoginActivity.class));
            PlanActivity.this.finish();
        }
    }

    @Override
    protected void initData() {

        orderType = getIntent().getIntExtra("orderType", 0);

        Map map = new HashMap();
        map.put("userId", userInfo.getUserId());
        map.put("appSid", userInfo.getAppSid());
        map.put("orderType", orderType + "");
        request.doPostRequest(0, true, Uri.getMaintainFun, map);
    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (code == 0) {
            if (data != null) {
                mPlanList = JSON.parseObject(data.toString(), new TypeReference<ArrayList<Plan>>() {
                });
                if (mPlanList != null && mPlanList.size() > 0) {
                    Plan plan = new Plan();
                    plan.setFunName("其他");
                    plan.setId("qita");
                    mPlanList.add(plan);
                    mPlanAdapter = new CommonAdapter<Plan>(PlanActivity.this, R.layout.item_plan_add, mPlanList) {

                        @Override
                        protected void convert(ViewHolder viewHolder, final Plan item, int position) {
                            viewHolder.setText(R.id.item_name, item.getFunName());
                            viewHolder.setOnClickListener(R.id.item_explan, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new AlertDialog.Builder(PlanActivity.this).setTitle("提示")
                                            .setMessage(item.getFunDetail())
                                            .show();
                                }
                            });
                            if (item.getId().equals("qita")) {
                                viewHolder.setVisible(R.id.item_explan, false);
                            } else {
                                viewHolder.setVisible(R.id.item_explan, true);
                            }
                            ((CheckBox) (viewHolder.getView(R.id.item_check))).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {
                                        if (!mSelectPlanList.contains(item))
                                            mSelectPlanList.add(item);
                                        if (item.getId().equals("qita"))
                                            mEditText.setVisibility(View.VISIBLE);
                                    } else {
                                        if (mSelectPlanList.contains(item))
                                            mSelectPlanList.remove(item);
                                        if (item.getId().equals("qita"))
                                            mEditText.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    };

                    mListView.setAdapter(mPlanAdapter);
                }
            }
        }
    }

    @Override
    public void showToast(String message) {
        myToast.toast(PlanActivity.this, message);
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

    @OnClick({R.id.plan_back, R.id.plan_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.plan_back:
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
                break;
            case R.id.plan_send:

                if (mSelectPlanList.size() == 0) {
                    showToast("请选择维修方案！");
                    return;
                }
                for (Plan p : mSelectPlanList) {
                    if (p.getId().equals("qita")) {
                        if (TextUtils.isEmpty(mEditText.getText().toString())) {
                            showToast("请输入其他方案！");
                            return;
                        } else {
                            p.setFunDetail(mEditText.getText().toString());
                        }
                    }
                }

                Intent intent = new Intent();
                intent.putExtra("planList", mSelectPlanList);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }
}
