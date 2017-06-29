package com.jinjiang.roadmaintenance.ui.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.data.EventTypeGrid;
import com.jinjiang.roadmaintenance.data.RoadType;
import com.jinjiang.roadmaintenance.data.UserInfo;
import com.jinjiang.roadmaintenance.model.LoacationListener;
import com.jinjiang.roadmaintenance.model.LoacationModel;
import com.jinjiang.roadmaintenance.model.NetWorkRequest;
import com.jinjiang.roadmaintenance.model.UIDataListener;
import com.jinjiang.roadmaintenance.ui.activity.EventAddActivity;
import com.jinjiang.roadmaintenance.ui.activity.LoginActivity;
import com.jinjiang.roadmaintenance.ui.activity.MainActivity;
import com.jinjiang.roadmaintenance.ui.view.DialogProgress;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.jinjiang.roadmaintenance.utils.ACache;
import com.jinjiang.roadmaintenance.utils.Uri;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 桌面
 */
public class MapFragment extends Fragment implements LoacationListener ,UIDataListener{
    private static MapFragment fragment;
    @BindView(R.id.map_bmapView)
    TextureMapView mMapView;
    @BindView(R.id.map_add)
    ImageView mAdd;
    @BindView(R.id.map_mylocation)
    ImageView mylocation;
    @BindView(R.id.map_shadow)
    View mShadow;
    @BindView(R.id.map_EventType_grid)
    GridView mGrid;
    @BindView(R.id.map_eventType_ll)
    LinearLayout mEventType_ll;
    Unbinder unbinder;
    private BaiduMap mBaiduMap;
    private LoacationModel locationModel;
    private MyLocationData locData;
    private LatLng myCenpt;
    private AlertDialog mDialog;
    private BitmapDescriptor bitmapDescriptor_location;
    private ACache mAcache;
    private Dialog dialog;
    private NetWorkRequest request;
    private UserInfo userInfo;
    private ArrayList<RoadType> mRoadTypeList;


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
        unbinder = ButterKnife.bind(this, view);

        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {

        mAcache = ACache.get(getActivity());
        dialog = DialogProgress.createLoadingDialog(getActivity(),"",this);
        locationModel = new LoacationModel(getActivity(), this, 3000);
        request = new NetWorkRequest(getActivity(),this);

        userInfo = (UserInfo) mAcache.getAsObject("UserInfo");

        if (userInfo ==null|| TextUtils.isEmpty(userInfo.getUserId())){
            myToast.toast(getActivity(),"登录状态已过期，请重新登录！");
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }

        Map map = new HashMap();
        map.put("userId",userInfo.getUserId());
        map.put("appSid",userInfo.getAppSid());
        map.put("body","{\"typeKey\":\"order_type\"}");
        request.doPostRequest(0,true, Uri.getDicByTypeKey,map);
    }

    private void initData() {
        ArrayList<EventTypeGrid> mGridlist = new ArrayList<>();
        mGridlist.add(new EventTypeGrid(R.drawable.pic_not_found,"全部病害"));
        mGridlist.add(new EventTypeGrid(R.drawable.pic_not_found,"等待维修"));
        mGridlist.add(new EventTypeGrid(R.drawable.pic_not_found,"正在维修"));
        mGridlist.add(new EventTypeGrid(R.drawable.pic_not_found,"维修完成"));
        mGrid.setAdapter(new CommonAdapter<EventTypeGrid>(getActivity(),R.layout.item_map_grid,mGridlist) {
            @Override
            protected void convert(ViewHolder viewHolder, EventTypeGrid item, int position) {
                viewHolder.setImageResource(R.id.item_mapgrid_img,item.getImg());
                viewHolder.setText(R.id.item_mapgrid_text,item.getText());
            }
        });
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mShadow.setVisibility(View.GONE);
                mEventType_ll.setVisibility(View.GONE);
            }
        });
        initBaiduMap();
    }

    private void initBaiduMap() {

        bitmapDescriptor_location = BitmapDescriptorFactory
                .fromResource(R.drawable.location_icon);

        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false);
        mBaiduMap.setMyLocationEnabled(true);
        LatLng cenpt = new LatLng(30.616744, 110.313039);
        setbaiduCenter(cenpt, 8);
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (mBaiduMap != null)
                    mBaiduMap.clear();

                MarkerOptions option = new MarkerOptions()
                        .position(latLng)
                        .icon(bitmapDescriptor_location);
                Marker marker = (Marker) mBaiduMap.addOverlay(option);
                Bundle bundle = new Bundle();
                bundle.putString("LatLng", "");
                marker.setExtraInfo(bundle);
            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker != null && marker.getExtraInfo() != null) {
                    //从marker中获取info信息
                    Bundle bundle = marker.getExtraInfo();
                    if (bundle.containsKey("LatLng")) {
                        mDialog.show();
                    }
                }
                return true;
            }
        });
    }

    /**
     * 创建病害选择对话框
     */
    private void creatDialog(String[] items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("选择病害");
//        builder.setIcon(R.drawable.tools);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), EventAddActivity.class);
                intent.putExtra("road", which);
                startActivity(intent);
            }
        });
        mDialog = builder.create();
    }

    /**
     * 获取定位数据
     *
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

    @OnClick({R.id.map_add, R.id.map_mylocation, R.id.map_eventType_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.map_add://新增
                mDialog.show();
                break;
            case R.id.map_mylocation:
                if (myCenpt != null) {
                    setbaiduCenter(myCenpt, 9);
                } else {
                    myToast.toast(getActivity(), "正在定位中...");
                }
                break;
            case R.id.map_eventType_icon:
                if (mShadow.getVisibility()==View.GONE){
                    mShadow.setVisibility(View.VISIBLE);
                    mEventType_ll.setVisibility(View.VISIBLE);
                }else {
                    mShadow.setVisibility(View.GONE);
                    mEventType_ll.setVisibility(View.GONE);
                }
                break;
        }
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
        bitmapDescriptor_location.recycle();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (code==0){
            if (data!=null){
                mRoadTypeList = JSON.parseObject(data.toString(), new TypeReference<ArrayList<RoadType>>(){});
                if (mRoadTypeList!=null&&mRoadTypeList.size()>0){
                    String[] item = new String[mRoadTypeList.size()];
                    for (int i=0;i<mRoadTypeList.size();i++){
                        item[i] = mRoadTypeList.get(i).getText();
                    }
                    creatDialog(item);
                }
            }
        }

    }
    @Override
    public void showToast(String message) {
        myToast.toast(getActivity(),message);
    }

    @Override
    public void showDialog() {
        if (dialog !=null)
            dialog.show();
    }

    @Override
    public void dismissDialog() {
        if (dialog !=null)
            dialog.dismiss();
    }

    @Override
    public void onError(String errorCode, String errorMessage) {
        showToast(errorMessage);
    }

    @Override
    public void cancelRequest() {
        request.CancelPost();
    }
}
