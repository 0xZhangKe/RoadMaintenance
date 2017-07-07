package com.jinjiang.roadmaintenance.data;

import java.io.Serializable;

/**
 * Created by wuwei on 2017/7/6.
 */

public class Task implements Serializable {

    /**
     * createDt : 2017-07-04 20:19:17
     * locationDesc :
     * orderStatus : 2
     * orderType : 1
     * orderTypeName : 沥青路面
     * taskId : 157521
     * workOrderId : 76
     */

    private String createDt;
    private String locationDesc;
    private int orderStatus;
    private int orderType;
    private String orderTypeName;
    private String taskId;
    private String workOrderId;

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public String getLocationDesc() {
        return locationDesc;
    }

    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
}
