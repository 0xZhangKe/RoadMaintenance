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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
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
import com.jinjiang.roadmaintenance.utils.Uri;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 病害详情
 */
public class EventDetailsActivity extends BaseActivity implements UIDataListener, ActionSheetDialog.OnActionSheetSelected, DialogInterface.OnCancelListener {


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
    private Task mTask;
    private ACache mAcache;
    private Dialog dialog;
    private NetWorkRequest request;
    private UserInfo userInfo;
    private TaskDetails mTaskDetails;
    private int userRole;
    private CommonAdapter<String> mGridTupianAdapter;
    private CommonAdapter<TaskDetails.DiseaseMsgDtosBean> adapter_eventtype;

    public static String SAVED_IMAGE_DIR_PATH =
            Environment.getExternalStorageDirectory().getPath()
                    + "/RM/camera/";// 拍照路径
    private String cameraPath;
    private ArrayList<File> mPhotoList;

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

    @Override
    protected void initData() {
        mPhotoList = new ArrayList<>();
    }

    /**
     * 设置界面数据
     */
    private void setUiData(TaskDetails td) {


        TaskDetails.WorkOrderMsgDtoBean wm = td.getWorkOrderMsgDto();
        if (wm.getOrderStatus() == 2) {
            mDriverwayTypeRg.setVisibility(View.VISIBLE);
            mPlansAdd.setVisibility(View.VISIBLE);
            mEventTypeAdd.setVisibility(View.VISIBLE);
            mDriverwayTypeRg.setVisibility(View.VISIBLE);
            mDriverwayTypeTv.setVisibility(View.GONE);
            mPhotoList.add(new File(""));
            mPlanTime.setEnabled(true);
            mPlanCost.setEnabled(true);
            mRemark.setEnabled(true);
        }


        mEventId.setText(td.getTaskId());
        mSaveDate.setText(td.getTaskCreateTime());
        mRoadName.setText(wm.getLocationDesc());
        mLocation.setText(wm.getLocationDesc());
        mRoadType.setText(wm.getOrderTypeName());
        mDriverwayTypeTv.setText(wm.getLineTypeName());
        mAllArea.setText(wm.getArea() + "m²");
        mPlanTime.setText(wm.getTimePlan() + "");
        mPlanCost.setText(wm.getMoneyPlan() + "元");
        mRemark.setText(wm.getDetail());

        if (wm.getLineType() == 1) {
            mRadio1.setChecked(true);
            mRadio2.setChecked(false);
        } else {
            mRadio1.setChecked(false);
            mRadio2.setChecked(true);
        }
//         mSavePerson.setText(td.getTaskCreateTime());
//        TextView mPlansTv;
//        ListViewForScrollView mPlansAddListview;
//        mApprovalStateTv.setText("");
//        ListViewForScrollView mApprovalStateLv;

        setGridAdapter(wm.getPicUrls());
        setEventtypeListAdapter(wm.getOrderStatus(),td.getDiseaseMsgDtos());
    }

    private void setEventtypeListAdapter(final int OrderStatus, final List<TaskDetails.DiseaseMsgDtosBean> list) {
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
                if (OrderStatus==2){
                    viewHolder.setOnClickListener(R.id.item_del, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            list.remove(position);
                            adapter_eventtype.notifyDataSetChanged();
                        }
                    });
                }else {
                    viewHolder.setVisible(R.id.item_del,false);
                }
            }
        };
        mEventtypeListview.setAdapter(adapter_eventtype);
    }

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
                myToast.toast(EventDetailsActivity.this, "请确认已经插入SD卡!");
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

    @OnClick({R.id.eventdetails_back, R.id.eventdetails_save, R.id.eventdetails_eventType_add, R.id.eventdetails_plansAdd, R.id.eventdetails_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.eventdetails_back:
                finish();
                break;
            case R.id.eventdetails_save:
                break;
            case R.id.eventdetails_eventType_add:
                Intent intent = new Intent(EventDetailsActivity.this, EventTypeActivity.class);
                startActivityForResult(intent, 105);
                break;
            case R.id.eventdetails_plansAdd:
                break;
            case R.id.eventdetails_send:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                LogUtils.d("path=" + cameraPath);
                File file = new File(cameraPath);
                mPhotoList.add(mPhotoList.size() - 1, file);
//                setGridAdapter();
            } else if (requestCode == 102) {
                try {
                    android.net.Uri uri = data.getData();
                    LogUtils.d(uri);
                    final String absolutePath = getRealFilePath(EventDetailsActivity.this, uri);
                    LogUtils.d("path=" + absolutePath);
                    LogUtils.d("path=" + data);
                    File file = new File(absolutePath);
                    if (!mPhotoList.contains(file)) {
                        mPhotoList.add(mPhotoList.size() - 1, file);
//                        setGridAdapter();
                    } else {
                        myToast.toast(EventDetailsActivity.this, "已选择该相片！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 105) {
//                mEventTypeBaseList.add((EventTypeBase) data.getSerializableExtra("EventTypeBase"));
//                adapter_eventtype.notifyDataSetChanged();
//                mAllArea.setText(getAllArea() + "m²");
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
}
