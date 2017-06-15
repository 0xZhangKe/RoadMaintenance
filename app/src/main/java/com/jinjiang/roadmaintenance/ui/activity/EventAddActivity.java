package com.jinjiang.roadmaintenance.ui.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
import com.jinjiang.roadmaintenance.ui.view.ActionSheetDialog;
import com.jinjiang.roadmaintenance.ui.view.MyGridView;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.io.File;
import java.util.ArrayList;

import cn.qqtheme.framework.picker.OptionPicker;

public class EventAddActivity extends BaseActivity implements ActionSheetDialog.OnActionSheetSelected,DialogInterface.OnCancelListener{

    private ArrayList<String> type_list;
    private OptionPicker picker_Type;
    private int eventType = 0;
    private TextView mRoadType_tv;
    private TextView mLocation_tv;
    private TextView mEventType_tv;
    private EditText mRoadName_tv;
    private EditText mLong;
    private EditText mWide;
    private EditText mArea;
    private EditText mContent;
    private MyGridView mGridView;
    private RadioGroup mDriverwayType;
    private LinearLayout mDriverwayType_ll;
    private LinearLayout mEventType_ll;
    private LinearLayout mProperty_ll;
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

        findViewById(R.id.eventadd_back).setOnClickListener(this);
        findViewById(R.id.eventadd_roadType_ll).setOnClickListener(this);
        findViewById(R.id.eventadd_location_ll).setOnClickListener(this);
        findViewById(R.id.eventadd_tupian_ll).setOnClickListener(this);
        findViewById(R.id.eventadd_send).setOnClickListener(this);
        findViewById(R.id.eventadd_save).setOnClickListener(this);

        mDriverwayType_ll = (LinearLayout)findViewById(R.id.eventadd_driverwayType_ll);
        mEventType_ll = (LinearLayout)findViewById(R.id.eventadd_eventType_ll);
        mProperty_ll = (LinearLayout)findViewById(R.id.eventadd_property_ll);

        mEventType_ll.setOnClickListener(this);

        mRoadType_tv = (TextView)findViewById(R.id.eventadd_roadType_tv);
        mLocation_tv = (TextView)findViewById(R.id.eventadd_location_tv);
        mEventType_tv = (TextView)findViewById(R.id.eventadd_eventType_tv);

        mRoadName_tv = (EditText)findViewById(R.id.eventadd_roadName_tv);
        mLong = (EditText)findViewById(R.id.eventadd_long);
        mWide = (EditText)findViewById(R.id.eventadd_wide);
        mArea = (EditText)findViewById(R.id.eventadd_area);
        mContent = (EditText)findViewById(R.id.eventadd_content);
        
        mGridView = (MyGridView)findViewById(R.id.eventadd_grid_tupian);
        
        mDriverwayType = (RadioGroup)findViewById(R.id.eventadd_driverwayType);


    }

    @Override
    protected void initData() {
        mPhotoList = new ArrayList<>();

        initOptionPicker();
        mDriverwayType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.eventadd_radio1){

                }else {

                }
            }
        });
    }

    private void setGridAdapter(){
        mGridView.setAdapter(new CommonAdapter<File>(EventAddActivity.this,R.layout.item_addphoto_grid,mPhotoList) {
            @Override
            protected void convert(ViewHolder viewHolder, final File item, int position) {
                ((SimpleDraweeView)viewHolder.getView(R.id.item_addphoto_grid_img)).setImageURI(Uri.parse("file://" + item.getAbsolutePath()));
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.eventadd_back:
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
                break;
            case R.id.eventadd_roadType_ll:
                picker_Type.show();
                break;
            case R.id.eventadd_location_ll:
                Intent intent = new Intent(this, LocationSelectActivity.class);
                startActivityForResult(intent,105);
                break;
            case R.id.eventadd_eventType_ll:
                break;
            case R.id.eventadd_tupian_ll:
                ActionSheetDialog.showSheet(EventAddActivity.this,this,this);
                break;
            case R.id.eventadd_send:
                break;
            case R.id.eventadd_save:
                break;
            default:
                break;
        }
    }


    /**
     * 初始化类型选择器
     */
    private void initOptionPicker() {
        type_list = new ArrayList<>();
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
                mRoadType_tv.setText(option);
                if (position == 0) {
                    mDriverwayType_ll.setVisibility(View.VISIBLE);
                    mEventType_ll.setVisibility(View.VISIBLE);
                    mProperty_ll.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    mDriverwayType_ll.setVisibility(View.VISIBLE);
                    mEventType_ll.setVisibility(View.VISIBLE);
                    mProperty_ll.setVisibility(View.VISIBLE);
                } else if (position==2){
                    mDriverwayType_ll.setVisibility(View.GONE);
                    mEventType_ll.setVisibility(View.VISIBLE);
                    mProperty_ll.setVisibility(View.VISIBLE);

                }else if (position==3){
                    mDriverwayType_ll.setVisibility(View.GONE);
                    mEventType_ll.setVisibility(View.VISIBLE);
                    mProperty_ll.setVisibility(View.VISIBLE);

                }else {
                    mDriverwayType_ll.setVisibility(View.GONE);
                    mEventType_ll.setVisibility(View.GONE);
                    mProperty_ll.setVisibility(View.GONE);
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
            }else {
                myToast.toast(EventAddActivity.this,"请确认已经插入SD卡!");
            }
        } else if (whichButton == 1) {
            Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");
            pickIntent.setAction(Intent.ACTION_GET_CONTENT);
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
                    final String absolutePath= getRealFilePath(EventAddActivity.this, uri);
                    LogUtils.d("path=" + absolutePath);
                    LogUtils.d("path=" + data);
                    File file = new File(absolutePath);
                    if (!mPhotoList.contains(file)){
                        mPhotoList.add(file);
                        setGridAdapter();
                    }else {
                        myToast.toast(EventAddActivity.this,"已选择该相片！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        if (requestCode == 105) {
//            LogUtils.d(data);
            mRoadName_tv.setText(data.getStringExtra("roadname"));
            mLocation_tv.setText(data.getStringExtra("location"));
        }
    }
    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
