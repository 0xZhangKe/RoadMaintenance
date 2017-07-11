package com.jinjiang.roadmaintenance.data;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wuwei on 2017/7/7.
 */

public class TaskDetails implements Serializable {

    /**
     * diseaseMsgDtos : [{"detail":"33","diseaseAttrMsgDtos":[{"diseaseAttrId":503,"diseaseAttrName":"长(m)","typeUnitId":1,"value":"3"},{"diseaseAttrId":504,"diseaseAttrName":"宽(m)","typeUnitId":111,"value":"0.2"},{"diseaseAttrId":505,"diseaseAttrName":"面积(㎡)","typeUnitId":112,"value":"0.60"}],"diseaseId":233,"diseaseType":1,"diseaseTypeName":"纵向裂缝","planArea":0,"planDetail":"","workOrderId":0}]
     * planFuns : [{"funDetail":"横向裂缝维修方案","funName":"横向裂缝维修方案","id":1}]
     * practicalFuns : []
     * taskCreateTime : 2017-07-11 10:40:35
     * taskId : 212516
     * taskInfos : [{"detail":"技术员病害采集","endTime":"2017-07-11 10:40:35","taskId":"212508","taskName":"技术员病害采集","userName":"黄伟(技术员)"}]
     * taskName : 施工
     * workOrderMsgDto : {"area":0.6,"createDt":"2017-07-11 10:40:34","detail":"6669","latitude":114.276566,"lineType":2,"lineTypeName":"机动车道","locationDesc":"湖北省武汉市江汉区新华下路110号","longitude":114.276566,"maintainPicUrls":[],"moneyPlan":6966,"moneyPractical":0,"orderStatus":6,"orderType":1,"orderTypeName":"沥青路面","picUrls":["http://yun1.seagetech.com:28080/road/upload/disease/huangw/A-LQLM-20170711-01/A-LQLM-20170711-01-00.jpg","http://yun1.seagetech.com:28080/road/upload/disease/huangw/A-LQLM-20170711-01/A-LQLM-20170711-01-01.jpg"],"processInstanceId":"212501","scenePicUrls":[],"sectiongId":1,"sn":"A-LQLM-20170711-01","timePlan":9,"timePractical":0,"userId":"huangw","workOrderId":97}
     */

    private String taskCreateTime;
    private String taskId;
    private String taskName;
    private WorkOrderMsgDtoBean workOrderMsgDto;
    private List<DiseaseMsgDtosBean> diseaseMsgDtos;
    private List<Plan> planFuns;
    private List<String> practicalFuns;
    private List<TaskInfosBean> taskInfos;

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

    public List<Plan> getPlanFuns() {
        return planFuns;
    }

    public void setPlanFuns(List<Plan> planFuns) {
        this.planFuns = planFuns;
    }

    public List<String> getPracticalFuns() {
        return practicalFuns;
    }

    public void setPracticalFuns(List<String> practicalFuns) {
        this.practicalFuns = practicalFuns;
    }

    public List<TaskInfosBean> getTaskInfos() {
        return taskInfos;
    }

    public void setTaskInfos(List<TaskInfosBean> taskInfos) {
        this.taskInfos = taskInfos;
    }

    public static class WorkOrderMsgDtoBean {
        /**
         * area : 0.6
         * createDt : 2017-07-11 10:40:34
         * detail : 6669
         * latitude : 114.276566
         * lineType : 2
         * lineTypeName : 机动车道
         * locationDesc : 湖北省武汉市江汉区新华下路110号
         * longitude : 114.276566
         * maintainPicUrls : []
         * moneyPlan : 6966
         * moneyPractical : 0
         * orderStatus : 6
         * orderType : 1
         * orderTypeName : 沥青路面
         * picUrls : ["http://yun1.seagetech.com:28080/road/upload/disease/huangw/A-LQLM-20170711-01/A-LQLM-20170711-01-00.jpg","http://yun1.seagetech.com:28080/road/upload/disease/huangw/A-LQLM-20170711-01/A-LQLM-20170711-01-01.jpg"]
         * processInstanceId : 212501
         * scenePicUrls : []
         * sectiongId : 1
         * sn : A-LQLM-20170711-01
         * timePlan : 9
         * timePractical : 0
         * userId : huangw
         * workOrderId : 97
         */

        private double area;
        private String createDt;
        private String detail;
        private double latitude;
        private int lineType;
        private String lineTypeName;
        private String locationDesc;
        private double longitude;
        private double moneyPlan;
        private double moneyPractical;
        private int orderStatus;
        private int orderType;
        private String orderTypeName;
        private String processInstanceId;
        private int sectiongId;
        private String sn;
        private int timePlan;
        private int timePractical;
        private String userId;
        private int workOrderId;
        private List<String> maintainPicUrls;
        private List<String> picUrls;
        private List<String> scenePicUrls;

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

        public double getMoneyPlan() {
            return moneyPlan;
        }

        public void setMoneyPlan(double moneyPlan) {
            this.moneyPlan = moneyPlan;
        }

        public double getMoneyPractical() {
            return moneyPractical;
        }

        public void setMoneyPractical(double moneyPractical) {
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

        public String getProcessInstanceId() {
            return processInstanceId;
        }

        public void setProcessInstanceId(String processInstanceId) {
            this.processInstanceId = processInstanceId;
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

        public List<String> getMaintainPicUrls() {
            return maintainPicUrls;
        }

        public void setMaintainPicUrls(List<String> maintainPicUrls) {
            this.maintainPicUrls = maintainPicUrls;
        }

        public List<String> getPicUrls() {
            return picUrls;
        }

        public void setPicUrls(List<String> picUrls) {
            this.picUrls = picUrls;
        }

        public List<String> getScenePicUrls() {
            return scenePicUrls;
        }

        public void setScenePicUrls(List<String> scenePicUrls) {
            this.scenePicUrls = scenePicUrls;
        }
    }

    public static class DiseaseMsgDtosBean {
        /**
         * detail : 33
         * diseaseAttrMsgDtos : [{"diseaseAttrId":503,"diseaseAttrName":"长(m)","typeUnitId":1,"value":"3"},{"diseaseAttrId":504,"diseaseAttrName":"宽(m)","typeUnitId":111,"value":"0.2"},{"diseaseAttrId":505,"diseaseAttrName":"面积(㎡)","typeUnitId":112,"value":"0.60"}]
         * diseaseId : 233
         * diseaseType : 1
         * diseaseTypeName : 纵向裂缝
         * planArea : 0
         * planDetail :
         * workOrderId : 0
         */

        private String detail;
        private String diseaseId;
        private int diseaseType;
        private String diseaseTypeName;
        private double planArea;
        private String planDetail;
        private int workOrderId;
        private List<DiseaseAttrMsgDtosBean> diseaseAttrMsgDtos;

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getDiseaseId() {
            return diseaseId;
        }

        public void setDiseaseId(String diseaseId) {
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

        public double getPlanArea() {
            return planArea;
        }

        public void setPlanArea(double planArea) {
            this.planArea = planArea;
        }

        public String getPlanDetail() {
            return planDetail;
        }

        public void setPlanDetail(String planDetail) {
            this.planDetail = planDetail;
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
             * diseaseAttrId : 503
             * diseaseAttrName : 长(m)
             * typeUnitId : 1
             * value : 3
             */
            private String diseaseAttrId;
            private String diseaseAttrName;
            private String typeUnitId;
            private String value;

            public String getDiseaseAttrId() {
                return diseaseAttrId;
            }

            public void setDiseaseAttrId(String diseaseAttrId) {
                this.diseaseAttrId = diseaseAttrId;
            }

            public String getDiseaseAttrName() {
                return diseaseAttrName;
            }

            public void setDiseaseAttrName(String diseaseAttrName) {
                this.diseaseAttrName = diseaseAttrName;
            }

            public String getTypeUnitId() {
                return typeUnitId;
            }

            public void setTypeUnitId(String typeUnitId) {
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


    public static class TaskInfosBean {
        /**
         * detail : 技术员病害采集
         * endTime : 2017-07-11 10:40:35
         * taskId : 212508
         * taskName : 技术员病害采集
         * userName : 黄伟(技术员)
         */

        private String detail;
        private String endTime;
        private String taskId;
        private String taskName;
        private String userName;

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
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

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
