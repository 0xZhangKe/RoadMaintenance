package com.jinjiang.roadmaintenance.ui.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
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
import com.jinjiang.roadmaintenance.ui.view.DialogProgress;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.jinjiang.roadmaintenance.utils.ACache;
import com.jinjiang.roadmaintenance.utils.Uri;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * 桌面
 */
public class MapFragment extends Fragment implements LoacationListener, UIDataListener {
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
    @BindView(R.id.chat_edit)
    EditText mMapEdit;
    private BaiduMap mBaiduMap;
    private LoacationModel locationModel;
    private MyLocationData locData;
    private LatLng myCenpt;
    private LatLng mEventCenpt;
    private AlertDialog mDialog;
    private BitmapDescriptor bitmapDescriptor_location;
    private ACache mAcache;
    private Dialog dialog;
    private NetWorkRequest request;
    private UserInfo userInfo;
    private ArrayList<RoadType> mRoadTypeList;
    private GeoCoder mSearch;
    private String address;
    private int userRole;
    private int state = 0;
    private ArrayList<EventTypeGrid> mGridlist;
    private CommonAdapter<EventTypeGrid> gridAdapter;

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
        dialog = DialogProgress.createLoadingDialog(getActivity(), "", this);
        locationModel = new LoacationModel(getActivity(), this, 3000);
        request = new NetWorkRequest(getActivity(), this);

        userInfo = (UserInfo) mAcache.getAsObject("UserInfo");

        if (userInfo == null || TextUtils.isEmpty(userInfo.getUserId())) {
            myToast.toast(getActivity(), "登录状态已过期，请重新登录！");
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }

        userRole = userInfo.getUserRole();
        if (userRole == 5 || userRole == 6) {
            mAdd.setVisibility(View.VISIBLE);
        } else {
            mAdd.setVisibility(View.GONE);
        }

        Map map = new HashMap();
        map.put("userId", userInfo.getUserId());
        map.put("appSid", userInfo.getAppSid());
        JSONObject object = new JSONObject();
        object.put("typeKey", "order_type");
        map.put("body", object.toJSONString());
        request.doPostRequest(0, true, Uri.getDicByTypeKey, map);
    }

    private void initData() {
        mGridlist = new ArrayList<>();
        mGridlist.add(new EventTypeGrid(R.drawable.state_quabbu2, "全部工单"));
        mGridlist.add(new EventTypeGrid(R.drawable.state_dengdai1, "等待维修"));
        mGridlist.add(new EventTypeGrid(R.drawable.state_zhengzai1, "正在维修"));
        mGridlist.add(new EventTypeGrid(R.drawable.state_wancheng1, "维修完成"));
        gridAdapter = new CommonAdapter<EventTypeGrid>(getActivity(), R.layout.item_map_grid, mGridlist) {
            @Override
            protected void convert(ViewHolder viewHolder, EventTypeGrid item, int position) {
                viewHolder.setImageResource(R.id.item_mapgrid_img, item.getImg());
                viewHolder.setText(R.id.item_mapgrid_text, item.getText());
            }
        };
        mGrid.setAdapter(gridAdapter);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0 && state != 0) {
                    state = 0;
                    mGridlist.get(0).setImg(R.drawable.state_quabbu2);
                    mGridlist.get(1).setImg(R.drawable.state_dengdai1);
                    mGridlist.get(2).setImg(R.drawable.state_zhengzai1);
                    mGridlist.get(3).setImg(R.drawable.state_wancheng1);
                } else if (position == 1 && state != 1) {
                    state = 1;
                    mGridlist.get(0).setImg(R.drawable.state_quabbu1);
                    mGridlist.get(1).setImg(R.drawable.state_dengdai2);
                    mGridlist.get(2).setImg(R.drawable.state_zhengzai1);
                    mGridlist.get(3).setImg(R.drawable.state_wancheng1);
                } else if (position == 2 && state != 2) {
                    state = 2;
                    mGridlist.get(0).setImg(R.drawable.state_quabbu1);
                    mGridlist.get(1).setImg(R.drawable.state_dengdai1);
                    mGridlist.get(2).setImg(R.drawable.state_zhengzai2);
                    mGridlist.get(3).setImg(R.drawable.state_wancheng1);
                } else if (position == 3 && state != 3) {
                    state = 3;
                    mGridlist.get(0).setImg(R.drawable.state_quabbu1);
                    mGridlist.get(1).setImg(R.drawable.state_dengdai1);
                    mGridlist.get(2).setImg(R.drawable.state_zhengzai1);
                    mGridlist.get(3).setImg(R.drawable.state_wancheng2);
                }
                gridAdapter.notifyDataSetChanged();
//                mShadow.setVisibility(View.GONE);
//                mEventType_ll.setVisibility(View.GONE);
            }
        });
        initBaiduMap();

        mMapEdit.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getActivity().getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    search();
                }
                return false;
            }
        });
    }

    private void initBaiduMap() {

        bitmapDescriptor_location = BitmapDescriptorFactory
                .fromResource(R.drawable.location_icon);

        mBaiduMap = mMapView.getMap();
//        mMapView.showZoomControls(false);
        mBaiduMap.setMyLocationEnabled(true);
        LatLng cenpt = new LatLng(24.787996, 118.558403);
        setbaiduCenter(cenpt, 13);
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (userRole == 5 || userRole == 6) {
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
            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker != null && marker.getExtraInfo() != null) {
                    //从marker中获取info信息
                    Bundle bundle = marker.getExtraInfo();
                    if (bundle.containsKey("LatLng")) {
                        if (mDialog != null)
                            mDialog.show();

                        mEventCenpt = marker.getPosition();
                    }
                    mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                            .location(marker.getPosition()));
                }
                return true;
            }
        });

        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(listener);
    }

    /**
     * 创建病害选择对话框
     */
    private void creatDialog(String[] items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("选择类型");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), EventAddActivity.class);
                intent.putExtra("value", mRoadTypeList.get(which).getValue());
                intent.putExtra("address", address);
                intent.putExtra("Cenpt", mEventCenpt);
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

    @OnClick({R.id.map_add, R.id.map_mylocation, R.id.map_eventType_icon, R.id.map_shadow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.map_add://新增
                if (myCenpt != null) {
                    if (mDialog != null)
                        mDialog.show();
                    mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                            .location(myCenpt));
                    mEventCenpt = myCenpt;
                } else {
                    showToast("未能定位到当前位置！");
                }
                break;
            case R.id.map_mylocation:
                if (myCenpt != null) {
                    setbaiduCenter(myCenpt, 9);
                } else {
                    myToast.toast(getActivity(), "正在定位中...");
                }
                break;
            case R.id.map_shadow:
            case R.id.map_eventType_icon:
                if (mShadow.getVisibility() == View.GONE) {
                    mShadow.setVisibility(View.VISIBLE);
                    mEventType_ll.setVisibility(View.VISIBLE);
                } else {
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

    /**
     * 地理位置反编译
     */
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
            address = result.getAddress();
        }
    };

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
        if (mMapView != null)
            mMapView.onDestroy();
        bitmapDescriptor_location.recycle();
        mSearch.destroy();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (code == 0) {
            if (data != null) {
                mRoadTypeList = JSON.parseObject(data.toString(), new TypeReference<ArrayList<RoadType>>() {
                });
                if (mRoadTypeList != null && mRoadTypeList.size() > 0) {
                    String[] item = new String[mRoadTypeList.size()];
                    for (int i = 0; i < mRoadTypeList.size(); i++) {
                        item[i] = mRoadTypeList.get(i).getText();
                    }
                    creatDialog(item);
                }
            }
        }

    }

    @Override
    public void showToast(String message) {
        myToast.toast(getActivity(), message);
    }

    @Override
    public void showDialog() {
        if (dialog != null)
            dialog.show();
    }

    @Override
    public void dismissDialog() {
        if (dialog != null)
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
