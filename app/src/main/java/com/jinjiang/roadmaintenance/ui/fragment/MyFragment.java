package com.jinjiang.roadmaintenance.ui.fragment;


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
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.data.RoadType;
import com.jinjiang.roadmaintenance.data.UserInfo;
import com.jinjiang.roadmaintenance.model.NetWorkRequest;
import com.jinjiang.roadmaintenance.model.UIDataListener;
import com.jinjiang.roadmaintenance.ui.activity.EventAddActivity;
import com.jinjiang.roadmaintenance.ui.activity.LoginActivity;
import com.jinjiang.roadmaintenance.ui.activity.ModifyPhoneActivity;
import com.jinjiang.roadmaintenance.ui.view.ActionSheetDialog;
import com.jinjiang.roadmaintenance.ui.view.DialogProgress;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.jinjiang.roadmaintenance.utils.ACache;
import com.jinjiang.roadmaintenance.utils.CompressImage;
import com.jinjiang.roadmaintenance.utils.GlideImgManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 我的
 */
public class MyFragment extends Fragment implements ActionSheetDialog.OnActionSheetSelected, DialogInterface.OnCancelListener, UIDataListener {
    private static MyFragment fragment;
    @BindView(R.id.personalCenter_img)
    ImageView mImg;
    @BindView(R.id.personalCenter_name)
    TextView mName;
    @BindView(R.id.personalCenter_job)
    TextView mJob;
    @BindView(R.id.personalCenter_mobilephone)
    TextView mMobilephone;
    @BindView(R.id.personalCenter_mail)
    TextView mMail;
    @BindView(R.id.personalCenter_gender)
    TextView mGender;
    Unbinder unbinder;
    private UserInfo userInfo;
    private ACache mAcache;
    public static String SAVED_IMAGE_DIR_PATH =
            Environment.getExternalStorageDirectory().getPath()
                    + "/RM/camera/";// 拍照路径
    String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tempfile/";
    private String cameraPath;
    private Dialog dialog;
    private NetWorkRequest request;

    public MyFragment() {
    }

    public static MyFragment newInstance() {

        if (fragment == null) {
            fragment = new MyFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        dialog = DialogProgress.createLoadingDialog(getActivity(), "", this);
        request = new NetWorkRequest(getActivity(), this);
        mAcache = ACache.get(getActivity());
        userInfo = (UserInfo) mAcache.getAsObject("UserInfo");

        if (userInfo == null || TextUtils.isEmpty(userInfo.getAppSid())) {
            myToast.toast(getActivity(), "登录状态已过期，请重新登录！");
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }

    private void initData() {
        mName.setText(userInfo.getRealName());
        mMobilephone.setText(userInfo.getUserTel());
        mJob.setText(userInfo.getUserRoleName());
        mMail.setText(userInfo.getUserEmail());
        mGender.setText(userInfo.getGender() == 0 ? "女" : "男");
        GlideImgManager.glideLoader(getActivity(), userInfo.getHeadPhoto(), R.drawable.pic_not_found, R.drawable.pic_not_found, mImg, 0);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.personalCenter_img, R.id.personalCenter_mobilephone, R.id.personalCenter_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.personalCenter_img:
                ActionSheetDialog.showSheet(getActivity(), this, this);
                break;
            case R.id.personalCenter_mobilephone:
                startActivity(new Intent(getActivity(), ModifyPhoneActivity.class));
                break;
            case R.id.personalCenter_exit:
                mAcache.remove("UserInfo");
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
        }
    }

    private void modifyHead(File file){
        Map map = new HashMap();
        map.put("userId", userInfo.getUserId());
        map.put("appSid", userInfo.getAppSid());
        request.doPostUploadone(0, true, com.jinjiang.roadmaintenance.utils.Uri.uploadHeader, map,"file",file);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (data != null) {
            String url = (String) data;
            userInfo.setHeadPhoto(url);
            GlideImgManager.glideLoader(getActivity(), userInfo.getHeadPhoto(), R.drawable.pic_not_found, R.drawable.pic_not_found, mImg, 0);
            mAcache.put("UserInfo", userInfo);
            showToast("修改成功！");
        }
    }
    @Override
    public void showToast(String message) {
        myToast.toast(getActivity(), message);
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
                myToast.toast(getActivity(), "请确认已经插入SD卡!");
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                LogUtils.d("path=" + cameraPath);
                File file = new File(cameraPath);
                File Compressfile = CompressImage.getSmallFile(cameraPath, savePath, file.getName());
                modifyHead(Compressfile);
            } else if (requestCode == 102) {
                try {
                    Uri uri = data.getData();
                    LogUtils.d(uri);
                    final String absolutePath = getRealFilePath(getActivity(), uri);
                    File file = new File(absolutePath);
                    File Compressfile = CompressImage.getSmallFile(file.getAbsolutePath(), savePath, file.getName());
                    modifyHead(Compressfile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
