package com.jinjiang.roadmaintenance.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.data.RateList;
import com.jinjiang.roadmaintenance.data.UserInfo;
import com.jinjiang.roadmaintenance.model.NetWorkRequest;
import com.jinjiang.roadmaintenance.model.UIDataListener;
import com.jinjiang.roadmaintenance.ui.activity.LoginActivity;
import com.jinjiang.roadmaintenance.ui.activity.RateDetailsActivity;
import com.jinjiang.roadmaintenance.ui.view.DialogProgress;
import com.jinjiang.roadmaintenance.ui.view.library.PullToRefreshBase;
import com.jinjiang.roadmaintenance.ui.view.library.PullToRefreshListView;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.jinjiang.roadmaintenance.utils.ACache;
import com.jinjiang.roadmaintenance.utils.ScreenUtils;
import com.jinjiang.roadmaintenance.utils.Uri;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.DateTimePicker;

/**
 * 进度
 */
public class RateFragment extends Fragment implements UIDataListener {
    private static RateFragment fragment;
    @BindView(R.id.rate_listview)
    PullToRefreshListView mListview;
    @BindView(R.id.rate_state1)
    TextView mState1;
    @BindView(R.id.rate_state2)
    TextView mState2;
    @BindView(R.id.rate_state3)
    TextView mState3;
    @BindView(R.id.rate_state4)
    TextView mState4;
    @BindView(R.id.rate_time1)
    RadioButton mTime1;
    @BindView(R.id.rate_time2)
    RadioButton mTime2;
    @BindView(R.id.rate_time3)
    RadioButton mTime3;
    @BindView(R.id.rate_time4)
    RadioButton mTime4;
    @BindView(R.id.rate_time5)
    TextView mTime5;
    @BindView(R.id.rate_time6)
    TextView mTime6;
    @BindView(R.id.rate_confirm)
    TextView mConfirm;
    @BindView(R.id.rate_search_down)
    LinearLayout mSearchDown;
    Unbinder unbinder;
    @BindView(R.id.rate_time_ll)
    LinearLayout mTimeLl;
    @BindView(R.id.rate_empty)
    View mEmpty;
    @BindView(R.id.rate_Gg)
    RadioGroup mGg;
    @BindView(R.id.rate_search_content)
    LinearLayout rateSearchContent;
    private DatePicker picker_Time;
    private String startTime = "";
    private String endTime = "";
    private int timeType = 3;
    private int timeState = 1;
    private int[] orderStatus = new int[]{0, 0, 0, 0};
    private ACache mAcache;
    private Dialog dialog;
    private NetWorkRequest request;
    private UserInfo userInfo;
    private RateList mRateList;
    private CommonAdapter<RateList.RowsBean> adapter;
    private ArrayList<RateList.RowsBean> mList;

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
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        mList = new ArrayList<>();

        mListview.setMode(PullToRefreshBase.Mode.BOTH);
        mListview.setEmptyView(mEmpty);
        mAcache = ACache.get(getActivity());
        dialog = DialogProgress.createLoadingDialog(getActivity(), "", this);
        request = new NetWorkRequest(getActivity(), this);
        userInfo = (UserInfo) mAcache.getAsObject("UserInfo");
        if (userInfo == null || TextUtils.isEmpty(userInfo.getUserId())) {
            myToast.toast(getActivity(), "登录状态已过期，请重新登录！");
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }

        initDatePicker();

        mTime4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mTimeLl.setVisibility(View.VISIBLE);
                    startTime = "";
                    endTime = "";
                    mTime5.setText("");
                    mTime6.setText("");
                } else {
                    mTimeLl.setVisibility(View.GONE);
                }
            }
        });

        mGg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rate_time1:
                        timeState = 1;
                        break;
                    case R.id.rate_time2:
                        timeState = 2;
                        break;
                    case R.id.rate_time3:
                        timeState = 3;
                        break;
                    case R.id.rate_time4:
                        timeState = 4;
                        break;
                    default:
                        break;
                }
            }
        });

        getDate(0, true, 1);

        mListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getDate(0, false, 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                int pageNo = (mList.size() + 9) / 10;
                getDate(1, false, pageNo);
            }
        });

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                i -= 1;
                Intent intent = new Intent(getActivity(), RateDetailsActivity.class);
                intent.putExtra("Row", mList.get(i));
                startActivity(intent);
            }
        });
    }


    /**
     * 获取进度数据
     *
     * @param flag
     * @param isshow
     * @param page
     */
    private void getDate(int flag, boolean isshow, int page) {
        Map map = new HashMap();
        map.put("userId", userInfo.getUserId());
        map.put("appSid", userInfo.getAppSid());
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("pageSize", 10);
        StringBuffer buffer = new StringBuffer();
        for (int i:orderStatus){
            if (buffer.length()==0){
                buffer.append(""+i);
            }else {
                buffer.append(","+i);
            }
        }
        object.put("orderStatus", buffer.toString());
        object.put("time", timeState);
        if (timeState == 4) {
            object.put("startTime", startTime);
            object.put("endTime", endTime);
        }
        map.put("body", object.toJSONString());
        request.doPostRequest(flag, isshow, Uri.getWorkOrderProgress, map);
    }

    private void setUiData() {

        adapter = new CommonAdapter<RateList.RowsBean>(getActivity(), R.layout.item_ratelist_main, (List<RateList.RowsBean>) mList) {
            @Override
            protected void convert(ViewHolder viewHolder, RateList.RowsBean item, int position) {
                switch (item.getOrderStatus()) {
                    case 2001:
                        viewHolder.setImageResource(R.id.item_img, R.drawable.rate2001);
                        break;
                    case 2002:
                        viewHolder.setImageResource(R.id.item_img, R.drawable.rate2002);
                        break;
                    case 2003:
                        viewHolder.setImageResource(R.id.item_img, R.drawable.rate2003);
                        break;
                    case 2004:
                        viewHolder.setImageResource(R.id.item_img, R.drawable.rate2004);
                        break;
                }
                viewHolder.setText(R.id.tv1, item.getRoadName() + "        " + item.getArea() + "㎡        " + item.getOrderStatusName());
                viewHolder.setText(R.id.tv2, item.getOrderTypeName() + "        " + ScreenUtils.getStrDate(item.getCreateTime()));
            }
        };

        mListview.setAdapter(adapter);
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
        picker_Time.setSelectedItem(year, month, day);
        //监听选择
        picker_Time.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                String selectDate = year + "-" + month + "-" + day;
                switch (timeType) {
                    case 0:
                        mTime5.setText(selectDate);
                        startTime = selectDate;
                        break;
                    case 1:
                        mTime6.setText(selectDate);
                        endTime = selectDate;
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
            if (dt1.getTime() > dt2.getTime()) {
                //DATENow 在DATESelect前
                return true;
            } else if (dt1.getTime() <= dt2.getTime()) {
                //DATENow在DATESelect后
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setmStateBack(int[] k) {
        if (k[0] == 0) {
            mState1.setBackgroundResource(R.drawable.gre_back1);
            mState1.setTextColor(getResources().getColor(R.color.text_gray));
        } else {
            mState1.setBackgroundResource(R.drawable.blue_back1);
            mState1.setTextColor(getResources().getColor(R.color.white));
        }
        if (k[1] == 0) {
            mState2.setBackgroundResource(R.drawable.gre_back1);
            mState2.setTextColor(getResources().getColor(R.color.text_gray));
        } else {
            mState2.setBackgroundResource(R.drawable.blue_back1);
            mState2.setTextColor(getResources().getColor(R.color.white));
        }
        if (k[2] == 0) {
            mState3.setBackgroundResource(R.drawable.gre_back1);
            mState3.setTextColor(getResources().getColor(R.color.text_gray));
        } else {
            mState3.setBackgroundResource(R.drawable.blue_back1);
            mState3.setTextColor(getResources().getColor(R.color.white));
        }
        if (k[3] == 0) {
            mState4.setBackgroundResource(R.drawable.gre_back1);
            mState4.setTextColor(getResources().getColor(R.color.text_gray));
        } else {
            mState4.setBackgroundResource(R.drawable.blue_back1);
            mState4.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void setTimeBack(int k) {
        switch (k) {
            case 1:
                mTime1.setChecked(true);
                mTime2.setChecked(false);
                mTime3.setChecked(false);
                mTime4.setChecked(false);
                break;
            case 2:
                mTime1.setChecked(false);
                mTime2.setChecked(true);
                mTime3.setChecked(false);
                mTime4.setChecked(false);
                break;
            case 3:
                mTime1.setChecked(false);
                mTime2.setChecked(false);
                mTime3.setChecked(true);
                mTime4.setChecked(false);
                break;
            case 4:
                mTime1.setChecked(false);
                mTime2.setChecked(false);
                mTime3.setChecked(false);
                mTime4.setChecked(true);
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.rate_time5img, R.id.rate_time6img, R.id.rate_confirm, R.id.rate_search, R.id.rate_state1, R.id.rate_state2, R.id.rate_state3, R.id.rate_state4, R.id.rate_search_down, R.id.rate_search_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rate_search:
                if (mSearchDown.getVisibility() == View.GONE) {
                    mSearchDown.setVisibility(View.VISIBLE);
                    mTime5.setText(startTime);
                    mTime6.setText(endTime);

                    setmStateBack(orderStatus);
                    setTimeBack(timeState);

                } else {
                    mSearchDown.setVisibility(View.GONE);
                }
                break;
            case R.id.rate_time5img:
                timeType = 0;
                picker_Time.show();
                break;
            case R.id.rate_time6img:
                timeType = 1;
                picker_Time.show();
                break;
            case R.id.rate_confirm:
                if (timeState == 4) {
                    if (TextUtils.isEmpty(startTime) && TextUtils.isEmpty(endTime)) {
                        showToast("请选择自定义时间！");
                        return;
                    }
                    if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
                        if (compare_date(startTime, endTime)) {
                            showToast("起始时间不能大于结束时间！");
                            return;
                        }
                    }
                }
                mSearchDown.setVisibility(View.GONE);
                getDate(0, true, 1);
                break;
            case R.id.rate_state1:
                if (orderStatus[0]==0){
                    orderStatus[0] = 2001;
                }else {
                    orderStatus[0] = 0;
                }
                setmStateBack(orderStatus);
                break;
            case R.id.rate_state2:
                if (orderStatus[1]==0){
                    orderStatus[1] = 2002;
                }else {
                    orderStatus[1] = 0;
                }
                setmStateBack(orderStatus);
                break;
            case R.id.rate_state3:
                if (orderStatus[2]==0){
                    orderStatus[2] = 2003;
                }else {
                    orderStatus[2] = 0;
                }
                setmStateBack(orderStatus);
                break;
            case R.id.rate_state4:
                if (orderStatus[3]==0){
                    orderStatus[3] = 2004;
                }else {
                    orderStatus[3] = 0;
                }
                setmStateBack(orderStatus);
                break;
            case R.id.rate_search_down:
                mSearchDown.setVisibility(View.GONE);
                break;
            case R.id.rate_search_content:
                break;
        }
    }

    @Override
    public void loadDataFinish(int code, Object data) {

        if (code == 0) {
            if (data != null) {
                mRateList = JSON.parseObject(data.toString(), new TypeReference<RateList>() {
                });
                if (mRateList != null && mRateList.getRows() != null) {
                    mList.clear();
                    mList.addAll(mRateList.getRows());
                    setUiData();
                }
            }
        } else if (code == 1) {
            if (data != null) {
                mRateList = JSON.parseObject(data.toString(), new TypeReference<RateList>() {
                });
                if (mRateList != null && mRateList.getRows() != null) {
                    mList.addAll(mRateList.getRows());
//                    setUiData();
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
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
        mListview.onRefreshComplete();
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
