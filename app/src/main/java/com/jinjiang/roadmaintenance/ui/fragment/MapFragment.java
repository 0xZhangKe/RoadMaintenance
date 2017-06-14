package com.jinjiang.roadmaintenance.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.model.LoacationListener;
import com.jinjiang.roadmaintenance.model.LoacationModel;
import com.jinjiang.roadmaintenance.ui.activity.EventAddActivity;
import com.jinjiang.roadmaintenance.ui.view.myToast;


/**
 * 桌面
 */
public class MapFragment extends Fragment implements LoacationListener, View.OnClickListener {
    private static MapFragment fragment;
    private TextureMapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LoacationModel locationModel;
    private MyLocationData locData;
    private ImageView mylocation;
    private LatLng myCenpt;
    private ImageView mAdd;


    public MapFragment() {
    }

    public static MapFragment newInstance() {

        if (fragment == null) {
            fragment = new MapFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mMapView = (TextureMapView) view.findViewById(R.id.map_bmapView);
        mylocation = (ImageView) view.findViewById(R.id.map_mylocation);
        mAdd = (ImageView) view.findViewById(R.id.map_add);

        mylocation.setOnClickListener(this);
        mAdd.setOnClickListener(this);

        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false);
        mBaiduMap.setMyLocationEnabled(true);
        LatLng cenpt = new LatLng(30.616744, 110.313039);
        setbaiduCenter(cenpt, 8);

    }

    /**
     * 设置地图中心点
     */
    private void setbaiduCenter(LatLng cenpt, int zoom) {
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(zoom)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    private void initData() {
        locationModel = new LoacationModel(getActivity(), this, 3000);
    }

    /**
     * 获取定位数据
     * @param location
     */
    @Override
    public void getLoacation(BDLocation location) {
        // 构造定位数据`
        locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            myCenpt = new LatLng(lat, lng);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.map_mylocation://定位到我的位置
                if (myCenpt != null) {
                    setbaiduCenter(myCenpt, 9);
                } else {
                    myToast.toast(getActivity(), "正在定位中...");
                }
                break;
            case R.id.map_add://新增
                startActivity(new Intent(getActivity(), EventAddActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationModel.stopLoacation();
        mMapView.onDestroy();
    }


}
