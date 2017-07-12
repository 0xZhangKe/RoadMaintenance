package com.jinjiang.roadmaintenance.data;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by wuwei on 2017/7/12.
 */
@Table(database = AppDatabase.class)
public class SaveEventData extends BaseModel {
    @PrimaryKey
    public long id;
    @Column
    public String userId;
    @Column
    public String appSid;
    @Column
    public int orderType;
    @Column
    public int lineType;
    @Column
    public double longitude;
    @Column
    public double latitude;
    @Column
    public String locationDesc;
    @Column
    public String area;
    @Column
    public String moneyPlan;
    @Column
    public String timePlan;
    @Column
    public String detail;
    @Column
    public int roadvalue;
}
