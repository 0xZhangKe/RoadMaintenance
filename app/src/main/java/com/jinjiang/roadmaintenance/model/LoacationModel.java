package com.jinjiang.roadmaintenance.model;

import android.content.Context;

import com.apkfuns.logutils.LogUtils;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

/**
 * 定位的model
 * 传入定位间隔时间，为0时只定位一次
 * 需>1000ms
 * Created by Administrator on 2016/10/10.
 */
public class LoacationModel {

    private Context context;
    private int time;
    private LoacationListener loacationListener;
    public static LocationClient mLocationClient;
    public BDLocationListener myListener = null;

    public LoacationModel(Context context, LoacationListener loacationListener, int time) {
        this.context = context;
        this.loacationListener = null;
        myListener = null;
        this.loacationListener = loacationListener;
        this.time = time;
        SDKInitializer.initialize(context.getApplicationContext());
        myListener = new MyLocationListener(loacationListener);
        mLocationClient = new LocationClient(context.getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        initLocation();
    }

    /**
     * 定位选项设置
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = time;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    public class MyLocationListener implements BDLocationListener {
        LoacationListener loacationListener;
        public MyLocationListener(LoacationListener loacationListener) {
            this.loacationListener = loacationListener;
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
//            LogUtils.d(location.getLatitude() + "//" + location.getLongitude() + "//" + location.getLocType());
            if (location == null){
                return;
            }
//            LogUtils.d(location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation || location.getLocType() == BDLocation.TypeOffLineLocation);
            if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation || location.getLocType() == BDLocation.TypeOffLineLocation) {
                LogUtils.d("getLoacation");
                loacationListener.getLoacation(location);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    public void stopLoacation() {
        mLocationClient.stop();
    }

    public void startLoacation() {
        mLocationClient.start();
    }
}
