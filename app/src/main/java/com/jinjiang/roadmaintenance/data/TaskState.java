package com.jinjiang.roadmaintenance.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wuwei on 2017/7/6.
 */

public class TaskState implements Serializable {

    /**
     * orderStatus : 1002
     * orderStatusName : 已提交，未施工
     * taskCount : 0
     */

    private int orderStatus;
    private String orderStatusName;
    private int taskCount;
    private ArrayList<Task> tasks;

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
}
