package com.jinjiang.roadmaintenance.ui.activity;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
import com.jinjiang.roadmaintenance.model.LoacationListener;
import com.jinjiang.roadmaintenance.model.LoacationModel;
import com.jinjiang.roadmaintenance.ui.view.DrivingRouteOverlay;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.jinjiang.roadmaintenance.utils.PermissionUtil;

public class NavigationActivity extends BaseActivity implements LoacationListener {

    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private LoacationModel loacationModel;
    private MyLocationData locData;
    private double lat;
    private double lot;
    private double lat2 = 30.616744;
    private double lot2 = 110.313039;
    private boolean isLocated = false;
    private RoutePlanSearch mSearch;
    private PlanNode stNode;
    private PlanNode enNode;
    private DrivingRouteOverlay drivingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_navigation;
    }

    @Override
    protected void initUI() {
        findViewById(R.id.navigation_back).setOnClickListener(this);

        mMapView = (TextureMapView) findViewById(R.id.navigation_bmapView);

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);

        LatLng latLng = new LatLng(30.616744, 110.313039);
        setbaiduCenter(latLng, 8);
    }

    @Override
    protected void initData() {
        if (PermissionUtil.isLacksOfPermission(PermissionUtil.PERMISSION[1]) || PermissionUtil.isLacksOfPermission(PermissionUtil.PERMISSION[2])) {
            ActivityCompat.requestPermissions(this, PermissionUtil.PERMISSION, 0x12);
        }
        loacationModel = new LoacationModel(NavigationActivity.this, this, 3000);

        initNavigation();
    }

    private  void initNavigation(){
        final OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener(){

            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR && result.error != SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    myToast.toast(NavigationActivity.this, "抱歉，未查询到线路结果！");
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    drivingOverlay = null;
                    drivingOverlay = new DrivingRouteOverlay(mBaiduMap);
                    mBaiduMap.setOnMarkerClickListener(drivingOverlay);
                    drivingOverlay.setData(result.getRouteLines().get(0));
                    drivingOverlay.addToMap();
                    drivingOverlay.zoomToSpan();

//                    List<DrivingRouteLine.DrivingStep> tempList = result.getRouteLines().get(0).getAllStep();
                }
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };

        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(listener);

    }

    private void searchLine(){
        stNode = PlanNode.withLocation(new LatLng(lat, lot));
        enNode = PlanNode.withLocation(new LatLng(lat2, lot2));
        mSearch.drivingSearch(new DrivingRoutePlanOption()
                .from(stNode)
                .to(enNode));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navigation_back:
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
                break;
            default:
                break;
        }
    }

    @Override
    public void getLoacation(BDLocation location) {
        locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);
        if (!isLocated) {
            lat = location.getLatitude();
            lot = location.getLongitude();
            isLocated = true;
            LatLng cenpt = new LatLng(location.getLatitude(), location.getLongitude());
            setbaiduCenter(cenpt, 12);
            searchLine();
        }
    }
    /**
     * 设置地图居中于当前位置
     *
     * @param cenpt
     */
    private void setbaiduCenter(LatLng cenpt, int zoom) {
        //设定中心点坐标
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(zoom)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        loacationModel.stopLoacation();
        mBaiduMap = null;
    }
}
