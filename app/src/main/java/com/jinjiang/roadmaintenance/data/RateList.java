package com.jinjiang.roadmaintenance.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wuwei on 2017/7/14.
 */

public class RateList implements Serializable {


    /**
     * rows : [{"area":"36.00","createTime":"2017-07-14 00:12:52","locationDesc":"湖北省武汉市江汉区青年路64-3号","orderStatusName":"待审批","processInstanceId":"245033","taskId":"245047","workOrderId":118}]
     * total : 1
     */

    private int total;
    private List<RowsBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean implements Serializable{
        /**
         * area : 36.00
         * createTime : 2017-07-14 00:12:52
         * locationDesc : 湖北省武汉市江汉区青年路64-3号
         * orderStatusName : 待审批
         * processInstanceId : 245033
         * taskId : 245047
         * workOrderId : 118
         */

        private String area;
        private String createTime;
        private String locationDesc;
        private String orderStatusName;
        private String processInstanceId;
        private String taskId;
        private int workOrderId;

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getLocationDesc() {
            return locationDesc;
        }

        public void setLocationDesc(String locationDesc) {
            this.locationDesc = locationDesc;
        }

        public String getOrderStatusName() {
            return orderStatusName;
        }

        public void setOrderStatusName(String orderStatusName) {
            this.orderStatusName = orderStatusName;
        }

        public String getProcessInstanceId() {
            return processInstanceId;
        }

        public void setProcessInstanceId(String processInstanceId) {
            this.processInstanceId = processInstanceId;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public int getWorkOrderId() {
            return workOrderId;
        }

        public void setWorkOrderId(int workOrderId) {
            this.workOrderId = workOrderId;
        }
    }
}
