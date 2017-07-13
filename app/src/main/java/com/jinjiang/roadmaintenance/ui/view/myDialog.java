package com.jinjiang.roadmaintenance.ui.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.utils.GlideImgManager;

/**
 * 创建自定义的dialog
 */
public class myDialog extends Dialog {

    private TextView contentText;
    private TextView titleTv;//消息标题文本
    private ImageView img;
    private Context context;
    //外部传值的显示值
    private String  contentStr, titleStr,res;

    public myDialog(Context context) {
        super(context, R.style.MyDialog);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mainlayout);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();

    }
    /**
     * 初始化界面控件
     */
    private void initView() {
        titleTv = (TextView) findViewById(R.id.title);
        contentText = (TextView) findViewById(R.id.desc);
        img = (ImageView) findViewById(R.id.img);
    }
    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (titleStr!=null) {
            titleTv.setText(titleStr);
        }
        if (contentStr!=null) {
            contentText.setText(contentStr);
        }
        if (res!=null){
            GlideImgManager.glideLoader(context,res,R.drawable.pic_not_found,R.drawable.pic_not_found,img,1);
        }
    }

    /**
     * 从外界Activity为Dialog设置标题
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    public void setContenttext(String text) {
        contentStr = text;
    }

    public void setImg(String ress) {
        res = ress;
    }
}
