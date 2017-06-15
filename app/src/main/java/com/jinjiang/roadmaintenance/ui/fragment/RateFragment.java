package com.jinjiang.roadmaintenance.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.ui.activity.EventDetailsActivity;
import com.jinjiang.roadmaintenance.ui.view.nicespinner.NiceSpinner;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.DateTimePicker;


/**
 * 进度
 */
public class RateFragment extends Fragment implements View.OnClickListener{
    private static RateFragment fragment;
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
    private int eventType = 0;
    private String startTime = "";
    private String endTime = "";

    public RateFragment() {
    }
    public static RateFragment newInstance() {

        if (fragment == null) {
            fragment = new RateFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate, container, false);

        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mSearch = (ImageView) view.findViewById(R.id.rate_search);
        mSearchdown = (LinearLayout) view.findViewById(R.id.rate_search_down);
        mTime1 = (CheckBox) view.findViewById(R.id.rate_time1);
        mTime2 = (TextView) view.findViewById(R.id.rate_time2);
        mTime3 = (TextView) view.findViewById(R.id.rate_time3);

        mBiao1 = (CheckBox) view.findViewById(R.id.rate_biao1);
        mBiao2 = (CheckBox) view.findViewById(R.id.rate_biao2);
        mBiao3 = (CheckBox) view.findViewById(R.id.rate_biao3);

        mRoad1 = (CheckBox) view.findViewById(R.id.rate_road1);
        mRoad2 = (CheckBox) view.findViewById(R.id.rate_road2);
        mRoad3 = (CheckBox) view.findViewById(R.id.rate_road3);
        mRoad4 = (CheckBox) view.findViewById(R.id.rate_road4);
        mRoad5 = (CheckBox) view.findViewById(R.id.rate_road5);

        mNs = (NiceSpinner) view.findViewById(R.id.rate_ns);
        mReset = (TextView) view.findViewById(R.id.rate_reset);
        mComfirm = (TextView) view.findViewById(R.id.rate_confirm);
        mListView = (ListView) view.findViewById(R.id.rate_listview);

        mSearch.setOnClickListener(this);
        mTime2.setOnClickListener(this);
        mTime3.setOnClickListener(this);
        mReset.setOnClickListener(this);
        mComfirm.setOnClickListener(this);

        initDatePicker();

    }

    /**
     * 重置
     */
    private void reset(){
        eventType = 0;
        mNs.setSelectedIndex(0);

        mTime1.setChecked(false);
        mTime2.setText("");
        mTime3.setText("");
        startTime = "";
        endTime = "";

        mBiao1.setChecked(false);
        mBiao2.setChecked(false);
        mBiao3.setChecked(false);

        mRoad1.setChecked(false);
        mRoad2.setChecked(false);
        mRoad3.setChecked(false);
        mRoad4.setChecked(false);
        mRoad5.setChecked(false);

    }

    private void initData() {
        ArrayList typeList = new ArrayList();
        typeList.add("全部");
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
                eventType = i;
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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), EventDetailsActivity.class));
            }
        });
        mTime1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    startTime = "";
                    endTime = "";
                    mTime2.setText("");
                    mTime3.setText("");
                    mTime2.setEnabled(false);
                    mTime3.setEnabled(false);
                    mTime2.setBackground(getResources().getDrawable(R.drawable.gre_stroke_back2));
                    mTime3.setBackground(getResources().getDrawable(R.drawable.gre_stroke_back2));
                }else {
                    mTime2.setEnabled(true);
                    mTime3.setEnabled(true);
                    mTime2.setBackground(getResources().getDrawable(R.drawable.gre_stroke_back));
                    mTime3.setBackground(getResources().getDrawable(R.drawable.gre_stroke_back));
                }
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
        picker_Time.setLineColor(RateFragment.this.getResources().getColor(R.color.blue));
        picker_Time.setTextColor(RateFragment.this.getResources().getColor(R.color.text_black));
        picker_Time.setCancelTextColor(RateFragment.this.getResources().getColor(R.color.gray_deep));
        picker_Time.setSubmitTextColor(RateFragment.this.getResources().getColor(R.color.blue));
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
    /**
     * 比较当前选择的日期与今日日期大小
     *
     * @param DATENow
     * @param DATESelect
     * @return
     */
    public static boolean compare_date(String DATENow, String DATESelect) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATENow);
            Date dt2 = df.parse(DATESelect);
            if (dt1.getTime() >= dt2.getTime()) {
                //DATENow 在DATESelect前
                return true;
            } else if (dt1.getTime() < dt2.getTime()) {
                //DATENow在DATESelect后
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rate_search:
                if (mSearchdown.getVisibility()==View.GONE){
                    mSearchdown.setVisibility(View.VISIBLE);
                }else {
                    mSearchdown.setVisibility(View.GONE);
                }
                break;
            case R.id.rate_time2:
                timeType=0;
                picker_Time.show();
                break;
            case R.id.rate_time3:
                timeType=1;
                picker_Time.show();
                break;
            case R.id.rate_reset:
                reset();
                break;
            case R.id.rate_confirm:
                mSearchdown.setVisibility(View.GONE);
                reset();
                break;
            default:
                break;
        }
    }
}
