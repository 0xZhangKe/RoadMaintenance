package com.jinjiang.roadmaintenance.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
import com.jinjiang.roadmaintenance.data.EventType;
import com.jinjiang.roadmaintenance.ui.view.myDialog;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class EventTypeSelectActivity extends BaseActivity {

    @BindView(R.id.eventTypeselect_listView)
    ListView mListView;
    private int position;
    ArrayList<EventType> mEventTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_event_type_select;
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mEventTypeList = (ArrayList<EventType>) intent.getSerializableExtra("EventTypeList");

        mListView.setAdapter(new CommonAdapter<EventType>(EventTypeSelectActivity.this, R.layout.item_plan_add, mEventTypeList) {
            @Override
            protected void convert(ViewHolder viewHolder, final EventType item, final int position) {
                viewHolder.setText(R.id.item_name, item.getName());
                viewHolder.setOnClickListener(R.id.item_explan, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog myDialog = new myDialog(EventTypeSelectActivity.this);
                        myDialog.setTitle(item.getName());
                        myDialog.setContenttext(item.getDetail());
                        myDialog.setImg(item.getPicUrl());
                        myDialog.show();
                    }
                });
                if (item.ischecked) {
                    ((CheckBox) (viewHolder.getView(R.id.item_check))).setChecked(true);
                } else {
                    ((CheckBox) (viewHolder.getView(R.id.item_check))).setChecked(false);
                }
                ((CheckBox) (viewHolder.getView(R.id.item_check))).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            for (EventType e : mEventTypeList) {
                                e.ischecked = false;
                            }
                            item.ischecked = true;
                            notifyDataSetChanged();
                            EventTypeSelectActivity.this.position = position;
                        } else {
                            item.ischecked = false;
                        }
                    }
                });
            }
        });

    }

    @OnClick({R.id.eventTypeselect_back, R.id.eventTypeselect_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.eventTypeselect_back:
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
                break;
            case R.id.eventTypeselect_send:
                Intent intent = new Intent();
                intent.putExtra("id", position);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }
}
