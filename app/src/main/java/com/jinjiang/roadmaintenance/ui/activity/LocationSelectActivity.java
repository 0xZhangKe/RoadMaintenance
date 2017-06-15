package com.jinjiang.roadmaintenance.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
import com.jinjiang.roadmaintenance.model.LoacationListener;
import com.jinjiang.roadmaintenance.model.LoacationModel;
import com.jinjiang.roadmaintenance.utils.PermissionUtil;

/**
 * 选择事故位置页面
 */
public class LocationSelectActivity extends BaseActivity implements BaiduMap.OnMapStatusChangeListener, View.OnClickListener, LoacationListener {

    private LinearLayout mback;
    private TextureMapView mMapView;
    private ImageView locationImg;
    private TextView infoText1;
    private BaiduMap mBaiduMap;
    private LinearLayout confirm;

    double lat = 0;
    double lot = 0;
    private String address = "";
    private LoacationModel loacationModel;
    private boolean isLocated = false;
    private TranslateAnimation animationUp;
    private TranslateAnimation animationDown;
    private TextView infoText2;
    private TextView infoText3;
    private TextView infoText4;
    private MyLocationData locData;
    private GeoCoder mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_locationselect;
    }

    @Override
    protected void initUI() {

        mback = (LinearLayout) findViewById(R.id.locationselect_back);
        mMapView = (TextureMapView) findViewById(R.id.locationselect_bmapView);
        locationImg = (ImageView) findViewById(R.id.locationselect_location);
        infoText1 = (TextView) findViewById(R.id.locationselect_info1);
        infoText2 = (TextView) findViewById(R.id.locationselect_info2);
        infoText3 = (TextView) findViewById(R.id.locationselect_info3);
        infoText4 = (TextView) findViewById(R.id.locationselect_info4);
        confirm = (LinearLayout) findViewById(R.id.locationselect_confirm);

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapStatusChangeListener(this);
        mBaiduMap.setMyLocationEnabled(true);
        mback.setOnClickListener(this);
        confirm.setOnClickListener(this);

        isLocated = false;
    }

    @Override
    protected void initData() {
        if (PermissionUtil.isLacksOfPermission(PermissionUtil.PERMISSION[1]) || PermissionUtil.isLacksOfPermission(PermissionUtil.PERMISSION[2])) {
            ActivityCompat.requestPermissions(this, PermissionUtil.PERMISSION, 0x12);
        }
        loacationModel = new LoacationModel(LocationSelectActivity.this, this, 3000);

        animationUp = new TranslateAnimation(0, 0, 0, -20);
        animationUp.setDuration(300);
        animationUp.setFillAfter(true);
        animationDown = new TranslateAnimation(0, 0, -20, 0);
        animationDown.setDuration(300);
        animationDown.setFillAfter(true);

        LatLng latLng = new LatLng(30.616744, 110.313039);
        setbaiduCenter(latLng, 8);

        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(listener);
    }

    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
            }
            //获取地理编码结果
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
                return;
            }
            //获取反向地理编码结果
            infoText3.setText("您当前位置为：");
            infoText4.setText(result.getAddress());
            address = result.getAddress();
        }
    };

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.base_slide_out);
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
        mSearch.destroy();
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
        locationImg.setAnimation(animationUp);
        animationUp.start();
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        locationImg.setAnimation(animationDown);
        animationDown.start();
        LatLng cenpt = mapStatus.target;
        lat = cenpt.latitude;
        lot = cenpt.longitude;
        infoText1.setText("所选地点经纬度为：");
        infoText2.setText(lot + "\n" + lat);
        infoText3.setText("");
        infoText4.setText("");

        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(cenpt));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.locationselect_back:
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
                break;
            case R.id.locationselect_confirm:
                Intent intent = new Intent();
                intent.putExtra("location", lat+"/"+lot);
                intent.putExtra("roadname", address);
                setResult(106, intent);
                finish();
                overridePendingTransition(0, R.anim.base_slide_out);
                break;
            default:
                break;
        }
    }

    @Override
    public void getLoacation(final BDLocation location) {
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    infoText1.setText("您当前经纬度为：");
                    infoText2.setText(lot + "\n" + lat);
                    infoText3.setText("您当前位置为：");
                    infoText4.setText(location.getAddrStr() + "(" + location.getLocationDescribe() + ")");
                    address = location.getAddrStr();
                }
            });

            loacationModel.stopLoacation();
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


}
