package com.jinjiang.roadmaintenance.data;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * Created by wuwei on 2017/7/5.
 * 病害属性
 */
@Table(database = AppDatabase.class)
public class EventAttr extends BaseModel implements Serializable {

    /**
     * typeUnitId : 6
     * name : 长(m)
     * defaultVal :
     */
    @PrimaryKey
    private String typeUnitId;
    @Column
    private String eventTypeId;//非必须
    @Column
    private long eventId;//非必须
    @Column
    private String name;
    @Column
    private String defaultVal;
    @Column
    private String Value;

    public String getTypeUnitId() {
        return typeUnitId;
    }

    public void setTypeUnitId(String typeUnitId) {
        this.typeUnitId = typeUnitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(String eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }
}
