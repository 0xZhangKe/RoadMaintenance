package com.jinjiang.roadmaintenance.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;

/**
 * 病害详情
 */
public class EventDetailsActivity extends BaseActivity {

    private TextView mNum;
    private TextView mName;
    private TextView mRoad;
    private TextView mDate;
    private TextView mLocation;
    private TextView mState;

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

        findViewById(R.id.eventdetails_back).setOnClickListener(this);
        findViewById(R.id.eventdetails_map).setOnClickListener(this);

        mNum = (TextView) findViewById(R.id.eventdetails_num);
        mName = (TextView) findViewById(R.id.eventdetails_name);
        mRoad = (TextView) findViewById(R.id.eventdetails_roadName);
        mDate = (TextView) findViewById(R.id.eventdetails_date);
        mLocation = (TextView) findViewById(R.id.eventdetails_place);
        mState = (TextView) findViewById(R.id.eventdetails_state);


    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.eventdetails_back:
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
                break;
            case R.id.eventdetails_map:
                startActivity(new Intent(EventDetailsActivity.this,NavigationActivity.class));
                break;
            default:
                break;
        }
    }
}
