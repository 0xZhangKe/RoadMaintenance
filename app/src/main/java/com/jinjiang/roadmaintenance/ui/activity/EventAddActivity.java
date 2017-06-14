package com.jinjiang.roadmaintenance.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;

public class EventAddActivity extends BaseActivity {

    private LinearLayout mBack;

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

        mBack = (LinearLayout)findViewById(R.id.eventadd_back);

        mBack.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.eventadd_back:
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
                break;
            default:
                break;
        }
    }
}
