package com.jinjiang.roadmaintenance.data;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by wuwei on 2017/7/12.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase";

    public static final int VERSION = 1;
}
