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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apkfuns.logutils.LogUtils;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
import com.jinjiang.roadmaintenance.data.EventAttr;
import com.jinjiang.roadmaintenance.data.EventType;
import com.jinjiang.roadmaintenance.data.EventTypeBase;
import com.jinjiang.roadmaintenance.data.MessageEvent;
import com.jinjiang.roadmaintenance.data.Plan;
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
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

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
    private HashMap mTask;
    private boolean IsModify = false;
    private long taskId;
    private double longitude;
    private double latitude;
    private ArrayList<HashMap> taskList;
    ArrayList<Plan> mPlanList;
    String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tempfile/";
    private CommonAdapter<Plan> adapter_plan;

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
        if (intent.hasExtra("Task")) {
            mTask = (HashMap) intent.getSerializableExtra("Task");
            IsModify = true;
        }
    }

    private void initLocadata() {
        if (mTask != null) {
            taskId = (long) mTask.get("id");
            JSONObject object = JSONObject.parseObject((String) mTask.get("workOrder"));
            longitude = object.getDouble("longitude");
            latitude = object.getDouble("latitude");
            mRoadvalue = (int) mTask.get("mRoadvalue");
            if (mRoadvalue == 1 || mRoadvalue == 2) {
                driverwayType = object.getInteger("lineType");
                if (driverwayType == 1) {
                    mRadio1.setChecked(true);
                    mRadio2.setChecked(false);
                } else {
                    mRadio1.setChecked(false);
                    mRadio2.setChecked(true);
                }
            }
            mAddress = object.getString("locationDesc");
            mRoadName.setText(mAddress);
            if (mRoadvalue != 5) {
                String mArea = object.getString("area");
                mAllArea.setText(mArea);
            }
            if (userRole == 5) {
                String timePlan = object.getString("timePlan");
                String moneyPlan = object.getString("moneyPlan");
                mPlanTime.setText(timePlan);
                mPlanCost.setText(moneyPlan);
                mPlanList= (ArrayList<Plan>) mTask.get("plans");
            }
            String detail = object.getString("detail");
            mContent.setText(detail);
            if (mRoadvalue != 5) {
                mEventTypeBaseList = (ArrayList<EventTypeBase>) mTask.get("mEventTypeBaseList");
                LogUtils.d(mEventTypeBaseList);
            }
            ArrayList<String> filelist = (ArrayList<String>) mTask.get("files");
            if (filelist != null) {
                for (String s : filelist) {
                    if (!TextUtils.isEmpty(s) && s.length() > 5)
                        mPhotoList.add(new File(s));
                }
            }
        }
    }

    @Override
    protected void initData() {
        mEventTypeBaseList = new ArrayList<>();
        mAcache = ACache.get(EventAddActivity.this);
        taskList = (ArrayList<HashMap>) mAcache.getAsObject("taskList");
        dialog = DialogProgress.createLoadingDialog(EventAddActivity.this, "", this);
        request = new NetWorkRequest(EventAddActivity.this, this);

        userInfo = (UserInfo) mAcache.getAsObject("UserInfo");

        if (userInfo == null || TextUtils.isEmpty(userInfo.getUserId())) {
            myToast.toast(EventAddActivity.this, "登录状态已过期，请重新登录！");
            startActivity(new Intent(EventAddActivity.this, LoginActivity.class));
            EventAddActivity.this.finish();
        }
        userRole = userInfo.getUserRole();

        initLocadata();
        setEventTypeAdapter();
        setplanAdapter();
        mPhotoList.add(mPhotoList.size(), new File(""));
        setGridAdapter();
        mRoadName.setText(mAddress);

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
                viewHolder.setText(R.id.item_attr, "");
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
                    Glide.with(EventAddActivity.this).load("file://" + item.getAbsolutePath()).into((ImageView) viewHolder.getView(R.id.item_addphoto_grid_img));
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
                    ActionSheetDialog.showSheet(EventAddActivity.this, EventAddActivity.this, EventAddActivity.this);
                }
            }
        });
    }

    @OnClick({R.id.eventadd_back, R.id.eventadd_save, R.id.eventadd_eventType_know, R.id.eventadd_eventType_add,R.id.eventadd_fangan_add, R.id.eventadd_send})
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
                Intent intent2 = new Intent(EventAddActivity.this, PlanActivity.class);
                intent2.putExtra("orderType",mRoadvalue);
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
        if (!IsNotNull()) {
            HashMap map = new HashMap();
            map.put("userId", userInfo.getUserId());
            map.put("appSid", userInfo.getAppSid());
            JSONObject object = new JSONObject();
            object.put("orderType", mEventTypeBaseList.get(0).getEventType().getOrderType());
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
            if (mRoadvalue != 5) {
                object.put("area", getAllArea());
            }
            if (userRole == 5) {
                StringBuffer plans = new StringBuffer();
                for (Plan p :mPlanList){
                    if (!p.getId().equals("qita")) {
                        if (plans.length() == 0) {
                            plans.append(p.getId());
                        } else {
                            plans.append("," + p.getId());
                        }
                    }else {
                        object.put("maintainDetailPlan", p.getFunDetail());
                    }
                }
                object.put("maintainFunIds", plans.toString());
                object.put("timePlan", mPlanTime.getText().toString());
                object.put("moneyPlan", mPlanCost.getText().toString());
            }
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
                request.doPostUpload(0, true, com.jinjiang.roadmaintenance.utils.Uri.addDisease, map, "files", mPhotoList);
            } else if (orderStatus == 1) {
                ArrayList<String> list = new ArrayList<>();
                for (File f : mPhotoList) {
                    list.add(f.getAbsolutePath());
                }
                map.put("files", list);
                map.put("plans", mPlanList);
                map.put("mRoadvalue", mRoadvalue);
                map.put("id", System.currentTimeMillis());
                map.put("mEventTypeBaseList", mEventTypeBaseList);

                if (taskList != null) {
                    if (taskId != 0) {
                        for (int i = 0; i < taskList.size(); i++) {
                            if (taskId == (long) taskList.get(i).get("id")) {
                                LogUtils.d("删除");
                                taskList.remove(i);
                            }
                        }
                    }
                } else {
                    taskList = new ArrayList<>();
                }
                taskList.add(map);
                mAcache.put("taskList", taskList);
                showToast("保存成功！");
                EventBus.getDefault().post(new MessageEvent(0, ""));
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
            }

        }
    }

    /**
     * 非空判断
     */
    private boolean IsNotNull() {
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
            if (mPlanList==null||mPlanList.size()==0) {
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

        if (TextUtils.isEmpty(mContent.getText().toString())) {
            showToast("请输入备注！");
            return true;
        }
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
                if (mPhotoList.size() > 8) {
                    showToast("单次最多上传8张图片！");
                    return;
                }
                LogUtils.d("path=" + cameraPath);
                File file = new File(cameraPath);
                File Compressfile = CompressImage.getSmallFile(cameraPath, savePath, file.getName());
                mPhotoList.add(mPhotoList.size() - 1, Compressfile);
                setGridAdapter();
            } else if (requestCode == 102) {
                if (mPhotoList.size() > 8) {
                    showToast("单次最多上传8张图片！");
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
                for (Plan p :planList){
                    boolean isExist = false;
                    for (Plan p2:mPlanList){
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
        showToast("操作成功！");
        if (taskId != 0) {
            for (int i = 0; i < taskList.size(); i++) {
                if (taskId == (long) taskList.get(i).get("id")) {
                    taskList.remove(i);
                    LogUtils.d("del");
                }
            }
            mAcache.put("taskList", taskList);
        }
        finish();
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
