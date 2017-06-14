package com.jinjiang.roadmaintenance.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.ui.view.nicespinner.NiceSpinner;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.Calendar;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.DateTimePicker;


/**
 * 任务
 */
public class TaskFragment extends Fragment implements View.OnClickListener{
    private static TaskFragment fragment;
    private ImageView mSearch;
    private LinearLayout mSearchdown;
    private CheckBox mTime1;
    private TextView mTime2;
    private TextView mTime3;
    private CheckBox mBiao1;
    private CheckBox mBiao2;
    private CheckBox mBiao3;
    private CheckBox mRoad1;
    private CheckBox mRoad2;
    private CheckBox mRoad3;
    private CheckBox mRoad4;
    private CheckBox mRoad5;
    private NiceSpinner mNs;
    private TextView mReset;
    private TextView mComfirm;
    private ListView mListView;
    private DatePicker picker_Time;
    private int timeType = 0;

    public TaskFragment() {
    }
    public static TaskFragment newInstance() {

        if (fragment == null) {
            fragment = new TaskFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mSearch = (ImageView) view.findViewById(R.id.task_search);
        mSearchdown = (LinearLayout) view.findViewById(R.id.task_search_down);
        mTime1 = (CheckBox) view.findViewById(R.id.task_time1);
        mTime2 = (TextView) view.findViewById(R.id.task_time2);
        mTime3 = (TextView) view.findViewById(R.id.task_time3);

        mBiao1 = (CheckBox) view.findViewById(R.id.task_biao1);
        mBiao2 = (CheckBox) view.findViewById(R.id.task_biao2);
        mBiao3 = (CheckBox) view.findViewById(R.id.task_biao3);

        mRoad1 = (CheckBox) view.findViewById(R.id.task_road1);
        mRoad2 = (CheckBox) view.findViewById(R.id.task_road2);
        mRoad3 = (CheckBox) view.findViewById(R.id.task_road3);
        mRoad4 = (CheckBox) view.findViewById(R.id.task_road4);
        mRoad5 = (CheckBox) view.findViewById(R.id.task_road5);

        mNs = (NiceSpinner) view.findViewById(R.id.task_ns);
        mReset = (TextView) view.findViewById(R.id.task_reset);
        mComfirm = (TextView) view.findViewById(R.id.task_confirm);
        mListView = (ListView) view.findViewById(R.id.task_listview);

        mSearch.setOnClickListener(this);
        mTime2.setOnClickListener(this);
        mTime3.setOnClickListener(this);
        mReset.setOnClickListener(this);
        mComfirm.setOnClickListener(this);

        initDatePicker();

    }

    private void initData() {
        ArrayList typeList = new ArrayList();
        typeList.add("已记录，待提交");
        typeList.add("已提交，未施工");
        typeList.add("已提交，待批复");
        typeList.add("已批复，审核未通过");
        typeList.add("已处理，待提交");
        typeList.add("初验");
        typeList.add("三方验收");
        typeList.add("验收不合格");
        typeList.add("待确认");
        mNs.attachDataSource(typeList);

        mNs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayList list = new ArrayList();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");

        mListView.setAdapter(new CommonAdapter<String>(getActivity(),R.layout.item_task_main,list) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
            }
        });

    }

    /**
     * 初始化日期时间选择器
     */
    private void initDatePicker() {
        //获取当前日期
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);//获取年份
        int month = ca.get(Calendar.MONTH) + 1;//获取月份
        int day = ca.get(Calendar.DATE);//获取日
        int hour = ca.get(Calendar.HOUR_OF_DAY);//获取日
        int minute = ca.get(Calendar.MINUTE);//获取日
        String monthStr = "" + month;
        String dayStr = "" + day;
        String hourStr = "" + hour;
        String minuteStr = "" + minute;
        if (month < 10) {
            monthStr = "0" + month;
        }
        if (day < 10) {
            dayStr = "0" + day;
        }
        String dateNow = year + "-" + monthStr + "-" + dayStr + " " + hourStr + ":" + minuteStr;
//        selectDate = year + "-" + monthStr + "-" + dayStr + " " + hourStr + ":" + minuteStr;
        //设置picker
        picker_Time = new DatePicker(getActivity(), DateTimePicker.YEAR_MONTH_DAY);
        picker_Time.setRange(1900, 2100);//设置年份起始
        picker_Time.setCancelText("取消");
        picker_Time.setSubmitText("确定");
        picker_Time.setLineColor(TaskFragment.this.getResources().getColor(R.color.blue));
        picker_Time.setTextColor(TaskFragment.this.getResources().getColor(R.color.text_black));
        picker_Time.setCancelTextColor(TaskFragment.this.getResources().getColor(R.color.gray_deep));
        picker_Time.setSubmitTextColor(TaskFragment.this.getResources().getColor(R.color.blue));
        picker_Time.setSelectedItem(year,month,day);
        //监听选择
        picker_Time.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                String selectDate = year + "-" + month + "-" + day;
                switch (timeType) {
                    case 0:
                        mTime2.setText(selectDate);
                        break;
                    case 1:
                        mTime3.setText(selectDate);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.task_search:
                if (mSearchdown.getVisibility()==View.GONE){
                    mSearchdown.setVisibility(View.VISIBLE);
                }else {
                    mSearchdown.setVisibility(View.GONE);
                }
                break;
            case R.id.task_time2:
                timeType=0;
                picker_Time.show();
                break;
            case R.id.task_time3:
                timeType=1;
                picker_Time.show();
                break;
            case R.id.task_reset:
                break;
            case R.id.task_confirm:
                mSearchdown.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
}
