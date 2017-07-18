package com.jinjiang.roadmaintenance.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
import com.jinjiang.roadmaintenance.data.EventAttr;
import com.jinjiang.roadmaintenance.data.EventAttr_Table;
import com.jinjiang.roadmaintenance.data.EventType;
import com.jinjiang.roadmaintenance.data.EventTypeBase;
import com.jinjiang.roadmaintenance.data.EventType_Table;
import com.jinjiang.roadmaintenance.data.FileUri;
import com.jinjiang.roadmaintenance.data.FileUri_Table;
import com.jinjiang.roadmaintenance.data.MessageEvent;
import com.jinjiang.roadmaintenance.data.Plan;
import com.jinjiang.roadmaintenance.data.Plan_Table;
import com.jinjiang.roadmaintenance.data.SaveEventData;
import com.jinjiang.roadmaintenance.data.SaveEventData_Table;
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
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;

public class EventAddActivity extends BaseActivity implements ActionSheetDialog.OnActionSheetSelected, DialogInterface.OnCancelListener, UIDataListener {

    @BindView(R.id.eventadd_grid_tupian)
    MyGridView mTupianGrid;
    @BindView(R.id.eventadd_num_tv)
    TextView mEventNum;
    @BindView(R.id.eventadd_roadName_tv)
    EditText mRoadName;
    @BindView(R.id.eventadd_driverwayType)
    RadioGroup mDriverwayType;
    @BindView(R.id.eventadd_eventtype_listview)
    ListViewForScrollView mEventtypeListview;
    @BindView(R.id.eventadd_all_area)
    EditText mAllArea;
    @BindView(R.id.eventadd_time)
    EditText mPlanTime;
    @BindView(R.id.eventadd_cost)
    EditText mPlanCost;
    @BindView(R.id.eventadd_content)
    EditText mContent;
    @BindView(R.id.eventadd_driverwayType_ll)
    LinearLayout mDriverwayTypeLl;
    @BindView(R.id.eventadd_eventType_ll)
    LinearLayout mEventTypeLl;
    @BindView(R.id.eventadd_area_ll)
    LinearLayout mAreaLl;
    @BindView(R.id.eventadd_time_ll)
    LinearLayout mTimeLl;
    @BindView(R.id.eventadd_space_ll)
    View mSpaceLl;
    @BindView(R.id.eventadd_fangan_ll)
    LinearLayout eventaddFanganLl;
    @BindView(R.id.eventadd_radio1)
    RadioButton mRadio1;
    @BindView(R.id.eventadd_radio2)
    RadioButton mRadio2;
    @BindView(R.id.eventadd_fangan_listview)
    ListViewForScrollView mFanganListview;
    @BindView(R.id.eventadd_save)
    TextView mSave;
    @BindView(R.id.eventadd_title)
    TextView mTitle;
    private OptionPicker picker_Type;
    private int eventType = 0;
    public static String SAVED_IMAGE_DIR_PATH =
            Environment.getExternalStorageDirectory().getPath()
                    + "/RM/camera/";// 拍照路径
    private String cameraPath;
    private ArrayList<File> mPhotoList;
    private ArrayList<EventTypeBase> mEventTypeBaseList;
    private int mRoadvalue;
    private String mAddress;
    private CommonAdapter<EventTypeBase> adapter_eventtype;
    private ACache mAcache;
    private Dialog dialog;
    private NetWorkRequest request;
    private UserInfo userInfo;
    private int driverwayType = 1;
    private LatLng mCenpt;
    private int userRole;
    private int orderStatus = 1;
    private boolean IsReLoad = false;
    private double longitude;
    private double latitude;
    private long eventId = 0;
    ;
    ArrayList<Plan> mPlanList;
    String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tempfile/";
    private CommonAdapter<Plan> adapter_plan;
    private SaveEventData mEventdata;
    private Task mTask;
    private TaskDetails mTaskDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_event_add;
    }

    @Override
    protected void initUI() {
        mPhotoList = new ArrayList<>();
        mPlanList = new ArrayList<>();
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra("value")) {
            mRoadvalue = intent.getIntExtra("value", 0);
        }
        if (intent.hasExtra("address")) {
            mAddress = intent.getStringExtra("address");
        }
        if (intent.hasExtra("Cenpt")) {
            mCenpt = intent.getParcelableExtra("Cenpt");
        }
        if (intent.hasExtra("SaveEventData")) {
            eventId = intent.getLongExtra("SaveEventData", 0);
        }
        if (intent.hasExtra("Task")) {
            mTask = (Task) intent.getSerializableExtra("Task");
            IsReLoad = true;
            mSave.setVisibility(View.GONE);
            mTitle.setText("重新提交工单");
        }
    }

    private void initLocadata() {
        mEventdata = new Select().from(SaveEventData.class).where(SaveEventData_Table.id.eq(eventId)).querySingle();
        if (mEventdata != null) {
            longitude = mEventdata.longitude;
            latitude = mEventdata.latitude;
            mRoadvalue = mEventdata.roadvalue;
            if (mRoadvalue == 1 || mRoadvalue == 2) {
                driverwayType = mEventdata.lineType;
                if (driverwayType == 1) {
                    mRadio1.setChecked(true);
                    mRadio2.setChecked(false);
                } else {
                    mRadio1.setChecked(false);
                    mRadio2.setChecked(true);
                }
            }
            mAddress = mEventdata.locationDesc;
            mRoadName.setText(mEventdata.roadName);
            if (mRoadvalue != 5) {
                String mArea = mEventdata.area;
                mAllArea.setText(mArea);
            }
            if (userRole == 5) {
                mPlanTime.setText(mEventdata.timePlan);
                mPlanCost.setText(mEventdata.moneyPlan);
                mPlanList = (ArrayList<Plan>) new Select().from(Plan.class).where(Plan_Table.eventId.eq(mEventdata.id)).queryList();
            }
            mContent.setText(mEventdata.detail);
            if (mRoadvalue != 5) {
                mEventTypeBaseList = new ArrayList<>();
                List<EventType> typeList = new Select().from(EventType.class).where(EventType_Table.eventId.eq(mEventdata.id)).queryList();
                for (EventType e : typeList) {
                    EventTypeBase eb = new EventTypeBase();
                    eb.setEventType(e);
                    ArrayList<EventAttr> attrList = (ArrayList<EventAttr>) new Select().from(EventAttr.class).where(EventAttr_Table.eventId.eq(mEventdata.id)).queryList();
                    eb.setEventAttrsList(attrList);
                    mEventTypeBaseList.add(eb);
                }
            }
            List<FileUri> filelist = new Select().from(FileUri.class).where(FileUri_Table.eventId.eq(mEventdata.id)).queryList();
            if (filelist != null) {
                for (FileUri s : filelist) {
                    if (!TextUtils.isEmpty(s.uri) && s.uri.length() > 5)
                        mPhotoList.add(new File(s.uri));
                }
            }
        }
    }

    private void setUiData(TaskDetails td) {
        if (mTaskDetails != null) {
            TaskDetails.WorkOrderMsgDtoBean wm = td.getWorkOrderMsgDto();
            mRoadvalue = wm.getOrderType();
            longitude = wm.getLongitude();
            latitude = wm.getLatitude();
            if (mRoadvalue == 1 || mRoadvalue == 2) {
                driverwayType = wm.getLineType();
                if (driverwayType == 1) {
                    mRadio1.setChecked(true);
                    mRadio2.setChecked(false);
                } else {
                    mRadio1.setChecked(false);
                    mRadio2.setChecked(true);
                }
            }
            mEventNum.setText(wm.getSn());
            mAddress = wm.getLocationDesc();
            mRoadName.setText(wm.getRoadName());
            if (mRoadvalue != 5) {
//                String mArea = wm.getArea() + "";
//                mAllArea.setText(mArea);
            }
            if (userRole == 5) {
//                mPlanTime.setText(wm.getTimePlan() + "");
//                mPlanCost.setText(wm.getMoneyPlan() + "");
//                mPlanList = (ArrayList<Plan>) td.getPlanFuns();
            }
            mContent.setText(wm.getDetail());
            if (mRoadvalue != 5) {
//                mEventTypeBaseList = new ArrayList<>();
//                List<TaskDetails.DiseaseMsgDtosBean> typeList = td.getDiseaseMsgDtos();
//                for (TaskDetails.DiseaseMsgDtosBean e : typeList) {
//                    EventTypeBase eb = new EventTypeBase();
//                    EventType eventType = new EventType();
//                    eventType.setId(e.getDiseaseId());
//                    eventType.setName(e.getDiseaseTypeName());
//                    eventType.setDesc(e.getDetail());
//                    eb.setEventType(eventType);
//                    ArrayList<TaskDetails.DiseaseMsgDtosBean.DiseaseAttrMsgDtosBean> attrList = (ArrayList<TaskDetails.DiseaseMsgDtosBean.DiseaseAttrMsgDtosBean>) e.getDiseaseAttrMsgDtos();
//                    ArrayList<EventAttr> eventAttrArrayList = new ArrayList<>();
//                    for (TaskDetails.DiseaseMsgDtosBean.DiseaseAttrMsgDtosBean b : attrList) {
//                        EventAttr ea = new EventAttr();
//                        ea.setName(b.getDiseaseAttrName());
//                        ea.setTypeUnitId(b.getTypeUnitId());
////                        ea.setDefaultVal(b.getValue());
//                        eventAttrArrayList.add(ea);
//                    }
//                    eb.setEventAttrsList(eventAttrArrayList);
//                    mEventTypeBaseList.add(eb);
//                }
            }
        }
    }

    @Override
    protected void initData() {
        mEventTypeBaseList = new ArrayList<>();
        mAcache = ACache.get(EventAddActivity.this);
        dialog = DialogProgress.createLoadingDialog(EventAddActivity.this, "", this);
        request = new NetWorkRequest(EventAddActivity.this, this);

        userInfo = (UserInfo) mAcache.getAsObject("UserInfo");

        if (userInfo == null || TextUtils.isEmpty(userInfo.getUserId())) {
            myToast.toast(EventAddActivity.this, "登录状态已过期，请重新登录！");
            startActivity(new Intent(EventAddActivity.this, LoginActivity.class));
            EventAddActivity.this.finish();
        }
        userRole = userInfo.getUserRole();

        if (mTask != null) {
            Map map = new HashMap();
            map.put("userId", userInfo.getUserId());
            map.put("appSid", userInfo.getAppSid());
            JSONObject object = new JSONObject();
            object.put("workOrderId", mTask.getWorkOrderId());
            object.put("taskId", mTask.getTaskId());
            map.put("body", object.toJSONString());
            request.doPostRequest(4, true, com.jinjiang.roadmaintenance.utils.Uri.getDiseaseInfos, map);
        }

        initLocadata();
        setEventTypeAdapter();
        setplanAdapter();
        mPhotoList.add(mPhotoList.size(), new File(""));
        setGridAdapter();
        if (!IsReLoad && eventId == 0) {
            mRoadName.setText(ScreenUtils.getRoad(mAddress));
        }

        if (userRole == 6) {
            mTimeLl.setVisibility(View.GONE);
        } else if (userRole == 5) {
            mTimeLl.setVisibility(View.VISIBLE);
        }

        if (mRoadvalue == 3 || mRoadvalue == 4 || mRoadvalue == 5) {
            mDriverwayTypeLl.setVisibility(View.GONE);
            if (mRoadvalue == 5) {
                mEventTypeLl.setVisibility(View.GONE);
                mAreaLl.setVisibility(View.GONE);
                mSpaceLl.setVisibility(View.GONE);
            }
        }

        mDriverwayType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.eventadd_radio1) {
                    driverwayType = 1;
                } else {
                    driverwayType = 2;
                }
            }
        });


    }

    private void setEventTypeAdapter() {
        adapter_eventtype = new CommonAdapter<EventTypeBase>(EventAddActivity.this, R.layout.item_eventtype_add, mEventTypeBaseList) {
            @Override
            protected void convert(ViewHolder viewHolder, EventTypeBase item, final int position) {
                viewHolder.setText(R.id.item_name, item.getEventType().getName());
                ArrayList<EventAttr> list = item.getEventAttrsList();
                if (list != null && list.size() > 0) {
                    if (list.size() == 1) {
                        viewHolder.setText(R.id.item_attr, list.get(0).getDefaultVal() + "m");
                    } else {
                        viewHolder.setText(R.id.item_attr, list.get(0).getDefaultVal() + "m" + "*" + list.get(1).getDefaultVal() + "m");
                    }
                }
                viewHolder.setOnClickListener(R.id.item_del, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEventTypeBaseList.remove(position);
                        adapter_eventtype.notifyDataSetChanged();
                        mAllArea.setText(getAllArea() + "m²");
                    }
                });
            }
        };
        mEventtypeListview.setAdapter(adapter_eventtype);
    }

    private void setplanAdapter() {
        adapter_plan = new CommonAdapter<Plan>(EventAddActivity.this, R.layout.item_eventtype_add, mPlanList) {
            @Override
            protected void convert(ViewHolder viewHolder, Plan item, final int position) {
                viewHolder.setText(R.id.item_name, item.getFunName());
                if (item.getId().equals("0")) {
                    viewHolder.setText(R.id.item_attr, item.getOtherDesc());
                } else {
                    viewHolder.setText(R.id.item_attr, "");
                }
                viewHolder.setOnClickListener(R.id.item_del, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPlanList.remove(position);
                        adapter_plan.notifyDataSetChanged();
                    }
                });
            }
        };
        mFanganListview.setAdapter(adapter_plan);
    }

    private void setGridAdapter() {
        LogUtils.d(mPhotoList);
        mTupianGrid.setAdapter(new CommonAdapter<File>(EventAddActivity.this, R.layout.item_addphoto_grid, mPhotoList) {
            @Override
            protected void convert(ViewHolder viewHolder, final File item, int position) {

                if (position != mPhotoList.size() - 1) {
                    GlideImgManager.glideLoader(EventAddActivity.this, "file://" + item.getAbsolutePath(), R.drawable.pic_not_found, R.drawable.pic_not_found, (ImageView) viewHolder.getView(R.id.item_addphoto_grid_img), 1);
                    viewHolder.setOnClickListener(R.id.item_addphoto_grid_del, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPhotoList.remove(item);
                            setGridAdapter();
                        }
                    });
                    viewHolder.setVisible(R.id.item_addphoto_grid_del, true);
                } else {
                    Glide.with(EventAddActivity.this).load(R.drawable.add3).into((ImageView) viewHolder.getView(R.id.item_addphoto_grid_img));
                    viewHolder.setVisible(R.id.item_addphoto_grid_del, false);
                }
            }
        });
        mTupianGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mPhotoList.size() - 1) {
                    if (mPhotoList.size() >= 5) {
                        showToast("最多上传4张图片！");
                        return;
                    }
                    ActionSheetDialog.showSheet(EventAddActivity.this, EventAddActivity.this, EventAddActivity.this);
                }
            }
        });
    }

    @OnClick({R.id.eventadd_back, R.id.eventadd_save, R.id.eventadd_eventType_know, R.id.eventadd_eventType_add, R.id.eventadd_fangan_add, R.id.eventadd_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.eventadd_back:
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
                break;
            case R.id.eventadd_save:
                orderStatus = 1;
                send();
                break;
            case R.id.eventadd_eventType_know:
                break;
            case R.id.eventadd_eventType_add:
                Intent intent = new Intent(EventAddActivity.this, EventTypeActivity.class);
                startActivityForResult(intent, 105);
                break;
            case R.id.eventadd_fangan_add:

                if (mEventTypeBaseList == null || mEventTypeBaseList.size() == 0) {
                    if (mRoadvalue != 5) {
                        showToast("请先选择病害类型！");
                        return;
                    }
                }
                StringBuffer typeIds = new StringBuffer();
                for (int i = 0; i < mEventTypeBaseList.size(); i++) {
                    if (i == 0) {
                        typeIds.append(mEventTypeBaseList.get(i).getEventType().getId());
                    } else {
                        typeIds.append("," + mEventTypeBaseList.get(i).getEventType().getId());
                    }
                }
                Intent intent2 = new Intent(EventAddActivity.this, PlanActivity.class);
                intent2.putExtra("orderType", mRoadvalue);
                intent2.putExtra("diseaseTypeId", typeIds.toString());
                startActivityForResult(intent2, 106);
                break;
            case R.id.eventadd_send:
                orderStatus = 2;
                send();
                break;
        }
    }

    /**
     * 保存或发送
     */
    private void send() {
        if (orderStatus == 2 && IsNotNull()) {
            return;
        }
        HashMap map = new HashMap();
        map.put("userId", userInfo.getUserId());
        map.put("appSid", userInfo.getAppSid());
        if (IsReLoad) {
            map.put("workOrderId", mTaskDetails.getWorkOrderMsgDto().getWorkOrderId() + "");
            map.put("taskId", mTaskDetails.getTaskId());
        }
        JSONObject object = new JSONObject();
        object.put("orderType", mRoadvalue);
        if (mRoadvalue == 1 || mRoadvalue == 2) {
            object.put("lineType", driverwayType);
        }
        if (mCenpt != null) {
            object.put("longitude", mCenpt.longitude);
            object.put("latitude", mCenpt.latitude);
        } else {
            object.put("longitude", longitude);
            object.put("latitude", latitude);
        }
        object.put("locationDesc", mAddress);
        object.put("roadName", mRoadName.getText().toString());
        if (mRoadvalue != 5) {
            object.put("area", getAllArea());
        }
        if (userRole == 5) {
            StringBuffer plans = new StringBuffer();
            for (Plan p : mPlanList) {
                if (!p.getId().equals("0")) {
                    if (plans.length() == 0) {
                        plans.append(p.getId());
                    } else {
                        plans.append("," + p.getId());
                    }
                } else {
                    object.put("maintainDetailPlan", p.getOtherDesc());
                }
            }
            object.put("maintainFunIds", plans.toString());
            object.put("timePlan", mPlanTime.getText().toString());
            object.put("moneyPlan", mPlanCost.getText().toString());
        }
        if (!IsReLoad)
            object.put("orderStatus", "2");
        object.put("detail", mContent.getText().toString());
        map.put("workOrder", object.toJSONString());
        if (mRoadvalue != 5) {
            JSONArray array = new JSONArray();
            JSONArray array2 = new JSONArray();
            for (EventTypeBase e : mEventTypeBaseList) {
                JSONObject object2 = new JSONObject();
                EventType type = e.getEventType();
                ArrayList<EventAttr> attrsList = e.getEventAttrsList();
                object2.put("diseaseType", type.getId());
                object2.put("detail", type.getDesc());
                array.add(object2);
                for (EventAttr a : attrsList) {
                    JSONObject object3 = new JSONObject();
                    object3.put("diseaseType", type.getId());
                    object3.put("typeUnitId", a.getTypeUnitId());
                    object3.put("value", a.getDefaultVal());
                    array2.add(object3);
                }
            }
            map.put("disease", array.toJSONString());
            map.put("diseaseAttr", array2.toJSONString());
        }
        if (orderStatus == 2) {
//            if (IsReLoad){
//                request.doPostUpload(0, true, com.jinjiang.roadmaintenance.utils.Uri.readdDisease, map, "files", mPhotoList);
//            }else {
                request.doPostUpload(0, true, com.jinjiang.roadmaintenance.utils.Uri.addDisease, map, "files", mPhotoList);
//            }
        } else if (orderStatus == 1) {
            if (eventId == 0) {
                mEventdata = new SaveEventData();
                mEventdata.id = System.currentTimeMillis();
            } else {
                SQLite.delete(FileUri.class)
                        .where(FileUri_Table.eventId.is(eventId))
                        .query();
                SQLite.delete(EventType.class)
                        .where(EventType_Table.eventId.is(eventId))
                        .query();
                SQLite.delete(EventAttr.class)
                        .where(EventAttr_Table.eventId.is(eventId))
                        .query();
                SQLite.delete(Plan.class)
                        .where(Plan_Table.eventId.is(eventId))
                        .query();
            }

            mEventdata.userId = userInfo.getUserId();
            mEventdata.appSid = userInfo.getAppSid();
            mEventdata.orderType = mEventTypeBaseList.get(0).getEventType().getOrderType();
            if (mRoadvalue == 1 || mRoadvalue == 2) {
                mEventdata.lineType = driverwayType;
            }
            if (mCenpt != null) {
                mEventdata.longitude = mCenpt.longitude;
                mEventdata.latitude = mCenpt.latitude;
            } else {
                mEventdata.longitude = longitude;
                mEventdata.latitude = latitude;
            }
            mEventdata.locationDesc = mAddress;
            mEventdata.roadName = mRoadName.getText().toString();
            if (mRoadvalue != 5) {
                mEventdata.area = getAllArea();
            }
            if (userRole == 5) {
                for (Plan p : mPlanList) {
                    p.setEventId(mEventdata.id);
                    p.save();
                }
                mEventdata.timePlan = mPlanTime.getText().toString();
                mEventdata.moneyPlan = mPlanCost.getText().toString();
            }
            mEventdata.detail = mContent.getText().toString();
            mEventdata.roadvalue = mRoadvalue;

            for (File f : mPhotoList) {
                FileUri fileUri = new FileUri();
                fileUri.eventId = mEventdata.id;
                fileUri.uri = f.getAbsolutePath();
                fileUri.save();
            }

            if (mRoadvalue != 5) {
                for (EventTypeBase e : mEventTypeBaseList) {
                    EventType type = e.getEventType();
                    ArrayList<EventAttr> attrsList = e.getEventAttrsList();
                    type.setEventId(mEventdata.id);
                    type.save();
                    for (EventAttr a : attrsList) {
                        a.setEventId(mEventdata.id);
                        a.setEventTypeId(type.getId());
                        a.save();
                    }
                }
            }
            if (eventId == 0) {
                mEventdata.save();
            } else {
                mEventdata.update();
            }
            showToast("保存成功！");
            EventBus.getDefault().post(new MessageEvent(1, ""));
            finish();
            overridePendingTransition(0, R.anim.base_slide_out);
        }
    }

    /**
     * 非空判断
     */
    private boolean IsNotNull() {
        if (mPhotoList == null || mPhotoList.size() == 0 || mPhotoList.size() == 1) {
            showToast("请选择病害图片！");
            return true;
        }
        if (TextUtils.isEmpty(mRoadName.getText().toString())) {
            showToast("请输入道路名称！");
            return true;
        }
        if (mRoadvalue != 5) {
            if (mEventTypeBaseList == null || mEventTypeBaseList.size() == 0) {
                showToast("请选择病害类型！");
                return true;
            }
        }
        if (userRole == 5) {
            if (mPlanList == null || mPlanList.size() == 0) {
                showToast("请选择施工方案！");
                return true;
            }
            if (TextUtils.isEmpty(mPlanTime.getText().toString())) {
                showToast("请输入预计工期！");
                return true;
            }
            if (TextUtils.isEmpty(mPlanCost.getText().toString())) {
                showToast("请输入预计维修费用！");
                return true;
            }
        }

//        if (TextUtils.isEmpty(mContent.getText().toString())) {
//            showToast("请输入备注！");
//            return true;
//        }
        return false;
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
                Uri uri = Uri.fromFile(new File(cameraPath));
                // 设置系统相机拍摄照片完成后图片文件的存放地址
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 100);
            } else {
                myToast.toast(EventAddActivity.this, "请确认已经插入SD卡!");
            }
        } else if (whichButton == 1) {
            Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");
            pickIntent.setAction(Intent.ACTION_PICK);
            startActivityForResult(pickIntent, 102);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                if (mPhotoList.size() > 4) {
                    showToast("最多上传4张图片！");
                    return;
                }
                LogUtils.d("path=" + cameraPath);
                File file = new File(cameraPath);
                File Compressfile = CompressImage.getSmallFile(cameraPath, savePath, file.getName());
                mPhotoList.add(mPhotoList.size() - 1, Compressfile);
                setGridAdapter();
            } else if (requestCode == 102) {
                if (mPhotoList.size() > 4) {
                    showToast("最多上传4张图片！");
                    return;
                }
                try {
                    Uri uri = data.getData();
                    LogUtils.d(uri);
                    final String absolutePath = getRealFilePath(EventAddActivity.this, uri);
//                    LogUtils.d("path=" + absolutePath);
//                    LogUtils.d("path=" + data);
                    File file = new File(absolutePath);
                    File Compressfile = CompressImage.getSmallFile(file.getAbsolutePath(), savePath, file.getName());
                    if (!mPhotoList.contains(Compressfile)) {
                        mPhotoList.add(mPhotoList.size() - 1, Compressfile);
                        setGridAdapter();
                    } else {
                        myToast.toast(EventAddActivity.this, "已选择该相片！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 105) {
                mEventTypeBaseList.add((EventTypeBase) data.getSerializableExtra("EventTypeBase"));
                adapter_eventtype.notifyDataSetChanged();
                mAllArea.setText(getAllArea() + "m²");
            } else if (requestCode == 106) {
                ArrayList<Plan> planList = (ArrayList<Plan>) data.getSerializableExtra("planList");
                for (Plan p : planList) {
                    boolean isExist = false;
                    for (Plan p2 : mPlanList) {
                        if (p.getId().equals(p2.getId())) {
                            isExist = true;// 找到相同项，跳出本层循环
                            break;
                        }
                    }
                    if (!isExist) {// 不相同，加入list3中
                        mPlanList.add(p);
                    }
                }
                adapter_plan.notifyDataSetChanged();
                LogUtils.d(mPlanList);
            }
        }
    }

    private String getAllArea() {
        float Allarea = 0;
        for (EventTypeBase e : mEventTypeBaseList) {
            if (e.getEventAttrsList().size() >= 3) {
                Allarea += Float.parseFloat(e.getEventAttrsList().get(2).getDefaultVal());
            }
        }
        DecimalFormat fnum = new DecimalFormat("##0.00");
        return fnum.format(Allarea);
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
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
    public void loadDataFinish(int code, Object data) {
        if (code == 0) {

            showToast("操作成功！");
            if (eventId != 0) {
                new Select().from(SaveEventData.class).where(SaveEventData_Table.id.eq(eventId)).querySingle().delete();
                SQLite.delete(FileUri.class)
                        .where(FileUri_Table.eventId.is(eventId))
                        .query();
                SQLite.delete(EventType.class)
                        .where(EventType_Table.eventId.is(eventId))
                        .query();
                SQLite.delete(EventAttr.class)
                        .where(EventAttr_Table.eventId.is(eventId))
                        .query();
                SQLite.delete(Plan.class)
                        .where(Plan_Table.eventId.is(eventId))
                        .query();

                EventBus.getDefault().post(new MessageEvent(1, ""));
            }
            if (userRole == 5) {
                EventBus.getDefault().post(new MessageEvent(1, ""));
            }
            finish();
        } else if (code == 4) {
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
        myToast.toast(EventAddActivity.this, message);
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
