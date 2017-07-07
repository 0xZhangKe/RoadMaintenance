package com.jinjiang.roadmaintenance.ui.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.data.RoadType;
import com.jinjiang.roadmaintenance.data.Task;
import com.jinjiang.roadmaintenance.data.TaskState;
import com.jinjiang.roadmaintenance.data.UserInfo;
import com.jinjiang.roadmaintenance.model.LoacationModel;
import com.jinjiang.roadmaintenance.model.NetWorkRequest;
import com.jinjiang.roadmaintenance.model.UIDataListener;
import com.jinjiang.roadmaintenance.ui.activity.EventDetailsActivity;
import com.jinjiang.roadmaintenance.ui.activity.LoginActivity;
import com.jinjiang.roadmaintenance.ui.view.DialogProgress;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.jinjiang.roadmaintenance.ui.view.nicespinner.NiceSpinner;
import com.jinjiang.roadmaintenance.utils.ACache;
import com.jinjiang.roadmaintenance.utils.Uri;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.DateTimePicker;


/**
 * 任务
 */
public class TaskFragment extends Fragment implements UIDataListener, View.OnClickListener {
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
    private int mOrderStatus = 0;
    private String startTime = "";
    private String endTime = "";
    private ACache mAcache;
    private Dialog dialog;
    private NetWorkRequest request;
    private UserInfo userInfo;
    private ArrayList<TaskState> mTaskStateList;
    private ArrayList<Task> mTaskList;
    private CommonAdapter<Task> adapter;

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

    /**
     * 重置
     */
    private void reset() {
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

        mTaskList = new ArrayList<>();

        mAcache = ACache.get(getActivity());
        dialog = DialogProgress.createLoadingDialog(getActivity(), "", this);
        request = new NetWorkRequest(getActivity(), this);
        userInfo = (UserInfo) mAcache.getAsObject("UserInfo");
        if (userInfo == null || TextUtils.isEmpty(userInfo.getUserId())) {
            myToast.toast(getActivity(), "登录状态已过期，请重新登录！");
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
        mNs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mOrderStatus = mTaskStateList.get(i).getOrderStatus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
                intent.putExtra("Task",mTaskList.get(position));
                startActivity(intent);
            }
        });
        mTime1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startTime = "";
                    endTime = "";
                    mTime2.setText("");
                    mTime3.setText("");
                    mTime2.setEnabled(false);
                    mTime3.setEnabled(false);
                    mTime2.setBackground(getResources().getDrawable(R.drawable.gre_stroke_back2));
                    mTime3.setBackground(getResources().getDrawable(R.drawable.gre_stroke_back2));
                } else {
                    mTime2.setEnabled(true);
                    mTime3.setEnabled(true);
                    mTime2.setBackground(getResources().getDrawable(R.drawable.gre_stroke_back));
                    mTime3.setBackground(getResources().getDrawable(R.drawable.gre_stroke_back));
                }
            }
        });

        Map map = new HashMap();
        map.put("userId", userInfo.getUserId());
        map.put("appSid", userInfo.getAppSid());
        request.doPostRequest(0, true, Uri.getOrderWork, map);

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
        picker_Time.setSelectedItem(year, month, day);
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
        switch (view.getId()) {
            case R.id.task_search:
                if (mSearchdown.getVisibility() == View.GONE) {
                    mSearchdown.setVisibility(View.VISIBLE);
                } else {
                    mSearchdown.setVisibility(View.GONE);
                }
                break;
            case R.id.task_time2:
                timeType = 0;
                picker_Time.show();
                break;
            case R.id.task_time3:
                timeType = 1;
                picker_Time.show();
                break;
            case R.id.task_reset:
                reset();
                break;
            case R.id.task_confirm:
                mSearchdown.setVisibility(View.GONE);
                reset();
                Map map2 = new HashMap();
                map2.put("userId", userInfo.getUserId());
                map2.put("appSid", userInfo.getAppSid());
                map2.put("orderStatus", mOrderStatus+"");
                request.doPostRequest(1, true, Uri.getMyTask, map2);
                break;
            default:
                break;
        }
    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (code == 0) {
            if (data != null) {
                mTaskStateList = JSON.parseObject(data.toString(), new TypeReference<ArrayList<TaskState>>() {
                });
                if (mTaskStateList != null && mTaskStateList.size() > 0) {
                    ArrayList typeList = new ArrayList();
                    for (int i = 0; i < mTaskStateList.size(); i++) {
                        typeList.add(mTaskStateList.get(i).getOrderStatusName());
                    }
                    mNs.attachDataSource(typeList);
                    mOrderStatus = mTaskStateList.get(0).getOrderStatus();
                    Map map2 = new HashMap();
                    map2.put("userId", userInfo.getUserId());
                    map2.put("appSid", userInfo.getAppSid());
                    map2.put("orderStatus", mOrderStatus+"");
                    request.doPostRequest(1, true, Uri.getMyTask, map2);
                }
            }
        }else if (code == 1) {
            if (data != null) {
                mTaskList = JSON.parseObject(data.toString(), new TypeReference<ArrayList<Task>>() {
                });
                if (mTaskList != null && mTaskList.size() > 0) {
                    adapter = new CommonAdapter<Task>(getActivity(), R.layout.item_task_main, mTaskList) {
                        @Override
                        protected void convert(ViewHolder viewHolder, Task item, int position) {
                            viewHolder.setText(R.id.tv,item.getLocationDesc()+"-"+item.getOrderTypeName()+"-"+item.getCreateDt());
                        }
                    };
                    mListView.setAdapter(adapter);
                }
            }
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
}
