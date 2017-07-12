package com.jinjiang.roadmaintenance.data;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * Created by wuwei on 2017/7/5.
 * 病害类型
 */
@Table(database = AppDatabase.class)
public class EventType extends BaseModel implements Serializable {

    /**
     * detail :
     * id : 1
     * name : 纵向裂缝
     * orderType : 1
     */
    @PrimaryKey
    private String id;
    @Column
    private long eventId;
    @Column
    private String detail;
    @Column
    private String name;
    @Column
    private int orderType;
    @Column
    private String picUrl;
    @Column
    private String desc;
    @Column
    public boolean ischecked;

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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }
}
