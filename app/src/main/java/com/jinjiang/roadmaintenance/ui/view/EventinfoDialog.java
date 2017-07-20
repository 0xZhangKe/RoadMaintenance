package com.jinjiang.roadmaintenance.ui.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinjiang.roadmaintenance.R;

/**
 * 创建自定义的dialog
 */
public class EventinfoDialog extends Dialog {

    private TextView nameText, typeText, wayText, diseaseText, areaText, stateText, timeText, okText;
    private LinearLayout way_ll,disease_ll,time_ll;
    private Context context;
    //外部传值的显示值
    private String nameStr, typeStr, wayStr, diseaseStr, areaStr, stateStr, timeStr;
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    public EventinfoDialog(Context context) {
        super(context, R.style.MyDialog);
        this.context = context;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(onYesOnclickListener onYesOnclickListener) {
        this.yesOnclickListener = onYesOnclickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_map);
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
        nameText = (TextView) findViewById(R.id.roadname);
        typeText = (TextView) findViewById(R.id.roadtype);
        wayText = (TextView) findViewById(R.id.way);
        diseaseText = (TextView) findViewById(R.id.disease);
        areaText = (TextView) findViewById(R.id.area);
        stateText = (TextView) findViewById(R.id.state);
        timeText = (TextView) findViewById(R.id.time);
        okText = (TextView) findViewById(R.id.ok);
        way_ll = (LinearLayout) findViewById(R.id.way_ll);
        disease_ll = (LinearLayout) findViewById(R.id.disease_ll);
        time_ll = (LinearLayout) findViewById(R.id.time_ll);

    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (nameStr != null) {
            nameText.setText(nameStr);
        }
        if (typeStr != null) {
            typeText.setText(typeStr);
        }
        if (wayStr != null) {
            wayText.setText(wayStr);
            if (TextUtils.isEmpty(wayStr.trim())){
                way_ll.setVisibility(View.GONE);
            }
        }else {
            way_ll.setVisibility(View.GONE);
        }

        if (diseaseStr != null) {
            diseaseText.setText(diseaseStr);
            if (TextUtils.isEmpty(diseaseStr.trim())){
                disease_ll.setVisibility(View.GONE);
            }
        }else {
            disease_ll.setVisibility(View.GONE);
        }
        if (areaStr != null) {
            areaText.setText(areaStr);
        }
        if (stateStr != null) {
            stateText.setText(stateStr);
        }
        if (timeStr != null){
            timeText.setText(timeStr);
        }else {
            time_ll.setVisibility(View.GONE);
        }

        okText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
    }

    /**
     * 从外界Activity为Dialog设置标题
     */

    public void setroadname(String str) {
        nameStr = str;
    }

    public void settypeStr(String str) {
        typeStr = str;
    }

    public void setwayStr(String str) {
        wayStr = str;
    }

    public void setdiseaseStr(String str) {
        diseaseStr = str;
    }

    public void setareaStr(String str) {
        areaStr = str;
    }

    public void setstateStr(String str) {
        stateStr = str;
    }

    public void settimeStr(String str) {
        timeStr = str;
    }
    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        public void onYesClick();
    }

}
