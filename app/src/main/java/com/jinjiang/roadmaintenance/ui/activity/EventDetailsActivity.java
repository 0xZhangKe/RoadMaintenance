package com.jinjiang.roadmaintenance.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 病害详情
 */
public class EventDetailsActivity extends BaseActivity {


    @BindView(R.id.eventdetails_num)
    TextView mNum;
    @BindView(R.id.eventdetails_name)
    TextView mName;
    @BindView(R.id.eventdetails_roadName)
    TextView mRoadName;
    @BindView(R.id.eventdetails_date)
    TextView mDate;
    @BindView(R.id.eventdetails_long)
    EditText mLong;
    @BindView(R.id.eventdetails_wide)
    EditText mWide;
    @BindView(R.id.eventdetails_area)
    EditText mArea;

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

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.eventdetails_back, R.id.eventdetails_map})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.eventdetails_back:
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
                break;
            case R.id.eventdetails_map:
                startActivity(new Intent(EventDetailsActivity.this,NavigationActivity.class));
                break;
        }
    }
}
