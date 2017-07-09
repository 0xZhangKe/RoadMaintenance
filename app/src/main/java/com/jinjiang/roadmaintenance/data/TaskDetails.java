package com.jinjiang.roadmaintenance.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wuwei on 2017/7/7.
 */

public class TaskDetails implements Serializable {

    /**
     * diseaseMsgDtos : [{"detail":"111","diseaseAttrMsgDtos":[{"diseaseAttrId":473,"diseaseAttrName":"长(m)","typeUnitId":1,"value":"2"},{"diseaseAttrId":474,"diseaseAttrName":"宽(m)","typeUnitId":111,"value":"0.2"},{"diseaseAttrId":475,"diseaseAttrName":"面积(㎡)","typeUnitId":112,"value":"0.40"}],"diseaseId":223,"diseaseType":1,"diseaseTypeName":"纵向裂缝","planArea":0,"planDetail":"","planTime":0,"practicalArea":0,"practicalTime":0,"workOrderId":0},{"detail":"222","diseaseAttrMsgDtos":[{"diseaseAttrId":476,"diseaseAttrName":"长(m)","typeUnitId":3,"value":"3"},{"diseaseAttrId":477,"diseaseAttrName":"宽(m)","typeUnitId":4,"value":"1"},{"diseaseAttrId":478,"diseaseAttrName":"面积(㎡)","typeUnitId":5,"value":"3.00"}],"diseaseId":224,"diseaseType":3,"diseaseTypeName":"块状裂缝","planArea":0,"planDetail":"","planTime":0,"practicalArea":0,"practicalTime":0,"workOrderId":0}]
     * taskCreateTime :
     * taskId :
     * taskName :
     * workOrderMsgDto : {"area":3.4,"createDt":"2017-07-09 11:17:28","detail":"bbb","latitude":110.35052,"lineType":1,"lineTypeName":"机动车道","locationDesc":"湖北省神农架林区","longitude":110.35052,"moneyPlan":0,"moneyPractical":0,"orderStatus":1,"orderType":1,"orderTypeName":"沥青路面","picUrls":["http://192.168.199.124/road/upload/disease/lijw/A-LQLM-20170709-01-01.jpg","http://192.168.199.124/road/upload/disease/lijw/A-LQLM-20170709-01-00.jpg"],"sectiongId":1,"sn":"A-LQLM-20170709-01","timePlan":0,"timePractical":0,"userId":"lijw","workOrderId":88}
     */

    private String taskCreateTime;
    private String taskId;
    private String taskName;
    private WorkOrderMsgDtoBean workOrderMsgDto;
    private List<DiseaseMsgDtosBean> diseaseMsgDtos;

    public String getTaskCreateTime() {
        return taskCreateTime;
    }

    public void setTaskCreateTime(String taskCreateTime) {
        this.taskCreateTime = taskCreateTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public WorkOrderMsgDtoBean getWorkOrderMsgDto() {
        return workOrderMsgDto;
    }

    public void setWorkOrderMsgDto(WorkOrderMsgDtoBean workOrderMsgDto) {
        this.workOrderMsgDto = workOrderMsgDto;
    }

    public List<DiseaseMsgDtosBean> getDiseaseMsgDtos() {
        return diseaseMsgDtos;
    }

    public void setDiseaseMsgDtos(List<DiseaseMsgDtosBean> diseaseMsgDtos) {
        this.diseaseMsgDtos = diseaseMsgDtos;
    }

    public static class WorkOrderMsgDtoBean {
        /**
         * area : 3.4
         * createDt : 2017-07-09 11:17:28
         * detail : bbb
         * latitude : 110.35052
         * lineType : 1
         * lineTypeName : 机动车道
         * locationDesc : 湖北省神农架林区
         * longitude : 110.35052
         * moneyPlan : 0
         * moneyPractical : 0
         * orderStatus : 1
         * orderType : 1
         * orderTypeName : 沥青路面
         * picUrls : ["http://192.168.199.124/road/upload/disease/lijw/A-LQLM-20170709-01-01.jpg","http://192.168.199.124/road/upload/disease/lijw/A-LQLM-20170709-01-00.jpg"]
         * sectiongId : 1
         * sn : A-LQLM-20170709-01
         * timePlan : 0
         * timePractical : 0
         * userId : lijw
         * workOrderId : 88
         */

        private double area;
        private String createDt;
        private String detail;
        private double latitude;
        private int lineType;
        private String lineTypeName;
        private String locationDesc;
        private double longitude;
        private int moneyPlan;
        private int moneyPractical;
        private int orderStatus;
        private int orderType;
        private String orderTypeName;
        private int sectiongId;
        private String sn;
        private int timePlan;
        private int timePractical;
        private String userId;
        private int workOrderId;
        private List<String> picUrls;

        public double getArea() {
            return area;
        }

        public void setArea(double area) {
            this.area = area;
        }

        public String getCreateDt() {
            return createDt;
        }

        public void setCreateDt(String createDt) {
            this.createDt = createDt;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public int getLineType() {
            return lineType;
        }

        public void setLineType(int lineType) {
            this.lineType = lineType;
        }

        public String getLineTypeName() {
            return lineTypeName;
        }

        public void setLineTypeName(String lineTypeName) {
            this.lineTypeName = lineTypeName;
        }

        public String getLocationDesc() {
            return locationDesc;
        }

        public void setLocationDesc(String locationDesc) {
            this.locationDesc = locationDesc;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public int getMoneyPlan() {
            return moneyPlan;
        }

        public void setMoneyPlan(int moneyPlan) {
            this.moneyPlan = moneyPlan;
        }

        public int getMoneyPractical() {
            return moneyPractical;
        }

        public void setMoneyPractical(int moneyPractical) {
            this.moneyPractical = moneyPractical;
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

        public int getSectiongId() {
            return sectiongId;
        }

        public void setSectiongId(int sectiongId) {
            this.sectiongId = sectiongId;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public int getTimePlan() {
            return timePlan;
        }

        public void setTimePlan(int timePlan) {
            this.timePlan = timePlan;
        }

        public int getTimePractical() {
            return timePractical;
        }

        public void setTimePractical(int timePractical) {
            this.timePractical = timePractical;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getWorkOrderId() {
            return workOrderId;
        }

        public void setWorkOrderId(int workOrderId) {
            this.workOrderId = workOrderId;
        }

        public List<String> getPicUrls() {
            return picUrls;
        }

        public void setPicUrls(List<String> picUrls) {
            this.picUrls = picUrls;
        }
    }

    public static class DiseaseMsgDtosBean {
        /**
         * detail : 111
         * diseaseAttrMsgDtos : [{"diseaseAttrId":473,"diseaseAttrName":"长(m)","typeUnitId":1,"value":"2"},{"diseaseAttrId":474,"diseaseAttrName":"宽(m)","typeUnitId":111,"value":"0.2"},{"diseaseAttrId":475,"diseaseAttrName":"面积(㎡)","typeUnitId":112,"value":"0.40"}]
         * diseaseId : 223
         * diseaseType : 1
         * diseaseTypeName : 纵向裂缝
         * planArea : 0
         * planDetail :
         * planTime : 0
         * practicalArea : 0
         * practicalTime : 0
         * workOrderId : 0
         */

        private String detail;
        private int diseaseId;
        private int diseaseType;
        private String diseaseTypeName;
        private int planArea;
        private String planDetail;
        private int planTime;
        private int practicalArea;
        private int practicalTime;
        private int workOrderId;
        private List<DiseaseAttrMsgDtosBean> diseaseAttrMsgDtos;

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public int getDiseaseId() {
            return diseaseId;
        }

        public void setDiseaseId(int diseaseId) {
            this.diseaseId = diseaseId;
        }

        public int getDiseaseType() {
            return diseaseType;
        }

        public void setDiseaseType(int diseaseType) {
            this.diseaseType = diseaseType;
        }

        public String getDiseaseTypeName() {
            return diseaseTypeName;
        }

        public void setDiseaseTypeName(String diseaseTypeName) {
            this.diseaseTypeName = diseaseTypeName;
        }

        public int getPlanArea() {
            return planArea;
        }

        public void setPlanArea(int planArea) {
            this.planArea = planArea;
        }

        public String getPlanDetail() {
            return planDetail;
        }

        public void setPlanDetail(String planDetail) {
            this.planDetail = planDetail;
        }

        public int getPlanTime() {
            return planTime;
        }

        public void setPlanTime(int planTime) {
            this.planTime = planTime;
        }

        public int getPracticalArea() {
            return practicalArea;
        }

        public void setPracticalArea(int practicalArea) {
            this.practicalArea = practicalArea;
        }

        public int getPracticalTime() {
            return practicalTime;
        }

        public void setPracticalTime(int practicalTime) {
            this.practicalTime = practicalTime;
        }

        public int getWorkOrderId() {
            return workOrderId;
        }

        public void setWorkOrderId(int workOrderId) {
            this.workOrderId = workOrderId;
        }

        public List<DiseaseAttrMsgDtosBean> getDiseaseAttrMsgDtos() {
            return diseaseAttrMsgDtos;
        }

        public void setDiseaseAttrMsgDtos(List<DiseaseAttrMsgDtosBean> diseaseAttrMsgDtos) {
            this.diseaseAttrMsgDtos = diseaseAttrMsgDtos;
        }

        public static class DiseaseAttrMsgDtosBean {
            /**
             * diseaseAttrId : 473
             * diseaseAttrName : 长(m)
             * typeUnitId : 1
             * value : 2
             */

            private int diseaseAttrId;
            private String diseaseAttrName;
            private int typeUnitId;
            private String value;

            public int getDiseaseAttrId() {
                return diseaseAttrId;
            }

            public void setDiseaseAttrId(int diseaseAttrId) {
                this.diseaseAttrId = diseaseAttrId;
            }

            public String getDiseaseAttrName() {
                return diseaseAttrName;
            }

            public void setDiseaseAttrName(String diseaseAttrName) {
                this.diseaseAttrName = diseaseAttrName;
            }

            public int getTypeUnitId() {
                return typeUnitId;
            }

            public void setTypeUnitId(int typeUnitId) {
                this.typeUnitId = typeUnitId;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
