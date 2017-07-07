package com.jinjiang.roadmaintenance.data;

import java.io.Serializable;

/**
 * Created by wuwei on 2017/7/5.
 * 病害类型
 */

public class EventType implements Serializable {

    /**
     * detail :
     * id : 1
     * name : 纵向裂缝
     * orderType : 1
     */

    private String detail;
    private String id;
    private String name;
    private int orderType;
    private String desc;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
