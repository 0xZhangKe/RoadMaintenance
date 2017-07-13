package com.jinjiang.roadmaintenance.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
import com.jinjiang.roadmaintenance.data.EventAttr;
import com.jinjiang.roadmaintenance.data.EventTypeBase;
import com.jinjiang.roadmaintenance.data.MessageEvent;
import com.jinjiang.roadmaintenance.data.Plan;
import com.jinjiang.roadmaintenance.data.Task;
import com.jinjiang.roadmaintenance.data.TaskDetails;
import com.jinjiang.roadmaintenance.data.UserInfo;
import com.jinjiang.roadmaintenance.model.NetWorkRequest;
import com.jinjiang.roadmaintenance.model.UIDataListener;
import com.jinjiang.roadmaintenance.ui.view.DialogProgress;
import com.jinjiang.roadmaintenance.ui.view.ListViewForScrollView;
import com.jinjiang.roadmaintenance.ui.view.MyGridView;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.jinjiang.roadmaintenance.utils.ACache;
import com.jinjiang.roadmaintenance.utils.ScreenUtils;
import com.jinjiang.roadmaintenance.utils.Uri;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 病害详情
 */
public class EventDetailsActivity extends BaseActivity implements UIDataListener {


    @BindView(R.id.eventdetails_title)
    TextView eventdetailsTitle;
    @BindView(R.id.eventdetails_grid_tupian)
    MyGridView mGridTupian;
    @BindView(R.id.eventdetails_eventId)
    TextView mEventId;
    @BindView(R.id.eventdetails_saveDate)
    TextView mSaveDate;
    @BindView(R.id.eventdetails_savePerson)
    TextView mSavePerson;
    @BindView(R.id.eventdetails_roadName)
    EditText mRoadName;
    @BindView(R.id.eventdetails_location)
    TextView mLocation;
    @BindView(R.id.eventdetails_roadType)
    TextView mRoadType;
    @BindView(R.id.eventdetails_driverwayType_tv)
    TextView mDriverwayTypeTv;
    @BindView(R.id.eventdetails_radio1)
    RadioButton mRadio1;
    @BindView(R.id.eventdetails_radio2)
    RadioButton mRadio2;
    @BindView(R.id.eventdetails_driverwayType_rg)
    RadioGroup mDriverwayTypeRg;
    @BindView(R.id.eventdetails_allArea)
    TextView mAllArea;
    @BindView(R.id.eventdetails_eventtype_listview)
    ListViewForScrollView mEventtypeListview;
    @BindView(R.id.eventdetails_plansTv)
    TextView mPlansTv;
    @BindView(R.id.eventdetails_plansAdd_listview)
    ListViewForScrollView mPlansAddListview;
    @BindView(R.id.eventdetails_planTime)
    EditText mPlanTime;
    @BindView(R.id.eventdetails_planCost)
    EditText mPlanCost;
    @BindView(R.id.eventdetails_approvalStateTv)
    TextView mApprovalStateTv;
    @BindView(R.id.eventdetails_approvalStateLv)
    ListViewForScrollView mApprovalStateLv;
    @BindView(R.id.eventdetails_remark)
    EditText mRemark;
    @BindView(R.id.eventdetails_eventType_add)
    ImageView mEventTypeAdd;
    @BindView(R.id.eventdetails_plansAdd)
    ImageView mPlansAdd;
    @BindView(R.id.eventdetails_approvalStateLl)
    LinearLayout mApprovalStateLl;
    @BindView(R.id.eventdetails_confirm1)
    RadioButton mConfirm1;
    @BindView(R.id.eventdetails_confirm2)
    RadioButton mConfirm2;
    @BindView(R.id.eventdetails_confirm_rg)
    RadioGroup mConfirmRg;
    @BindView(R.id.eventdetails_remark_ll)
    LinearLayout mRemarkLl;
    @BindView(R.id.eventdetails_send)
    TextView mSend;
    @BindView(R.id.eventdetails_drivertype_ll)
    LinearLayout mDrivertypeLl;
    @BindView(R.id.eventdetails_eventtype_ll)
    LinearLayout mEventtypeLl;
    private Task mTask;
    private ACache mAcache;
    private Dialog dialog;
    private NetWorkRequest request;
    private UserInfo userInfo;
    private TaskDetails mTaskDetails;
    private int userRole;
    private int OrderStatus;
    private CommonAdapter<String> mGridTupianAdapter;
    private CommonAdapter<TaskDetails.DiseaseMsgDtosBean> adapter_eventtype;
    private CommonAdapter<Plan> adapter_plan;
    private CommonAdapter<TaskDetails.TaskInfosBean> adapter_approval;
    private int OrderType;
    private double totalArea = 0;

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
        if (intent.hasExtra("Task")) {
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
        userRole = userInfo.getUserRole();

    }

    @Override
    protected void initData() {
        if (mTask != null) {

            Map map = new HashMap();
            map.put("userId", userInfo.getUserId());
            map.put("appSid", userInfo.getAppSid());
            JSONObject object = new JSONObject();
            object.put("workOrderId", mTask.getWorkOrderId());
            object.put("taskId", mTask.getTaskId());
            map.put("body", object.toJSONString());
            request.doPostRequest(0, true, Uri.getDiseaseInfos, map);
        }
    }

    /**
     * 设置界面数据
     */
    private void setUiData(TaskDetails td) {


        TaskDetails.WorkOrderMsgDtoBean wm = td.getWorkOrderMsgDto();
        OrderStatus = wm.getOrderStatus();
        OrderType = wm.getOrderType();
        totalArea = wm.getArea();
        if (OrderType==5){
            mDrivertypeLl.setVisibility(View.GONE);
            mEventtypeLl.setVisibility(View.GONE);
        }else if (OrderType==4||OrderType==3){
            mDrivertypeLl.setVisibility(View.GONE);
        }
        if (OrderStatus == 2) {//待确认
            mRemark.setText(wm.getDetail());
            mPlansAdd.setVisibility(View.VISIBLE);
            mEventTypeAdd.setVisibility(View.VISIBLE);
            mPlanTime.setEnabled(true);
            mPlanCost.setEnabled(true);
            mRemark.setEnabled(true);
            mApprovalStateLl.setVisibility(View.GONE);
        } else if (OrderStatus == 3) {//技术员审批--不处理
            mRemark.setEnabled(false);
            mConfirmRg.setVisibility(View.GONE);
            mSend.setVisibility(View.GONE);
        } else if (OrderStatus == 4) {//需处理(不会出现4)
            mRemark.setEnabled(false);
            mConfirmRg.setVisibility(View.GONE);
            mSend.setVisibility(View.GONE);
        } else if (OrderStatus == 5) {//待批复
            mRadio1.setText("情况属实");
            mRadio2.setText("情况不属实");
            mRemark.setEnabled(true);
            mRemark.setHint("请输入处理意见");
        } else if (OrderStatus == 6) {// <20m未施工

        } else if (OrderStatus == 7) {//监理审核否--重新下单
        } else if (OrderStatus == 8) {//监理审核属实--一级业主批复
            mRadio1.setText("情况属实");
            mRadio2.setText("情况不属实");
            mRemark.setEnabled(true);
            mRemark.setHint("请输入处理意见");
        } else if (OrderStatus == 9) {//一级业主审核否--重新下单
        } else if (OrderStatus == 10) {//一级业主审核属实--二级业主批复
            mRadio1.setText("情况属实");
            mRadio2.setText("情况不属实");
            mRemark.setEnabled(true);
            mRemark.setHint("请输入处理意见");
        } else if (OrderStatus == 11) {//二级业主审核否--重新下单
        } else if (OrderStatus == 12) {//二级业主审核属实--三级业主批复
            mRadio1.setText("情况属实");
            mRadio2.setText("情况不属实");
            mRemark.setEnabled(true);
            mRemark.setHint("请输入处理意见");
        } else if (OrderStatus == 13) {//三级业主审核否--重新下单
        } else if (OrderStatus == 14) {//三级业主审核属实-->20m未施工
        }


        mEventId.setText(td.getTaskId());
        mSaveDate.setText(td.getTaskCreateTime());
        mRoadName.setText(wm.getLocationDesc());
        mLocation.setText(wm.getLocationDesc());
        mRoadType.setText(wm.getOrderTypeName());
        mDriverwayTypeTv.setText(wm.getLineTypeName());
        mAllArea.setText("总面积" + wm.getArea() + "m²");
        mPlanTime.setText(wm.getTimePlan() + "");
        mPlanCost.setText(wm.getMoneyPlan() + "");

//        if (wm.getLineType() == 1) {
//            mRadio1.setChecked(true);
//            mRadio2.setChecked(false);
//        } else {
//            mRadio1.setChecked(false);
//            mRadio2.setChecked(true);
//        }
        mSavePerson.setText(wm.getUserId());
        mApprovalStateTv.setText(td.getTaskName());

        setGridAdapter(wm.getPicUrls());
        setEventtypeListAdapter(td.getDiseaseMsgDtos());
        setplanAdapter(td.getPlanFuns());
        setApprovalAdapter(td.getTaskInfos());
    }

    /**
     * 病害类型
     *
     * @param list
     */
    private void setEventtypeListAdapter(final List<TaskDetails.DiseaseMsgDtosBean> list) {
        adapter_eventtype = new CommonAdapter<TaskDetails.DiseaseMsgDtosBean>(EventDetailsActivity.this, R.layout.item_eventtype_add, list) {
            @Override
            protected void convert(ViewHolder viewHolder, TaskDetails.DiseaseMsgDtosBean item, final int position) {
                viewHolder.setText(R.id.item_name, item.getDiseaseTypeName());
                ArrayList<TaskDetails.DiseaseMsgDtosBean.DiseaseAttrMsgDtosBean> attrlist = (ArrayList<TaskDetails.DiseaseMsgDtosBean.DiseaseAttrMsgDtosBean>) item.getDiseaseAttrMsgDtos();
                if (attrlist != null && attrlist.size() > 0) {
                    if (attrlist.size() == 1) {
                        viewHolder.setText(R.id.item_attr, attrlist.get(0).getValue() + "m");
                    } else {
                        viewHolder.setText(R.id.item_attr, attrlist.get(0).getValue() + "m" + "*" + attrlist.get(1).getValue() + "m");
                    }
                }
                if (OrderStatus == 2) {//可编辑
                    viewHolder.setOnClickListener(R.id.item_del, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            list.remove(position);
                            for (TaskDetails.DiseaseMsgDtosBean.DiseaseAttrMsgDtosBean b : list.get(position).getDiseaseAttrMsgDtos()) {
                                if (b.getDiseaseAttrName().contains("面积")) {
                                    totalArea = totalArea - Double.parseDouble(b.getValue());
                                }
                            }
                            adapter_eventtype.notifyDataSetChanged();
                        }
                    });
                } else {
                    viewHolder.setVisible(R.id.item_del, false);
                }
            }
        };
        mEventtypeListview.setAdapter(adapter_eventtype);
    }

    /**
     * 施工计划
     *
     * @param list
     */
    private void setplanAdapter(final List<Plan> list) {
        adapter_plan = new CommonAdapter<Plan>(EventDetailsActivity.this, R.layout.item_eventtype_add, list) {
            @Override
            protected void convert(ViewHolder viewHolder, Plan item, final int position) {
                viewHolder.setText(R.id.item_name, item.getFunName());
                viewHolder.setText(R.id.item_attr, "");
                if (OrderStatus == 2) {
                    viewHolder.setOnClickListener(R.id.item_del, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            list.remove(position);
                            adapter_plan.notifyDataSetChanged();
                        }
                    });
                } else {
                    viewHolder.setVisible(R.id.item_del, false);
                }
            }
        };
        mPlansAddListview.setAdapter(adapter_plan);
    }

    /**
     * 审批
     *
     * @param list
     */
    private void setApprovalAdapter(final List<TaskDetails.TaskInfosBean> list) {
        adapter_approval = new CommonAdapter<TaskDetails.TaskInfosBean>(EventDetailsActivity.this, R.layout.item_approval_list, list) {
            @Override
            protected void convert(ViewHolder viewHolder, TaskDetails.TaskInfosBean item, final int position) {
                viewHolder.setText(R.id.item_name, item.getUserName());
                viewHolder.setText(R.id.item_desc, item.getDetail());
                viewHolder.setText(R.id.item_time, item.getEndTime());
                if (position == 0) {
                    viewHolder.setBackgroundRes(R.id.item_dot, R.drawable.gre_dot_blue_back);
                    TextView v = (TextView) viewHolder.getView(R.id.item_line);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    params.setMargins(0, ScreenUtils.dp2px(EventDetailsActivity.this, 20), 0, 0);
                    v.setLayoutParams(params);
                } else {
                    viewHolder.setBackgroundRes(R.id.item_dot, R.drawable.gre_dot_back);
                    TextView v = viewHolder.getView(R.id.item_line);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    params.setMargins(0, 0, 0, 0);
                    v.setLayoutParams(params);
                }
            }
        };
        mApprovalStateLv.setAdapter(adapter_approval);
    }

    /**
     * 首行图片
     *
     * @param list
     */
    private void setGridAdapter(List<String> list) {
        mGridTupianAdapter = new CommonAdapter<String>(EventDetailsActivity.this, R.layout.item_addphoto_grid, list) {
            @Override
            protected void convert(ViewHolder viewHolder, final String item, int position) {
                Glide.with(EventDetailsActivity.this).load(item).into((ImageView) viewHolder.getView(R.id.item_addphoto_grid_img));
                viewHolder.setVisible(R.id.item_addphoto_grid_del, false);
            }
        };
        mGridTupian.setAdapter(mGridTupianAdapter);
    }

    @OnClick({R.id.eventdetails_back, R.id.eventdetails_save, R.id.eventdetails_eventType_add, R.id.eventdetails_plansAdd, R.id.eventdetails_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.eventdetails_back:
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
                break;
            case R.id.eventdetails_save:
                break;
            case R.id.eventdetails_eventType_add:
                Intent intent = new Intent(EventDetailsActivity.this, EventTypeActivity.class);
                startActivityForResult(intent, 105);
                break;
            case R.id.eventdetails_plansAdd:
                StringBuffer typeIds = new StringBuffer();
                if (mTaskDetails != null && mTaskDetails.getDiseaseMsgDtos() != null){
                    if (OrderType != 5) {
                        for (int i =0;i<mTaskDetails.getDiseaseMsgDtos().size();i++) {
                            if (i == 0) {
                                typeIds.append(mTaskDetails.getDiseaseMsgDtos().get(i).getDiseaseId());
                            } else {
                                typeIds.append("," + mTaskDetails.getDiseaseMsgDtos().get(i).getDiseaseId());
                            }
                        }
                    }
                }

                Intent intent2 = new Intent(EventDetailsActivity.this, PlanActivity.class);
                intent2.putExtra("orderType", OrderType);
                intent2.putExtra("diseaseTypeId", typeIds.toString());
                startActivityForResult(intent2, 106);
                break;
            case R.id.eventdetails_send:
                if (OrderStatus == 2) {
                    confirmTask1();
                } else if (OrderStatus == 5) {
                    confirmTask2();
                } else if (OrderStatus == 8) {
                    confirmTask3();
                } else if (OrderStatus == 10) {
                    confirmTask4();
                } else if (OrderStatus == 12) {
                    confirmTask5();
                }
                break;
        }
    }

    /**
     * 技术员确认信息
     */
    private void confirmTask1() {
        int deal = mConfirmRg.getCheckedRadioButtonId() == R.id.eventdetails_confirm1 ? 4 : 3;
        String planTime = mPlanTime.getText().toString();
        String planCost = mPlanCost.getText().toString();

        if (mTaskDetails.getPlanFuns() == null || mTaskDetails.getPlanFuns().size() == 0) {
            showToast("请选择修复方案！");
            return;
        }

        if (TextUtils.isEmpty(planTime) || Integer.parseInt(planTime) == 0) {
            showToast("请输入预计修复时间！");
            return;
        }
        if (TextUtils.isEmpty(planCost) || Double.parseDouble(planCost) == 0) {
            showToast("请输入预计修复费用！");
            return;
        }

        Map map = new HashMap();
        map.put("userId", userInfo.getUserId());
        map.put("appSid", userInfo.getAppSid());
        JSONObject object = new JSONObject();
        object.put("vacationApproved", deal);
        object.put("opinion", mRemark.getText().toString());
        object.put("taskId", mTaskDetails.getTaskId());
        object.put("roleCode", "5");
        object.put("workOrderId", mTaskDetails.getWorkOrderMsgDto().getWorkOrderId());
        map.put("body", object.toJSONString());

        if (deal == 4) {
            JSONObject object1 = new JSONObject();
            if (OrderType != 5) {
                object1.put("area", totalArea);

                JSONArray array = new JSONArray();
                JSONArray array2 = new JSONArray();
                for (TaskDetails.DiseaseMsgDtosBean e : mTaskDetails.getDiseaseMsgDtos()) {
                    JSONObject object2 = new JSONObject();
                    ArrayList<TaskDetails.DiseaseMsgDtosBean.DiseaseAttrMsgDtosBean> attrsList = (ArrayList<TaskDetails.DiseaseMsgDtosBean.DiseaseAttrMsgDtosBean>) e.getDiseaseAttrMsgDtos();
                    object2.put("diseaseType", e.getDiseaseType());
                    object2.put("detail", e.getDetail());
                    array.add(object2);
                    for (TaskDetails.DiseaseMsgDtosBean.DiseaseAttrMsgDtosBean a : attrsList) {
                        JSONObject object3 = new JSONObject();
                        object3.put("diseaseType", e.getDiseaseType());
                        object3.put("typeUnitId", a.getTypeUnitId());
                        object3.put("value", a.getValue());
                        array2.add(object3);
                    }
                }
                map.put("disease", array.toJSONString());
                map.put("diseaseAttr", array2.toJSONString());
            }
            StringBuffer plans = new StringBuffer();
            for (Plan p : mTaskDetails.getPlanFuns()) {
                if (!p.getId().equals("0")) {
                    if (plans.length() == 0) {
                        plans.append(p.getId());
                    } else {
                        plans.append("," + p.getId());
                    }
                } else {
                    object1.put("maintainDetailPlan", p.getOtherDesc());
                }
            }
            object1.put("workOrderId", mTaskDetails.getWorkOrderMsgDto().getWorkOrderId());
            object1.put("processInstanceId", mTaskDetails.getWorkOrderMsgDto().getProcessInstanceId());
            object1.put("orderType", OrderType);
            object1.put("maintainFunIds", plans.toString());
            object1.put("detail", mTaskDetails.getWorkOrderMsgDto().getDetail());
            object1.put("timePlan", mPlanTime.getText().toString());
            object1.put("moneyPlan", mPlanCost.getText().toString());
            map.put("workOrder", object1.toJSONString());
        }
        request.doPostRequest(1, true, Uri.techAffirmDiseaseInfo, map);
    }

    /**
     * 监理审批
     */
    private void confirmTask2() {
        int deal = mConfirmRg.getCheckedRadioButtonId() == R.id.eventdetails_confirm1 ? 8 : 7;
        Map map = new HashMap();
        map.put("userId", userInfo.getUserId());
        map.put("appSid", userInfo.getAppSid());
        JSONObject object = new JSONObject();
        object.put("vacationApproved", deal);
        object.put("opinion", mRemark.getText().toString());
        object.put("taskId", mTaskDetails.getTaskId());
        object.put("roleCode", "1");
        map.put("body", object.toJSONString());
        request.doPostRequest(2, true, Uri.official, map);
    }

    /**
     * 一级业主审批
     */
    private void confirmTask3() {
        int deal = mConfirmRg.getCheckedRadioButtonId() == R.id.eventdetails_confirm1 ? 10 : 9;
        Map map = new HashMap();
        map.put("userId", userInfo.getUserId());
        map.put("appSid", userInfo.getAppSid());
        JSONObject object = new JSONObject();
        object.put("vacationApproved", deal);
        object.put("opinion", mRemark.getText().toString());
        object.put("taskId", mTaskDetails.getTaskId());
        map.put("body", object.toJSONString());
        request.doPostRequest(3, true, Uri.official, map);
    }

    /**
     * 二级业主审批
     */
    private void confirmTask4() {
        int deal = mConfirmRg.getCheckedRadioButtonId() == R.id.eventdetails_confirm1 ? 12 : 11;
        Map map = new HashMap();
        map.put("userId", userInfo.getUserId());
        map.put("appSid", userInfo.getAppSid());
        JSONObject object = new JSONObject();
        object.put("vacationApproved", deal);
        object.put("opinion", mRemark.getText().toString());
        object.put("taskId", mTaskDetails.getTaskId());
        map.put("body", object.toJSONString());
        request.doPostRequest(4, true, Uri.official, map);
    }

    /**
     * 三级业主审批
     */
    private void confirmTask5() {
        int deal = mConfirmRg.getCheckedRadioButtonId() == R.id.eventdetails_confirm1 ? 14 : 13;
        Map map = new HashMap();
        map.put("userId", userInfo.getUserId());
        map.put("appSid", userInfo.getAppSid());
        JSONObject object = new JSONObject();
        object.put("vacationApproved", deal);
        object.put("opinion", mRemark.getText().toString());
        object.put("taskId", mTaskDetails.getTaskId());
        map.put("body", object.toJSONString());
        object.put("roleCode", "5");
        request.doPostRequest(5, true, Uri.owner3Official, map);
    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (code == 0) {
            if (data != null) {
                mTaskDetails = JSON.parseObject(data.toString(), new TypeReference<TaskDetails>() {
                });
                if (mTaskDetails != null) {
                    setUiData(mTaskDetails);
                }
            }
        } else if (code == 1) {
            showToast("确认成功！");
            EventBus.getDefault().post(new MessageEvent(1, ""));
            finish();
        } else if (code == 2 || code == 3 || code == 4 || code == 5) {
            showToast("审批成功！");
            EventBus.getDefault().post(new MessageEvent(1, ""));
            finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 105) {
                TaskDetails.DiseaseMsgDtosBean dtosBean = new TaskDetails.DiseaseMsgDtosBean();
                EventTypeBase eventTypeBase = (EventTypeBase) data.getSerializableExtra("EventTypeBase");
                dtosBean.setDetail(eventTypeBase.getEventType().getDetail());
                dtosBean.setDiseaseId(eventTypeBase.getEventType().getId());
                dtosBean.setDiseaseType(eventTypeBase.getEventType().getOrderType());
                dtosBean.setDiseaseTypeName(eventTypeBase.getEventType().getName());

                ArrayList<TaskDetails.DiseaseMsgDtosBean.DiseaseAttrMsgDtosBean> list = new ArrayList<>();

                String areaStr = "";
                for (EventAttr a : eventTypeBase.getEventAttrsList()) {
                    TaskDetails.DiseaseMsgDtosBean.DiseaseAttrMsgDtosBean bean = new TaskDetails.DiseaseMsgDtosBean.DiseaseAttrMsgDtosBean();
                    bean.setDiseaseAttrName(a.getName());
                    bean.setTypeUnitId(a.getTypeUnitId());
                    bean.setValue(a.getDefaultVal());
                    list.add(bean);
                    if (bean.getDiseaseAttrName().contains("面积")) {
                        areaStr = bean.getValue();
                    }
                }
                dtosBean.setDiseaseAttrMsgDtos(list);

                mTaskDetails.getDiseaseMsgDtos().add(dtosBean);
                adapter_eventtype.notifyDataSetChanged();
                if (!TextUtils.isEmpty(areaStr)) {
                    totalArea = totalArea + Double.parseDouble(areaStr);
                    mAllArea.setText(totalArea + "m²");
                }
//
            } else if (requestCode == 106) {
                ArrayList<Plan> planList = (ArrayList<Plan>) data.getSerializableExtra("planList");
                for (Plan p : planList) {
                    boolean isExist = false;
                    for (Plan p2 : mTaskDetails.getPlanFuns()) {
                        if (p.getId().equals(p2.getId())) {
                            isExist = true;// 找到相同项，跳出本层循环
                            break;
                        }
                    }
                    if (!isExist) {// 不相同，加入list3中
                        mTaskDetails.getPlanFuns().add(p);
                    }
                }
                adapter_plan.notifyDataSetChanged();
                LogUtils.d(mTaskDetails.getPlanFuns());
            }
        }
    }

}
