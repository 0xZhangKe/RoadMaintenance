package com.jinjiang.roadmaintenance.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class EventTypeActivity extends BaseActivity {

    @BindView(R.id.eventType_tv)
    TextView mTypeTv;
    @BindView(R.id.eventtype_long)
    EditText mLong;
    @BindView(R.id.eventtype_wide)
    EditText mWide;
    @BindView(R.id.eventtype_area)
    EditText mArea;
    @BindView(R.id.eventtype_content)
    EditText mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_event_type;
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.eventtype_back, R.id.eventadd_eventType_know, R.id.eventType_tv_ll, R.id.eventtype_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.eventtype_back:
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
                break;
            case R.id.eventadd_eventType_know:
                break;
            case R.id.eventType_tv_ll:
                break;
            case R.id.eventtype_send:
                break;
        }
    }
}
