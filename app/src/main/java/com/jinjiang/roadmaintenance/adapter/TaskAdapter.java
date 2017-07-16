package com.jinjiang.roadmaintenance.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.data.Task;
import com.jinjiang.roadmaintenance.data.TaskState;
import com.jinjiang.roadmaintenance.utils.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by wuwei on 2017/7/11.
 */

public class TaskAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<TaskState> mList;

    public TaskAdapter(Context context, ArrayList<TaskState> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void setDatas(ArrayList<TaskState> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mList!=null&&mList.get(groupPosition)!=null&&mList.get(groupPosition).getTasks()!=null){
            return mList.get(groupPosition).getTasks().size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getTasks().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = null;
        GroupHolder groupholder = null;
        if (convertView != null) {
            view = convertView;
            groupholder = (GroupHolder) view.getTag();
        } else {
            view = View.inflate(context, R.layout.item_task_main, null);
            groupholder = new GroupHolder();
            groupholder.tv = (TextView) view.findViewById(R.id.tv);
            view.setTag(groupholder);
        }
        if (mList.get(groupPosition).getTasks() != null) {
            groupholder.tv.setText(mList.get(groupPosition).getOrderStatusName() + "(" + mList.get(groupPosition).getTasks().size() + ")");
        } else {
            groupholder.tv.setText(mList.get(groupPosition).getOrderStatusName() + "(0)");
        }
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = null;
        ChildHolder childholder = null;
        if (convertView != null) {
            view = convertView;
            childholder = (ChildHolder) view.getTag();
        } else {
            view = View.inflate(context, R.layout.item_task_sub, null);
            childholder = new ChildHolder();
            childholder.tv1 = (TextView) view.findViewById(R.id.tv1);
            childholder.tv2 = (TextView) view.findViewById(R.id.tv2);
            childholder.tv3 = (TextView) view.findViewById(R.id.tv3);
            view.setTag(childholder);
        }
        Task item = mList.get(groupPosition).getTasks().get(childPosition);
        try {

            childholder.tv1.setText(item.getOrderTypeName());
            childholder.tv2.setText(ScreenUtils.getRoad(item.getLocationDesc()));
            childholder.tv3.setText(item.getCreateDt().substring(0, item.getCreateDt().lastIndexOf("-") + 3));
        } catch (Exception e) {
            childholder.tv1.setText(item.getOrderTypeName());
            childholder.tv2.setText(ScreenUtils.getRoad(item.getLocationDesc()));
            childholder.tv3.setText(item.getCreateDt());
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupHolder {
        TextView tv;
    }

    static class ChildHolder {
        TextView tv1;
        TextView tv2;
        TextView tv3;
    }
}
