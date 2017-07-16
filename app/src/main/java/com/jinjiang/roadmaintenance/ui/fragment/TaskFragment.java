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
import android.widget.ExpandableListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.adapter.TaskAdapter;
import com.jinjiang.roadmaintenance.data.MessageEvent;
import com.jinjiang.roadmaintenance.data.SaveEventData;
import com.jinjiang.roadmaintenance.data.SaveEventData_Table;
import com.jinjiang.roadmaintenance.data.Task;
import com.jinjiang.roadmaintenance.data.TaskState;
import com.jinjiang.roadmaintenance.data.UserInfo;
import com.jinjiang.roadmaintenance.model.NetWorkRequest;
import com.jinjiang.roadmaintenance.model.UIDataListener;
import com.jinjiang.roadmaintenance.ui.activity.EventAddActivity;
import com.jinjiang.roadmaintenance.ui.activity.EventDetail2Activity;
import com.jinjiang.roadmaintenance.ui.activity.EventDetailsActivity;
import com.jinjiang.roadmaintenance.ui.activity.LoginActivity;
import com.jinjiang.roadmaintenance.ui.view.DialogProgress;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.jinjiang.roadmaintenance.utils.ACache;
import com.jinjiang.roadmaintenance.utils.Uri;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 任务
 */
public class TaskFragment extends Fragment implements UIDataListener {
    private static TaskFragment fragment;
    @BindView(R.id.task_listview)
    ExpandableListView mListview;
    Unbinder unbinder;
    private ACache mAcache;
    private Dialog dialog;
    private NetWorkRequest request;
    private UserInfo userInfo;
    private ArrayList<TaskState> mTaskStateList;
    private TaskAdapter adapter;
    private List<SaveEventData> mSaveEventData;

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

        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initData();
        return view;
    }

    private void initData() {
//        mListview.setGroupIndicator(null);
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        mListview.setIndicatorBounds(width-60, width-10);
        mTaskStateList = new ArrayList<>();

        mAcache = ACache.get(getActivity());
        mSaveEventData = new Select().from(SaveEventData.class).queryList();
        dialog = DialogProgress.createLoadingDialog(getActivity(), "", this);
        request = new NetWorkRequest(getActivity(), this);
        userInfo = (UserInfo) mAcache.getAsObject("UserInfo");
        if (userInfo == null || TextUtils.isEmpty(userInfo.getUserId())) {
            myToast.toast(getActivity(), "登录状态已过期，请重新登录！");
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }

        adapter = new TaskAdapter(getActivity(), mTaskStateList);
        mListview.setAdapter(adapter);
        mListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (mTaskStateList.get(groupPosition).getOrderStatus() == 1001) {
                    Intent intent = new Intent(getActivity(), EventAddActivity.class);
                    intent.putExtra("SaveEventData", mSaveEventData.get(childPosition).id);
                    startActivity(intent);
                } else if (mTaskStateList.get(groupPosition).getOrderStatus() == 1004){
                    Intent intent = new Intent(getActivity(), EventAddActivity.class);
                    intent.putExtra("Task", mTaskStateList.get(groupPosition).getTasks().get(childPosition));
                    startActivity(intent);
                }else if (mTaskStateList.get(groupPosition).getOrderStatus() == 1002 ||
                        mTaskStateList.get(groupPosition).getOrderStatus() == 1006 ||
                        mTaskStateList.get(groupPosition).getOrderStatus() == 1007 ||
                        mTaskStateList.get(groupPosition).getOrderStatus() == 1008
                        ) {
                    Intent intent = new Intent(getActivity(), EventDetail2Activity.class);
                    intent.putExtra("Task", mTaskStateList.get(groupPosition).getTasks().get(childPosition));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
                    intent.putExtra("Task", mTaskStateList.get(groupPosition).getTasks().get(childPosition));
                    startActivity(intent);
                }
                return true;
            }
        });

        Map map = new HashMap();
        map.put("userId", userInfo.getUserId());
        map.put("appSid", userInfo.getAppSid());
        request.doPostRequest(0, true, Uri.getOrderWork, map);

    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (code == 0) {
            if (data != null) {
                mTaskStateList = JSON.parseObject(data.toString(), new TypeReference<ArrayList<TaskState>>() {
                });
                if (mTaskStateList != null && mTaskStateList.size() > 0) {
                    for (TaskState t : mTaskStateList) {
                        if (t.getOrderStatus() == 1001) {
                            t.setTasks(null);
                            mSaveEventData = new Select().from(SaveEventData.class).queryList();
                            if (mSaveEventData != null && mSaveEventData.size() > 0) {
                                ArrayList<Task> localTask = new ArrayList<>();
                                for (SaveEventData d : mSaveEventData) {
                                    Task task = new Task();
                                    int road = d.roadvalue;
                                    switch (road){
                                        case 1:
                                            task.setOrderTypeName("沥青路面");
                                            break;
                                        case 2:
                                            task.setOrderTypeName("水泥路面");
                                            break;
                                        case 3:
                                            task.setOrderTypeName("人行道");
                                            break;
                                        case 4:
                                            task.setOrderTypeName("井盖");
                                            break;
                                        case 5:
                                            task.setOrderTypeName("其他");
                                            break;
                                    }

                                    task.setLocationDesc(d.locationDesc);
                                    SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/yyyy");
                                    java.util.Date dt = new Date(d.id * 1000);
                                    String sDateTime = sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
                                    task.setCreateDt(sDateTime);
                                    task.setTaskId("loc");
                                    localTask.add(task);
                                }
                                t.setTasks(localTask);
                            }
                        }
                    }
                    adapter.setDatas(mTaskStateList);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(MessageEvent event) {
        LogUtils.d(event);
        if (event.code == 1) {
            Map map = new HashMap();
            map.put("userId", userInfo.getUserId());
            map.put("appSid", userInfo.getAppSid());
            request.doPostRequest(0, true, Uri.getOrderWork, map);
        }
    }
}
