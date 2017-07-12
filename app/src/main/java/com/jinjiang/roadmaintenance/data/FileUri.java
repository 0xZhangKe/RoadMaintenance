package com.jinjiang.roadmaintenance.data;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * Created by wuwei on 2017/7/12.
 */
@Table(database = AppDatabase.class)
public class FileUri extends BaseModel implements Serializable{
    @PrimaryKey(autoincrement = true)
    public long id;
    @Column
    public long eventId;
    @Column
    public int type;
    @Column
    public String uri;
}
