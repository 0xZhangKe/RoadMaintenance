package com.jinjiang.roadmaintenance.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
import com.jinjiang.roadmaintenance.data.MessageEvent;
import com.jinjiang.roadmaintenance.data.Plan;
import com.jinjiang.roadmaintenance.data.Task;
import com.jinjiang.roadmaintenance.data.TaskDetails;
import com.jinjiang.roadmaintenance.data.UserInfo;
import com.jinjiang.roadmaintenance.model.NetWorkRequest;
import com.jinjiang.roadmaintenance.model.UIDataListener;
import com.jinjiang.roadmaintenance.ui.view.ActionSheetDialog;
import com.jinjiang.roadmaintenance.ui.view.DialogProgress;
import com.jinjiang.roadmaintenance.ui.view.ListViewForScrollView;
import com.jinjiang.roadmaintenance.ui.view.MyGridView;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.jinjiang.roadmaintenance.utils.ACache;
import com.jinjiang.roadmaintenance.utils.CompressImage;
import com.jinjiang.roadmaintenance.utils.GlideImgManager;
import com.jinjiang.roadmaintenance.utils.ScreenUtils;
import com.jinjiang.roadmaintenance.utils.Uri;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

public class EventDetail2Activity extends BaseActivity implements UIDataListener, ActionSheetDialog.OnActionSheetSelected, DialogInterface.OnCancelListener {
    @BindView(R.id.eventdetail2_eventId)
    TextView mEventId;
    @BindView(R.id.eventdetail2_saveDate)
    TextView mSaveDate;
    @BindView(R.id.eventdetail2_savePerson)
    TextView mSavePerson;
    @BindView(R.id.eventdetail2_roadName)
    EditText mRoadName;
    @BindView(R.id.eventdetail2_location)
    TextView mLocation;
    @BindView(R.id.eventdetail2_roadType)
    TextView mRoadType;
    @BindView(R.id.eventdetail2_driverwayType)
    TextView mDriverwayType;
    @BindView(R.id.eventdetail2_plansAdd_listview)
    ListViewForScrollView mPlansAddListview;
    @BindView(R.id.eventdetail2_plan_ll)
    LinearLayout mPlanLl;
    @BindView(R.id.eventdetail2_grid_xiufuqiantupian)
    MyGridView mGridXiufuqiantupian;
    @BindView(R.id.eventdetail2_xiufuqiantupian_ll)
    LinearLayout mXiufuqiantupianLl;
    @BindView(R.id.eventdetail2_grid_xiufutupian)
    MyGridView mGridXiufutupian;
    @BindView(R.id.eventdetail2_grid_fujian)
    MyGridView mGridFujian;
    @BindView(R.id.eventdetail2_approvalStateTv)
    TextView mApprovalStateTv;
    @BindView(R.id.eventdetail2_approvalStateLv)
    ListViewForScrollView mApprovalStateLv;
    @BindView(R.id.eventdetail2_approvalStateLl)
    LinearLayout mApprovalStateLl;
    @BindView(R.id.eventdetail2_remark)
    EditText mRemark;
    @BindView(R.id.eventdetail2_remark_ll)
    LinearLayout mRemarkLl;
    @BindView(R.id.eventdetail2_confirm1)
    RadioButton mConfirm1;
    @BindView(R.id.eventdetail2_confirm2)
    RadioButton mConfirm2;
    @BindView(R.id.eventdetail2_confirm_rg)
    RadioGroup mConfirmRg;
    @BindView(R.id.eventdetail2_send)
    TextView mSend;
    @BindView(R.id.eventdetail2_planTime)
    EditText mPlanTime;
    @BindView(R.id.eventdetail2_planCost)
    TextView mPlanCost;
    @BindView(R.id.eventdetail2_realTime)
    EditText mRealTime;
    @BindView(R.id.eventdetail2_realcost)
    EditText mRealcost;
    @BindView(R.id.eventdetail2_drivertype_ll)
    LinearLayout mDrivertypeLl;
    @BindView(R.id.eventdetail2_eventtype_listview)
    ListViewForScrollView mEventtypeListview;
    @BindView(R.id.eventdetail2_finishTime)
    TextView mFinishTime;
    @BindView(R.id.eventdetail2_realArea)
    EditText mRealArea;
    @BindView(R.id.eventdetail2_realarea_ll)
    LinearLayout mRealareaLl;
    @BindView(R.id.eventdetail2_uirealTime)
    EditText mUirealTime;
    @BindView(R.id.eventdetail2_uirealcost)
    EditText mUirealcost;
    @BindView(R.id.eventdetail2_plan_listview)
    ListViewForScrollView mPlanListview;
    @BindView(R.id.eventdetail2_uirealTime_ll)
    LinearLayout mUirealTimeLl;
    @BindView(R.id.eventdetail2_uirealdtate)
    EditText mUirealdtate;
    @BindView(R.id.eventdetail2_uirealarea)
    EditText mUirealarea;
    @BindView(R.id.eventdetail2_uirealDate_ll)
    LinearLayout mUirealDateLl;
    @BindView(R.id.eventdetail2_title)
    TextView mTitle;
    @BindView(R.id.eventdetail2_approvalStateImg)
    ImageView mApprovalStateImg;
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
    private int OrderType;
    private double totalArea = 0;
    private CommonAdapter<TaskDetails.TaskInfosBean> adapter_approval;
    private CommonAdapter<Plan> adapter_plan;
    private ArrayList<Plan> real_plan;
    private ArrayList<File> xiufutuList;
    private ArrayList<File> fujiantuList;
    private boolean fujian = false;
    public static String SAVED_IMAGE_DIR_PATH =
            Environment.getExternalStorageDirectory().getPath()
                    + "/RM/camera/";// 拍照路径
    private String cameraPath;
    String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tempfile/";
    private List<String> maintainPicUrls;
    private List<String> scenePicUrls;
    private CommonAdapter<String> mGridTupianAdapter1;
    private CommonAdapter<String> mGridfujianAdapter1;
    private DatePicker picker_Time;
    private String selectDate;
    private CommonAdapter<Plan> adapter_uiplan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_event_detail2;
    }

    @Override
    protected void initUI() {
        Intent intent = getIntent();
        if (intent.hasExtra("Task")) {
            mTask = (Task) intent.getSerializableExtra("Task");
        }
        mAcache = ACache.get(EventDetail2Activity.this);
        dialog = DialogProgress.createLoadingDialog(EventDetail2Activity.this, "", this);
        request = new NetWorkRequest(EventDetail2Activity.this, this);
        userInfo = (UserInfo) mAcache.getAsObject("UserInfo");
        if (userInfo == null || TextUtils.isEmpty(userInfo.getUserId())) {
            myToast.toast(EventDetail2Activity.this, "登录状态已过期，请重新登录！");
            startActivity(new Intent(EventDetail2Activity.this, LoginActivity.class));
            finish();
        }
        userRole = userInfo.getUserRole();

        real_plan = new ArrayList<>();
        xiufutuList = new ArrayList<>();
        fujiantuList = new ArrayList<>();

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
        initDatePicker();
    }

    /**
     * 设置界面数据
     */
    private void setUiData(TaskDetails td) {

        TaskDetails.WorkOrderMsgDtoBean wm = td.getWorkOrderMsgDto();
        OrderStatus = wm.getOrderStatus();
        OrderType = wm.getOrderType();
        totalArea = wm.getArea();

        if (OrderType == 3 || OrderType == 4 || OrderType == 5) {
            mDrivertypeLl.setVisibility(View.GONE);
        }
        if (OrderStatus == 2) {//待确认
        } else if (OrderStatus == 3) {//技术员审批--不处理
        } else if (OrderStatus == 4) {//需处理(不会出现4)
        } else if (OrderStatus == 5) {//监理审批
        } else if (OrderStatus == 7) {//监理审核否--重新下单
        } else if (OrderStatus == 8) {//监理审核属实--一级业主批复
        } else if (OrderStatus == 9) {//一级业主审核否--重新下单
        } else if (OrderStatus == 10) {//一级业主审核属实--二级业主批复
        } else if (OrderStatus == 11) {//二级业主审核否--重新下单
        } else if (OrderStatus == 12) {//二级业主审核属实--三级业主批复
        } else if (OrderStatus == 13) {//三级业主审核否--重新下单
        } else if (OrderStatus == 14 || OrderStatus == 6 || OrderStatus == 17 || OrderStatus == 19) {//三级业主审核属实-->20m未施工
            if (OrderStatus == 17 || OrderStatus == 19) {
                mTitle.setText("重新提交");
            } else {
                mTitle.setText("提交初验");
            }
            xiufutuList.add(new File(""));
            fujiantuList.add(new File(""));
            mRealTime.setEnabled(true);
            mRealcost.setEnabled(true);
            mUirealTimeLl.setVisibility(View.GONE);
            mPlanLl.setVisibility(View.VISIBLE);
            mXiufuqiantupianLl.setVisibility(View.GONE);
            mApprovalStateLl.setVisibility(View.GONE);
            mConfirmRg.setVisibility(View.GONE);
            mRemarkLl.setVisibility(View.GONE);
            mRealareaLl.setVisibility(View.VISIBLE);
            mUirealDateLl.setVisibility(View.GONE);
            mSend.setText("提交初验");


        } else if (OrderStatus == 15) {//初验
            mSend.setText("验收");
            mRemark.setEnabled(true);
            mTitle.setText("初验");

        } else if (OrderStatus == 17) {//初验不合格，重新提交施工
            mApprovalStateTv.setTextColor(getResources().getColor(R.color.red));
            mApprovalStateImg.setVisibility(View.VISIBLE);
        } else if (OrderStatus == 18) {//初验合格，三方验收
            mRemark.setEnabled(true);
            mSend.setText("验收");
            mTitle.setText("验收");
        } else if (OrderStatus == 19) {//验收不合格，重新提交施工
            mApprovalStateTv.setTextColor(getResources().getColor(R.color.red));
            mApprovalStateImg.setVisibility(View.VISIBLE);
        } else if (OrderStatus == 20) {//验收合格，完结状态


        }


        mEventId.setText(wm.getSn());
        mSaveDate.setText(td.getTaskCreateTime());
        mSavePerson.setText(wm.getUserId());
        mRoadName.setText(wm.getRoadName());
        mLocation.setText(wm.getLocationDesc());
        mRoadType.setText(wm.getOrderTypeName());
        mDriverwayType.setText(wm.getLineTypeName());
        mPlanTime.setText(wm.getTimePlan() + "天");
        mPlanCost.setText(wm.getMoneyPlan() + "元");
        mUirealTime.setText(wm.getTimePractical() + "天");
        mUirealcost.setText(wm.getMoneyPractical() + "元");

        mUirealdtate.setText(wm.getMaintainStarTime());
        mUirealarea.setText(wm.getMaintainArea() + "m²");

        mApprovalStateTv.setText(td.getTaskName());

        //设计图
        maintainPicUrls = wm.getMaintainPicUrls();
        //施工图
        scenePicUrls = wm.getScenePicUrls();

        setGridAdapter(wm.getPicUrls());
        setApprovalAdapter(td.getTaskInfos());
        setuiplanAdapter(mTaskDetails.getPracticalFuns());
        LogUtils.d(mTaskDetails.getPlanFuns());
        if (mTaskDetails.getDiseaseMsgDtos() != null)
            setEventtypeListAdapter(mTaskDetails.getDiseaseMsgDtos());
        if (OrderStatus == 14 || OrderStatus == 6 || OrderStatus == 17 || OrderStatus == 19) {
            setxiufuGridAdapter();
            setfujianGridAdapter();
            setplanAdapter(real_plan);
            setuiplanAdapter(mTaskDetails.getPlanFuns());
        } else {
            setxiufuAdapter(scenePicUrls);
            setfujianAdapter(maintainPicUrls);
        }
    }

    /**
     * 病害类型
     *
     * @param list
     */
    private void setEventtypeListAdapter(final List<TaskDetails.DiseaseMsgDtosBean> list) {
        adapter_eventtype = new CommonAdapter<TaskDetails.DiseaseMsgDtosBean>(EventDetail2Activity.this, R.layout.item_eventtype_add2, list) {
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
    private void setplanAdapter(final List<Plan> list) {
        adapter_plan = new CommonAdapter<Plan>(EventDetail2Activity.this, R.layout.item_eventtype_add, list) {
            @Override
            protected void convert(ViewHolder viewHolder, Plan item, final int position) {
                viewHolder.setText(R.id.item_name, item.getFunName());
                if (item.getId().equals("0")) {
                    viewHolder.setText(R.id.item_attr, item.getOtherDesc());
                } else {
                    viewHolder.setText(R.id.item_attr, "");
                }
                if (OrderStatus == 6 || OrderStatus == 14) {
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
     * 施工计划
     *
     * @param list
     */
    private void setuiplanAdapter(final List<Plan> list) {
        adapter_uiplan = new CommonAdapter<Plan>(EventDetail2Activity.this, R.layout.item_eventtype_add2, list) {
            @Override
            protected void convert(ViewHolder viewHolder, Plan item, final int position) {
                viewHolder.setText(R.id.item_name, item.getFunName().trim());
                if (item.getId().equals("0")) {
                    viewHolder.setText(R.id.item_attr, item.getOtherDesc());
                } else {
                    viewHolder.setText(R.id.item_attr, "");
                }
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
        adapter_approval = new CommonAdapter<TaskDetails.TaskInfosBean>(EventDetail2Activity.this, R.layout.item_approval_list, list) {
            @Override
            protected void convert(ViewHolder viewHolder, TaskDetails.TaskInfosBean item, final int position) {
                viewHolder.setText(R.id.item_name, item.getUserName());
                viewHolder.setText(R.id.item_desc, item.getDetail());
                viewHolder.setText(R.id.item_time, item.getEndTime());
                if (position == 0) {
                    viewHolder.setImageResource(R.id.item_dot, R.drawable.appr2);
                    TextView v = (TextView) viewHolder.getView(R.id.item_line);
                    View v2 = viewHolder.getView(R.id.item_dot);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) v2.getLayoutParams();
                    params.setMargins(0, ScreenUtils.dp2px(EventDetail2Activity.this, 20), 0, 0);
                    params2.width = ScreenUtils.dp2px(EventDetail2Activity.this, 15);
                    params2.height = ScreenUtils.dp2px(EventDetail2Activity.this, 15);
                    v.setLayoutParams(params);
                    v2.setLayoutParams(params2);
                } else {
                    viewHolder.setImageResource(R.id.item_dot, R.drawable.appr1);
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
    private void setGridAdapter(final List<String> list) {
        mGridTupianAdapter = new CommonAdapter<String>(EventDetail2Activity.this, R.layout.item_addphoto_grid, list) {
            @Override
            protected void convert(ViewHolder viewHolder, final String item, int position) {
                GlideImgManager.glideLoader(EventDetail2Activity.this, item, R.drawable.pic_not_found, R.drawable.pic_not_found, (ImageView) viewHolder.getView(R.id.item_addphoto_grid_img), 1);
                viewHolder.setVisible(R.id.item_addphoto_grid_del, false);
            }
        };
        mGridXiufuqiantupian.setAdapter(mGridTupianAdapter);
        mGridXiufuqiantupian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventDetail2Activity.this,ImgshowActivity.class);
                intent.putExtra("url",list.get(position));
                startActivity(intent);
            }
        });
    }

    /**
     * 修复后网络图片
     *
     * @param list
     */
    private void setxiufuAdapter(final List<String> list) {
        mGridTupianAdapter1 = new CommonAdapter<String>(EventDetail2Activity.this, R.layout.item_addphoto_grid, list) {
            @Override
            protected void convert(ViewHolder viewHolder, final String item, int position) {
                GlideImgManager.glideLoader(EventDetail2Activity.this, item, R.drawable.pic_not_found, R.drawable.pic_not_found, (ImageView) viewHolder.getView(R.id.item_addphoto_grid_img), 1);
                viewHolder.setVisible(R.id.item_addphoto_grid_del, false);
            }
        };
        mGridXiufutupian.setAdapter(mGridTupianAdapter1);
        mGridXiufutupian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventDetail2Activity.this,ImgshowActivity.class);
                intent.putExtra("url",list.get(position));
                startActivity(intent);
            }
        });
    }

    /**
     * 修复后附件网络图片
     *
     * @param list
     */
    private void setfujianAdapter(final List<String> list) {
        mGridfujianAdapter1 = new CommonAdapter<String>(EventDetail2Activity.this, R.layout.item_addphoto_grid, list) {
            @Override
            protected void convert(ViewHolder viewHolder, final String item, int position) {
                Glide.with(EventDetail2Activity.this).load(item).into((ImageView) viewHolder.getView(R.id.item_addphoto_grid_img));
                GlideImgManager.glideLoader(EventDetail2Activity.this, item, R.drawable.pic_not_found, R.drawable.pic_not_found, (ImageView) viewHolder.getView(R.id.item_addphoto_grid_img), 1);
                viewHolder.setVisible(R.id.item_addphoto_grid_del, false);
            }
        };
        mGridFujian.setAdapter(mGridfujianAdapter1);
        mGridXiufutupian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventDetail2Activity.this,ImgshowActivity.class);
                intent.putExtra("url",list.get(position));
                startActivity(intent);
            }
        });
    }

    /**
     * 修复后本地图片
     */
    private void setxiufuGridAdapter() {
        mGridXiufutupian.setAdapter(new CommonAdapter<File>(EventDetail2Activity.this, R.layout.item_addphoto_grid, xiufutuList) {
            @Override
            protected void convert(ViewHolder viewHolder, final File item, int position) {
                if (position != xiufutuList.size() - 1) {
                    GlideImgManager.glideLoader(EventDetail2Activity.this, "file://" + item.getAbsolutePath(), R.drawable.pic_not_found, R.drawable.pic_not_found, (ImageView) viewHolder.getView(R.id.item_addphoto_grid_img), 1);
                    viewHolder.setOnClickListener(R.id.item_addphoto_grid_del, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            xiufutuList.remove(item);
                            setxiufuGridAdapter();
                        }
                    });
                    viewHolder.setVisible(R.id.item_addphoto_grid_del, true);
                } else {
                    Glide.with(EventDetail2Activity.this).load(R.drawable.add3).into((ImageView) viewHolder.getView(R.id.item_addphoto_grid_img));
                    viewHolder.setVisible(R.id.item_addphoto_grid_del, false);
                }
            }
        });
        mGridXiufutupian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == xiufutuList.size() - 1) {
                    if (xiufutuList.size()>=5){
                        showToast("最多上传4张图片！");
                        return;
                    }
                    ActionSheetDialog.showSheet(EventDetail2Activity.this, EventDetail2Activity.this, EventDetail2Activity.this);
                    fujian = false;
                }
            }
        });
    }

    /**
     * 修复后附件本地图片
     */
    private void setfujianGridAdapter() {
        mGridFujian.setAdapter(new CommonAdapter<File>(EventDetail2Activity.this, R.layout.item_addphoto_grid, fujiantuList) {
            @Override
            protected void convert(ViewHolder viewHolder, final File item, int position) {
                if (position != fujiantuList.size() - 1) {
                    GlideImgManager.glideLoader(EventDetail2Activity.this, "file://" + item.getAbsolutePath(), R.drawable.pic_not_found, R.drawable.pic_not_found, (ImageView) viewHolder.getView(R.id.item_addphoto_grid_img), 1);
                    viewHolder.setOnClickListener(R.id.item_addphoto_grid_del, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fujiantuList.remove(item);
                            setfujianGridAdapter();
                        }
                    });
                    viewHolder.setVisible(R.id.item_addphoto_grid_del, true);
                } else {
                    Glide.with(EventDetail2Activity.this).load(R.drawable.add3).into((ImageView) viewHolder.getView(R.id.item_addphoto_grid_img));
                    viewHolder.setVisible(R.id.item_addphoto_grid_del, false);
                }
            }
        });
        mGridFujian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == fujiantuList.size() - 1) {
                    if (fujiantuList.size()>=5){
                        showToast("最多上传4张图片！");
                        return;
                    }
                    ActionSheetDialog.showSheet(EventDetail2Activity.this, EventDetail2Activity.this, EventDetail2Activity.this);
                    fujian = true;
                }
            }
        });
    }

    @OnClick({R.id.eventdetail2_back, R.id.eventdetail2_save, R.id.eventdetail2_plansAdd, R.id.eventdetail2_send, R.id.eventdetail2_finishTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.eventdetail2_back:
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
                break;
            case R.id.eventdetail2_finishTime:
                picker_Time.show();
                break;
            case R.id.eventdetail2_save:
                break;
            case R.id.eventdetail2_plansAdd:
                StringBuffer typeIds = new StringBuffer();
                if (mTaskDetails != null && mTaskDetails.getDiseaseMsgDtos() != null) {
                    if (OrderType != 5) {
                        for (int i = 0; i < mTaskDetails.getDiseaseMsgDtos().size(); i++) {
                            if (i == 0) {
                                typeIds.append(mTaskDetails.getDiseaseMsgDtos().get(i).getDiseaseId());
                            } else {
                                typeIds.append("," + mTaskDetails.getDiseaseMsgDtos().get(i).getDiseaseId());
                            }
                        }
                    }
                }
                Intent intent2 = new Intent(EventDetail2Activity.this, PlanActivity.class);
                intent2.putExtra("orderType", OrderType);
                intent2.putExtra("diseaseTypeId", typeIds.toString());
                startActivityForResult(intent2, 106);
                break;
            case R.id.eventdetail2_send:
                if (OrderStatus == 14 || OrderStatus == 6) {//三级业主审核属实-->20m未施工--提交施工后图
                    wangong();
                } else if (OrderStatus == 15) {//监理初验
                    chuyan();

                } else if (OrderStatus == 17) {//初验不合格，重新提交施工
                    wangong();

                } else if (OrderStatus == 18) {//初验合格，三方验收
                    sanfang();

                } else if (OrderStatus == 19) {//验收不合格，重新提交施工
                    wangong();

                } else if (OrderStatus == 20) {//验收合格，完结状态


                }
                break;
        }
    }

    /**
     * 初始化日期时间选择器
     */
    private void initDatePicker() {
        //获取当前日期
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);//获取年份
        int month = ca.get(Calendar.MONTH) + 1;//获取月份
        int day = ca.get(Calendar.DATE);//获取日
        String monthStr = "" + month;
        String dayStr = "" + day;
        if (month < 10) {
            monthStr = "0" + month;
        }
        if (day < 10) {
            dayStr = "0" + day;
        }
        selectDate = year + "-" + monthStr + "-" + dayStr;
        mFinishTime.setText(selectDate);
        //设置picker
        picker_Time = new DatePicker(this, DatePicker.YEAR_MONTH_DAY);
        picker_Time.setRange(1900, 2100);//设置年份起始
        picker_Time.setCancelText("取消");
        picker_Time.setSubmitText("确定");
        picker_Time.setLineColor(EventDetail2Activity.this.getResources().getColor(R.color.blue));
        picker_Time.setTextColor(EventDetail2Activity.this.getResources().getColor(R.color.text_black));
        picker_Time.setCancelTextColor(EventDetail2Activity.this.getResources().getColor(R.color.gray));
        picker_Time.setSubmitTextColor(EventDetail2Activity.this.getResources().getColor(R.color.blue));
        picker_Time.setSelectedItem(year, month, day);
        //监听选择
        picker_Time.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                selectDate = year + "-" + month + "-" + day;
                mFinishTime.setText(selectDate);
            }
        });
    }

    /**
     * 完工
     */
    private void wangong() {
        String reallTime = mRealTime.getText().toString();
        String reallCost = mRealcost.getText().toString();
        String reallArea = mRealArea.getText().toString();
        String finishTime = mFinishTime.getText().toString();
        if (TextUtils.isEmpty(finishTime)) {
            showToast("请选择完工日期！");
            return;
        }

        if (real_plan == null || real_plan.size() == 0) {
            showToast("请选择实际修复方案！");
            return;
        }
        if (TextUtils.isEmpty(reallTime) || Integer.parseInt(reallTime) == 0) {
            showToast("请输入实际工期！");
            return;
        }
        if (TextUtils.isEmpty(reallCost) || Double.parseDouble(reallCost) == 0) {
            showToast("请输入实际修复费用！");
            return;
        }
        if (TextUtils.isEmpty(reallArea) || Double.parseDouble(reallArea) == 0) {
            showToast("请输入实际修复面积！");
            return;
        }


        if (xiufutuList == null || xiufutuList.size() == 0 || xiufutuList.size() == 1) {
            showToast("请选择维修后图片！");
            return;
        }
        if (fujiantuList == null || fujiantuList.size() == 0 || fujiantuList.size() == 1) {
            showToast("请选择施工附件！");
            return;
        }


        Map map = new HashMap();
        map.put("userId", userInfo.getUserId());
        map.put("appSid", userInfo.getAppSid());
        JSONObject object = new JSONObject();
        object.put("orderStatus", 15);
        object.put("timePractical", reallTime);
        object.put("moneyPractical", reallCost);
        object.put("taskId", mTaskDetails.getTaskId());
        object.put("workOrderId", mTaskDetails.getWorkOrderMsgDto().getWorkOrderId());
        object.put("maintainTime", finishTime);

        StringBuffer plans = new StringBuffer();
        for (Plan p : real_plan) {
            if (!p.getId().equals("0")) {
                if (plans.length() == 0) {
                    plans.append(p.getId());
                } else {
                    plans.append("," + p.getId());
                }
            } else {
                object.put("maintainDetailPractical", p.getOtherDesc());
            }
        }
        object.put("maintainFunIds", plans.toString());
        object.put("areaPractical", reallArea);
        map.put("body", object.toString());
        request.doPostUpload(1, true, Uri.saveConstructionInfo, map, "maintainFiles", fujiantuList, "sceneFiles", xiufutuList);
    }

    /**
     * 初验
     */
    private void chuyan() {
        int deal = mConfirmRg.getCheckedRadioButtonId() == R.id.eventdetail2_confirm1 ? 18 : 17;
        Map map = new HashMap();
        map.put("userId", userInfo.getUserId());
        map.put("appSid", userInfo.getAppSid());
        JSONObject object = new JSONObject();
        object.put("vacationApproved", deal);
        object.put("opinion", mRemark.getText().toString());
        object.put("taskId", mTaskDetails.getTaskId());
        object.put("roleCode", 1);
        map.put("body", object.toJSONString());
        request.doPostRequest(2, true, Uri.official, map);
    }

    /**
     * 三方验收
     */
    private void sanfang() {
        int deal = mConfirmRg.getCheckedRadioButtonId() == R.id.eventdetails_confirm1 ? 20 : 19;
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
            showToast("提交成功！");
            EventBus.getDefault().post(new MessageEvent(1, ""));
            finish();
        } else {
            showToast("处理成功！");
            EventBus.getDefault().post(new MessageEvent(1, ""));
            finish();
        }
    }

    @Override
    public void showToast(String message) {
        myToast.toast(EventDetail2Activity.this, message);
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

            if (requestCode == 100) {
                if (fujian) {
                    if (fujiantuList.size() > 4) {
                        showToast("最多上传4张图片！");
                        return;
                    }
                    File file = new File(cameraPath);
                    File Compressfile = CompressImage.getSmallFile(cameraPath, savePath, file.getName());
                    fujiantuList.add(fujiantuList.size() - 1, Compressfile);
                    setfujianGridAdapter();
                } else {
                    if (xiufutuList.size() > 4) {
                        showToast("最多上传4张图片！");
                        return;
                    }
                    File file = new File(cameraPath);
                    File Compressfile = CompressImage.getSmallFile(cameraPath, savePath, file.getName());
                    xiufutuList.add(xiufutuList.size() - 1, Compressfile);
                    setxiufuGridAdapter();
                }

            } else if (requestCode == 102) {
                if (fujian) {
                    if (fujiantuList.size() > 4) {
                        showToast("最多上传4张图片！");
                        return;
                    }
                    try {
                        android.net.Uri uri = data.getData();
                        LogUtils.d(uri);
                        final String absolutePath = getRealFilePath(EventDetail2Activity.this, uri);
                        File file = new File(absolutePath);
                        File Compressfile = CompressImage.getSmallFile(file.getAbsolutePath(), savePath, file.getName());
                        if (!fujiantuList.contains(Compressfile)) {
                            fujiantuList.add(fujiantuList.size() - 1, Compressfile);
                            setfujianGridAdapter();
                        } else {
                            myToast.toast(EventDetail2Activity.this, "已选择该相片！");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (xiufutuList.size() > 4) {
                        showToast("最多上传4张图片！");
                        return;
                    }
                    try {
                        android.net.Uri uri = data.getData();
                        LogUtils.d(uri);
                        final String absolutePath = getRealFilePath(EventDetail2Activity.this, uri);
                        File file = new File(absolutePath);
                        File Compressfile = CompressImage.getSmallFile(file.getAbsolutePath(), savePath, file.getName());
                        if (!xiufutuList.contains(Compressfile)) {
                            xiufutuList.add(xiufutuList.size() - 1, Compressfile);
                            setxiufuGridAdapter();
                        } else {
                            myToast.toast(EventDetail2Activity.this, "已选择该相片！");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else if (requestCode == 106) {
                ArrayList<Plan> planList = (ArrayList<Plan>) data.getSerializableExtra("planList");
                for (Plan p : planList) {
                    boolean isExist = false;
                    for (Plan p2 : real_plan) {
                        if (p.getId().equals(p2.getId())) {
                            isExist = true;// 找到相同项，跳出本层循环
                            break;
                        }
                    }
                    if (!isExist) {// 不相同，加入list3中
                        real_plan.add(p);
                    }
                }
                adapter_plan.notifyDataSetChanged();
//                LogUtils.d(real_plan);
            }
        }
    }

    public static String getRealFilePath(final Context context, final android.net.Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
    }

    @Override
    public void onClick(int whichButton) {
        if (whichButton == 0) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cameraPath = SAVED_IMAGE_DIR_PATH + System.currentTimeMillis() + ".png";
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                String out_file_path = SAVED_IMAGE_DIR_PATH;
                File dir = new File(out_file_path);
                if (!dir.exists()) {
                    dir.mkdirs();
                } // 把文件地址转换成Uri格式
                android.net.Uri uri = android.net.Uri.fromFile(new File(cameraPath));
                // 设置系统相机拍摄照片完成后图片文件的存放地址
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 100);
            } else {
                myToast.toast(EventDetail2Activity.this, "请确认已经插入SD卡!");
            }
        } else if (whichButton == 1) {
            Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");
            pickIntent.setAction(Intent.ACTION_PICK);
            startActivityForResult(pickIntent, 102);
        }
    }
}
