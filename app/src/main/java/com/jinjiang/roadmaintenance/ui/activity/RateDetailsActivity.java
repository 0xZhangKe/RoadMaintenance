package com.jinjiang.roadmaintenance.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
import com.jinjiang.roadmaintenance.data.Plan;
import com.jinjiang.roadmaintenance.data.RateList;
import com.jinjiang.roadmaintenance.data.TaskDetails;
import com.jinjiang.roadmaintenance.data.UserInfo;
import com.jinjiang.roadmaintenance.model.NetWorkRequest;
import com.jinjiang.roadmaintenance.model.UIDataListener;
import com.jinjiang.roadmaintenance.ui.view.DialogProgress;
import com.jinjiang.roadmaintenance.ui.view.ListViewForScrollView;
import com.jinjiang.roadmaintenance.ui.view.MyGridView;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.jinjiang.roadmaintenance.utils.ACache;
import com.jinjiang.roadmaintenance.utils.GlideImgManager;
import com.jinjiang.roadmaintenance.utils.ScreenUtils;
import com.jinjiang.roadmaintenance.utils.Uri;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RateDetailsActivity extends BaseActivity implements UIDataListener {

    @BindView(R.id.rateDetails_title)
    TextView mTitle;
    @BindView(R.id.rateDetails_eventId)
    TextView mEventId;
    @BindView(R.id.rateDetails_saveDate)
    TextView mSaveDate;
    @BindView(R.id.rateDetails_savePerson)
    TextView mSavePerson;
    @BindView(R.id.rateDetails_roadName)
    EditText mRoadName;
    @BindView(R.id.rateDetails_location)
    TextView mLocation;
    @BindView(R.id.rateDetails_roadType)
    TextView mRoadType;
    @BindView(R.id.rateDetails_driverwayType)
    TextView mDriverwayType;
    @BindView(R.id.rateDetails_planTime)
    EditText mPlanTime;
    @BindView(R.id.rateDetails_planCost)
    TextView mPlanCost;
    @BindView(R.id.rateDetails_realTime)
    EditText mRealTime;
    @BindView(R.id.rateDetails_realcost)
    EditText mRealcost;
    @BindView(R.id.rateDetails_grid_xiufuqiantupian)
    MyGridView mGridXiufuqiantupian;
    @BindView(R.id.rateDetails_grid_xiufutupian)
    MyGridView mGridXiufutupian;
    @BindView(R.id.rateDetails_grid_fujian)
    MyGridView mGridFujian;
    @BindView(R.id.rateDetails_approvalStateTv)
    TextView mApprovalStateTv;
    @BindView(R.id.rateDetails_approvalStateLv)
    ListViewForScrollView mApprovalStateLv;
    @BindView(R.id.rateDetails_drivertype_ll)
    LinearLayout mDrivertypeLl;
    @BindView(R.id.rateDetails_eventtype_listview)
    ListViewForScrollView mEventtypeListview;
    @BindView(R.id.rateDetails_plan_listview)
    ListViewForScrollView mPlanListview;
    @BindView(R.id.rateDetails_uirealdtate)
    EditText mUirealdtate;
    @BindView(R.id.rateDetails_uirealarea)
    EditText mUirealarea;
    @BindView(R.id.rateDetails_uirealDate_ll)
    LinearLayout mUirealDateLl;
    @BindView(R.id.rateDetails_planTime_ll)
    LinearLayout mPlanTimeLl;
    @BindView(R.id.rateDetails_realTime_ll)
    LinearLayout mRealTimeLl;
    @BindView(R.id.rateDetails_xiufutupian_ll)
    LinearLayout mXiufutupianLl;
    @BindView(R.id.rateDetails_fujian_ll)
    LinearLayout mFujianLl;
    private ACache mAcache;
    private Dialog dialog;
    private NetWorkRequest request;
    private UserInfo userInfo;
    private TaskDetails mTaskDetails;
    private RateList.RowsBean mRowsBean;
    private int OrderStatus;
    private int OrderType;
    private double totalArea;
    private List<String> maintainPicUrls;
    private List<String> scenePicUrls;
    private CommonAdapter<TaskDetails.TaskInfosBean> adapter_approval;
    private CommonAdapter<String> mGridTupianAdapter;
    private CommonAdapter<String> mGridTupianAdapter1;
    private CommonAdapter<String> mGridfujianAdapter1;
    private CommonAdapter<Plan> adapter_uiplan;
    private CommonAdapter<TaskDetails.DiseaseMsgDtosBean> adapter_eventtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_rate_details;
    }

    @Override
    protected void initUI() {
        mAcache = ACache.get(RateDetailsActivity.this);
        dialog = DialogProgress.createLoadingDialog(RateDetailsActivity.this, "", this);
        request = new NetWorkRequest(RateDetailsActivity.this, this);
        userInfo = (UserInfo) mAcache.getAsObject("UserInfo");
        if (userInfo == null || TextUtils.isEmpty(userInfo.getUserId())) {
            myToast.toast(RateDetailsActivity.this, "登录状态已过期，请重新登录！");
            startActivity(new Intent(RateDetailsActivity.this, LoginActivity.class));
            finish();
        }

        Intent intent = getIntent();
        if (intent.hasExtra("Row")) {
            mRowsBean = (RateList.RowsBean) intent.getSerializableExtra("Row");
        }
    }

    @Override
    protected void initData() {
        if (mRowsBean != null) {

            Map map = new HashMap();
            map.put("userId", userInfo.getUserId());
            map.put("appSid", userInfo.getAppSid());
            JSONObject object = new JSONObject();
            object.put("workOrderId", mRowsBean.getWorkOrderId());
            object.put("taskId", mRowsBean.getTaskId());
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

        if (OrderType == 5) {
            mDrivertypeLl.setVisibility(View.GONE);
        } else if (OrderType == 4 || OrderType == 3) {
            mDrivertypeLl.setVisibility(View.GONE);
        }

        if (OrderStatus == 2) {//待确认
            mPlanTimeLl.setVisibility(View.GONE);
            mRealTimeLl.setVisibility(View.GONE);
            mFujianLl.setVisibility(View.GONE);
            mXiufutupianLl.setVisibility(View.GONE);
            mUirealDateLl.setVisibility(View.GONE);

        } else if (OrderStatus == 3) {//技术员审批--不处理
            mPlanTimeLl.setVisibility(View.GONE);
            mRealTimeLl.setVisibility(View.GONE);
            mFujianLl.setVisibility(View.GONE);
            mXiufutupianLl.setVisibility(View.GONE);
            mUirealDateLl.setVisibility(View.GONE);
        } else if (OrderStatus == 4) {//需处理(不会出现4)
            mRealTimeLl.setVisibility(View.GONE);
            mFujianLl.setVisibility(View.GONE);
            mXiufutupianLl.setVisibility(View.GONE);
            mUirealDateLl.setVisibility(View.GONE);
        } else if (OrderStatus == 5) {//监理审批
            mRealTimeLl.setVisibility(View.GONE);
            mFujianLl.setVisibility(View.GONE);
            mXiufutupianLl.setVisibility(View.GONE);
            mUirealDateLl.setVisibility(View.GONE);
        } else if (OrderStatus == 7) {//监理审核否--重新下单
            mRealTimeLl.setVisibility(View.GONE);
            mFujianLl.setVisibility(View.GONE);
            mXiufutupianLl.setVisibility(View.GONE);
            mUirealDateLl.setVisibility(View.GONE);
        } else if (OrderStatus == 8) {//监理审核属实--一级业主批复
            mRealTimeLl.setVisibility(View.GONE);
            mFujianLl.setVisibility(View.GONE);
            mXiufutupianLl.setVisibility(View.GONE);
            mUirealDateLl.setVisibility(View.GONE);
        } else if (OrderStatus == 9) {//一级业主审核否--重新下单
            mRealTimeLl.setVisibility(View.GONE);
            mFujianLl.setVisibility(View.GONE);
            mXiufutupianLl.setVisibility(View.GONE);
            mUirealDateLl.setVisibility(View.GONE);
        } else if (OrderStatus == 10) {//一级业主审核属实--二级业主批复
            mRealTimeLl.setVisibility(View.GONE);
            mFujianLl.setVisibility(View.GONE);
            mXiufutupianLl.setVisibility(View.GONE);
            mUirealDateLl.setVisibility(View.GONE);
        } else if (OrderStatus == 11) {//二级业主审核否--重新下单
            mRealTimeLl.setVisibility(View.GONE);
            mFujianLl.setVisibility(View.GONE);
            mXiufutupianLl.setVisibility(View.GONE);
            mUirealDateLl.setVisibility(View.GONE);
        } else if (OrderStatus == 12) {//二级业主审核属实--三级业主批复
            mRealTimeLl.setVisibility(View.GONE);
            mFujianLl.setVisibility(View.GONE);
            mXiufutupianLl.setVisibility(View.GONE);
            mUirealDateLl.setVisibility(View.GONE);
        } else if (OrderStatus == 13) {//三级业主审核否--重新下单
            mRealTimeLl.setVisibility(View.GONE);
            mFujianLl.setVisibility(View.GONE);
            mXiufutupianLl.setVisibility(View.GONE);
            mUirealDateLl.setVisibility(View.GONE);
        } else if (OrderStatus == 14 || OrderStatus == 6 || OrderStatus == 17 || OrderStatus == 19) {//三级业主审核属实-->20m未施工
            mRealTimeLl.setVisibility(View.GONE);
            mFujianLl.setVisibility(View.GONE);
            mXiufutupianLl.setVisibility(View.GONE);
            mUirealDateLl.setVisibility(View.GONE);
        } else if (OrderStatus == 15) {//初验

        } else if (OrderStatus == 17) {//初验不合格，重新提交施工


        } else if (OrderStatus == 18) {//初验合格，三方验收

        } else if (OrderStatus == 19) {//验收不合格，重新提交施工


        } else if (OrderStatus == 20) {//验收合格，完结状态

        }


        mEventId.setText(wm.getSn());
        mSaveDate.setText(td.getTaskCreateTime());
        mSavePerson.setText(wm.getUserId());
        mRoadName.setText(ScreenUtils.getRoad(wm.getLocationDesc()));
        mLocation.setText(wm.getLocationDesc());
        mRoadType.setText(wm.getOrderTypeName());
        mDriverwayType.setText(wm.getLineTypeName());
        mPlanTime.setText(wm.getTimePlan() + "天");
        mPlanCost.setText(wm.getMoneyPlan() + "元");
        mRealTime.setText(wm.getTimePractical() + "天");
        mRealcost.setText(wm.getMoneyPractical() + "元");

        mUirealdtate.setText(wm.getMaintainStarTime());
        mUirealarea.setText(wm.getMaintainArea() + "m²");

        mApprovalStateTv.setText(td.getTaskName());

        if (td.getPracticalFuns() != null && td.getPracticalFuns().size() > 0) {
            setuiplanAdapter(td.getPracticalFuns());
        } else {
            setuiplanAdapter(td.getPlanFuns());
        }
        setEventtypeListAdapter(td.getDiseaseMsgDtos());

        //设计图
        maintainPicUrls = wm.getMaintainPicUrls();
        //施工图
        scenePicUrls = wm.getScenePicUrls();

        setGridAdapter(wm.getPicUrls());
        setApprovalAdapter(td.getTaskInfos());
        setxiufuAdapter(scenePicUrls);
        setfujianAdapter(maintainPicUrls);

    }

    /**
     * 病害类型
     *
     * @param list
     */
    private void setEventtypeListAdapter(final List<TaskDetails.DiseaseMsgDtosBean> list) {
        adapter_eventtype = new CommonAdapter<TaskDetails.DiseaseMsgDtosBean>(RateDetailsActivity.this, R.layout.item_eventtype_add2, list) {
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
            }
        };
        mEventtypeListview.setAdapter(adapter_eventtype);
    }

    /**
     * 施工计划
     *
     * @param list
     */
    private void setuiplanAdapter(final List<Plan> list) {
        adapter_uiplan = new CommonAdapter<Plan>(RateDetailsActivity.this, R.layout.item_eventtype_add2, list) {
            @Override
            protected void convert(ViewHolder viewHolder, Plan item, final int position) {
                viewHolder.setText(R.id.item_name, item.getFunName().trim());
                viewHolder.setText(R.id.item_attr, "");
            }
        };
        mPlanListview.setAdapter(adapter_uiplan);
    }

    /**
     * 审批
     *
     * @param list
     */
    private void setApprovalAdapter(final List<TaskDetails.TaskInfosBean> list) {
        adapter_approval = new CommonAdapter<TaskDetails.TaskInfosBean>(RateDetailsActivity.this, R.layout.item_approval_list, list) {
            @Override
            protected void convert(ViewHolder viewHolder, TaskDetails.TaskInfosBean item, final int position) {
                viewHolder.setText(R.id.item_name, item.getUserName());
                viewHolder.setText(R.id.item_desc, item.getDetail());
                viewHolder.setText(R.id.item_time, item.getEndTime());
                if (position == 0) {
                    viewHolder.setBackgroundRes(R.id.item_dot, R.drawable.gre_dot_blue_back);
                    TextView v = (TextView) viewHolder.getView(R.id.item_line);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    params.setMargins(0, ScreenUtils.dp2px(RateDetailsActivity.this, 20), 0, 0);
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
     * 修复前图片
     *
     * @param list
     */
    private void setGridAdapter(List<String> list) {
        mGridTupianAdapter = new CommonAdapter<String>(RateDetailsActivity.this, R.layout.item_addphoto_grid, list) {
            @Override
            protected void convert(ViewHolder viewHolder, final String item, int position) {
                GlideImgManager.glideLoader(RateDetailsActivity.this,item,R.drawable.pic_not_found,R.drawable.pic_not_found,(ImageView)viewHolder.getView(R.id.item_addphoto_grid_img),1);
                viewHolder.setVisible(R.id.item_addphoto_grid_del, false);
            }
        };
        mGridXiufuqiantupian.setAdapter(mGridTupianAdapter);
    }

    /**
     * 修复后网络图片
     *
     * @param list
     */
    private void setxiufuAdapter(List<String> list) {
        mGridTupianAdapter1 = new CommonAdapter<String>(RateDetailsActivity.this, R.layout.item_addphoto_grid, list) {
            @Override
            protected void convert(ViewHolder viewHolder, final String item, int position) {
                Glide.with(RateDetailsActivity.this).load(item).into((ImageView) viewHolder.getView(R.id.item_addphoto_grid_img));
                viewHolder.setVisible(R.id.item_addphoto_grid_del, false);
            }
        };
        mGridXiufutupian.setAdapter(mGridTupianAdapter1);
    }

    /**
     * 修复后附件网络图片
     *
     * @param list
     */
    private void setfujianAdapter(List<String> list) {
        mGridfujianAdapter1 = new CommonAdapter<String>(RateDetailsActivity.this, R.layout.item_addphoto_grid, list) {
            @Override
            protected void convert(ViewHolder viewHolder, final String item, int position) {
                Glide.with(RateDetailsActivity.this).load(item).into((ImageView) viewHolder.getView(R.id.item_addphoto_grid_img));
                viewHolder.setVisible(R.id.item_addphoto_grid_del, false);
            }
        };
        mGridFujian.setAdapter(mGridfujianAdapter1);
    }


    @OnClick(R.id.rateDetails_back)
    public void onViewClicked() {
        finish();
        overridePendingTransition(0, R.anim.base_slide_out);
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
        }
    }

    @Override
    public void showToast(String message) {
        myToast.toast(RateDetailsActivity.this, message);
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
