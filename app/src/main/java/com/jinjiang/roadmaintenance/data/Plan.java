package com.jinjiang.roadmaintenance.data;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * Created by wuwei on 2017/7/10.
 * 施工方案
 */
@Table(database = AppDatabase.class)
public class Plan extends BaseModel implements Serializable {
    @PrimaryKey
    private String id;
    @Column
    private String funName;
    @Column
    private long eventId;
    @Column
    private int type;
    @Column
    private String funDetail;
    @Column
    private String otherDesc;
    @Column
    private String picUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }

    public String getFunDetail() {
        return funDetail;
    }

    public void setFunDetail(String funDetail) {
        this.funDetail = funDetail;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getOtherDesc() {
        return otherDesc;
    }

    public void setOtherDesc(String otherDesc) {
        this.otherDesc = otherDesc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
