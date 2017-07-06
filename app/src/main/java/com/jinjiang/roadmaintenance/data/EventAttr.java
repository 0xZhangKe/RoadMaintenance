package com.jinjiang.roadmaintenance.data;

import java.io.Serializable;

/**
 * Created by wuwei on 2017/7/5.
 * 病害属性
 */

public class EventAttr implements Serializable {

    /**
     * typeUnitId : 6
     * name : 长(m)
     * defaultVal :
     */

    private String typeUnitId;
    private String name;
    private String defaultVal;
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
}
