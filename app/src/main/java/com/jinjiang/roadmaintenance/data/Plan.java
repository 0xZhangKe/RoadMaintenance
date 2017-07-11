package com.jinjiang.roadmaintenance.data;

import java.io.Serializable;

/**
 * Created by wuwei on 2017/7/10.
 * 施工方案
 */

public class Plan implements Serializable {
    private String id;
    private String funName;
    private String funDetail;

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
}
