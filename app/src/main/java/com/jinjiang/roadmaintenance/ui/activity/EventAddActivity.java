package com.jinjiang.roadmaintenance.ui.activity;

import android.app.Activity;
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
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
import com.jinjiang.roadmaintenance.ui.view.ActionSheetDialog;
import com.jinjiang.roadmaintenance.ui.view.ListViewForScrollView;
import com.jinjiang.roadmaintenance.ui.view.MyGridView;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;

public class EventAddActivity extends BaseActivity implements ActionSheetDialog.OnActionSheetSelected, DialogInterface.OnCancelListener {

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
    @BindView(R.id.eventadd_plan_listview)
    ListViewForScrollView mPlanListview;
    @BindView(R.id.eventadd_time)
    EditText mPlanTime;
    @BindView(R.id.eventadd_cost)
    EditText mPlanCost;
    @BindView(R.id.eventadd_content)
    EditText mContent;
    private OptionPicker picker_Type;
    private int eventType = 0;
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
        return R.layout.activity_event_add;
    }

    @Override
    protected void initUI() {
        ButterKnife.bind( this ) ;
    }

    @Override
    protected void initData() {
        mPhotoList = new ArrayList<>();

        initOptionPicker();
        mDriverwayType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.eventadd_radio1) {

                } else {

                }
            }
        });
    }

    private void setGridAdapter() {
        mTupianGrid.setAdapter(new CommonAdapter<File>(EventAddActivity.this, R.layout.item_addphoto_grid, mPhotoList) {
            @Override
            protected void convert(ViewHolder viewHolder, final File item, int position) {
                ((SimpleDraweeView) viewHolder.getView(R.id.item_addphoto_grid_img)).setImageURI(Uri.parse("file://" + item.getAbsolutePath()));
                viewHolder.setOnClickListener(R.id.item_addphoto_grid_del, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPhotoList.remove(item);
                        setGridAdapter();
                    }
                });
            }
        });
    }

    @OnClick({R.id.eventadd_back, R.id.eventadd_save, R.id.eventadd_eventType_know, R.id.eventadd_eventType_add, R.id.eventadd_planAdd, R.id.eventadd_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.eventadd_back:
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
                break;
            case R.id.eventadd_save:
                break;
            case R.id.eventadd_eventType_know:
                break;
            case R.id.eventadd_eventType_add:
                ActionSheetDialog.showSheet(EventAddActivity.this, this, this);
                break;
            case R.id.eventadd_planAdd:
                break;
            case R.id.eventadd_send:
                break;
        }
    }

    /**
     * 初始化类型选择器
     */
    private void initOptionPicker() {
        ArrayList<String> type_list = new ArrayList<>();
        type_list.add("沥青路面");
        type_list.add("水泥路面");
        type_list.add("人行道");
        type_list.add("井盖");
        type_list.add("其他");
        picker_Type = null;
        picker_Type = new OptionPicker(EventAddActivity.this, type_list);
        picker_Type.setOffset(1);
        picker_Type.setCycleDisable(true);
        picker_Type.setSelectedIndex(1);
        picker_Type.setTextSize(15);
        picker_Type.setLineColor(EventAddActivity.this.getResources().getColor(R.color.blue));
        picker_Type.setTextColor(EventAddActivity.this.getResources().getColor(R.color.text_black));
        picker_Type.setCancelText("取消");
        picker_Type.setCancelTextColor(EventAddActivity.this.getResources().getColor(R.color.gray_deep));
        picker_Type.setSubmitText("确定");
        picker_Type.setSubmitTextColor(EventAddActivity.this.getResources().getColor(R.color.blue));
        picker_Type.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int position, String option) {
                eventType = position;
//                mRoadType_tv.setText(option);
                if (position == 0) {
//                    mDriverwayType_ll.setVisibility(View.VISIBLE);
//                    mEventType_ll.setVisibility(View.VISIBLE);
//                    mProperty_ll.setVisibility(View.VISIBLE);
                } else if (position == 1) {
//                    mDriverwayType_ll.setVisibility(View.VISIBLE);
//                    mEventType_ll.setVisibility(View.VISIBLE);
//                    mProperty_ll.setVisibility(View.VISIBLE);
                } else if (position == 2) {
//                    mDriverwayType_ll.setVisibility(View.GONE);
//                    mEventType_ll.setVisibility(View.VISIBLE);
//                    mProperty_ll.setVisibility(View.VISIBLE);

                } else if (position == 3) {
//                    mDriverwayType_ll.setVisibility(View.GONE);
//                    mEventType_ll.setVisibility(View.VISIBLE);
//                    mProperty_ll.setVisibility(View.VISIBLE);

                } else {
//                    mDriverwayType_ll.setVisibility(View.GONE);
//                    mEventType_ll.setVisibility(View.GONE);
//                    mProperty_ll.setVisibility(View.GONE);
                }
            }
        });
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
                LogUtils.d("path=" + cameraPath);
                File file = new File(cameraPath);
                mPhotoList.add(file);
                setGridAdapter();
            }
            if (requestCode == 102) {
                try {
                    Uri uri = data.getData();
                    LogUtils.d(uri);
                    final String absolutePath = getRealFilePath(EventAddActivity.this, uri);
                    LogUtils.d("path=" + absolutePath);
                    LogUtils.d("path=" + data);
                    File file = new File(absolutePath);
                    if (!mPhotoList.contains(file)) {
                        mPhotoList.add(file);
                        setGridAdapter();
                    } else {
                        myToast.toast(EventAddActivity.this, "已选择该相片！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        LogUtils.d(data);
        if (requestCode == 105 && resultCode == 106) {
            LogUtils.d(data);
//            mRoadName_tv.setText(data.getStringExtra("roadname"));
//            mLocation_tv.setText(data.getStringExtra("location"));
        }
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


}
