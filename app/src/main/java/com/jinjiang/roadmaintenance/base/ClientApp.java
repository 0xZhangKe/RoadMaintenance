package com.jinjiang.roadmaintenance.base;

import android.app.Application;

import com.apkfuns.logutils.LogLevel;
import com.apkfuns.logutils.LogUtils;
import com.baidu.mapapi.SDKInitializer;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;

/**
 * @ClassName: ClientApp
 * @Description: [BaseApp]
 * @Author: wuw
 */
public class ClientApp extends Application {
    private static ClientApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initLogs();

        SDKInitializer.initialize(getApplicationContext());

        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());
    }

    public static ClientApp getInstance() {
        return instance;
    }

    private void initLogs() {
        LogUtils.getLogConfig()
                .configAllowLog(true)
                .configTagPrefix("MyApp")
                .configShowBorders(true)
                .configLevel(LogLevel.TYPE_VERBOSE);
    }

}
