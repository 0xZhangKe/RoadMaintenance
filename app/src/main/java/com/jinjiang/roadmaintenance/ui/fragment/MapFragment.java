package com.jinjiang.roadmaintenance.ui.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
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
import com.baidu.mapapi.map.OverlayOptions;
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
import com.jinjiang.roadmaintenance.data.MapData;
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
    private int[] state = new int[]{0,0,0,0};
    private ArrayList<EventTypeGrid> mGridlist;
    private ArrayList<MapData> mPointList;
    private CommonAdapter<EventTypeGrid> gridAdapter;
    private BitmapDescriptor icon1;
    private BitmapDescriptor icon2;
    private BitmapDescriptor icon3;
    private BitmapDescriptor icon4;
    private Marker marker_dot;

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

        icon1 = BitmapDescriptorFactory
                .fromBitmap(getViewBitmap(R.drawable.shenpi));
        icon2 = BitmapDescriptorFactory
                .fromBitmap(getViewBitmap(R.drawable.zhengzai));
        icon3 = BitmapDescriptorFactory
                .fromBitmap(getViewBitmap(R.drawable.dengdai));
        icon4 = BitmapDescriptorFactory
                .fromBitmap(getViewBitmap(R.drawable.wancheng));

        bitmapDescriptor_location = BitmapDescriptorFactory
                .fromBitmap(getViewBitmap(R.drawable.location_icon));

        mPointList = new ArrayList<>();
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


        if (userRole != 6) {
            getMapdata("", "");
        }
    }

    /**
     * 将View转换成Bitmap
     *
     * @return
     */

    private Bitmap getViewBitmap(int res) {

        View addViewContent = View.inflate(getActivity(), R.layout.item_marker, null);
        ((ImageView) addViewContent.findViewById(R.id.img)).setImageResource(res);
        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }

    private void getMapdata(String roadName, String orderStatus) {
        Map map = new HashMap();
        map.put("userId", userInfo.getUserId());
        map.put("appSid", userInfo.getAppSid());
        JSONObject object = new JSONObject();
        object.put("roadName", roadName);
        StringBuffer buffer = new StringBuffer();
        for (int i:state){
            if (i!=0){
                if (buffer.length()==0){
                    buffer.append(""+i);
                }else {
                    buffer.append(","+i);
                }
            }
        }
        object.put("orderStatus", buffer.toString());
        map.put("body", object.toJSONString());
        request.doPostRequest(1, true, Uri.getWorkOrderLocation, map);
    }

    private void initData() {
        mGridlist = new ArrayList<>();
//        mGridlist.add(new EventTypeGrid(R.drawable.state_quabbu2, "全部工单"));
        mGridlist.add(new EventTypeGrid(R.drawable.state_shenpi1, "待审批"));
        mGridlist.add(new EventTypeGrid(R.drawable.state_zhengzai1, "维修中"));
        mGridlist.add(new EventTypeGrid(R.drawable.state_dengdai1, "待验收"));
        mGridlist.add(new EventTypeGrid(R.drawable.state_wancheng1, "已完成"));
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

                if (position == 0) {
                    if (state[0]==0){
                        state[0]=2001;
                        mGridlist.get(0).setImg(R.drawable.state_shenpi2);
                    }else {
                        state[0]=0;
                        mGridlist.get(0).setImg(R.drawable.state_shenpi1);
                    }
                    getMapdata("", "");
                } else if (position == 1) {
                    if (state[1]==0){
                        state[1]=2002;
                        mGridlist.get(1).setImg(R.drawable.state_zhengzai2);
                    }else {
                        state[1]=0;
                        mGridlist.get(1).setImg(R.drawable.state_zhengzai1);
                    }
                    getMapdata("", "");
                } else if (position == 2) {
                    if (state[2]==0){
                        state[2]=2003;
                        mGridlist.get(2).setImg(R.drawable.state_dengdai2);
                    }else {
                        state[2]=0;
                        mGridlist.get(2).setImg(R.drawable.state_dengdai1);
                    }
                    getMapdata("", "");
                } else if (position == 3) {
                    if (state[3]==0){
                        state[3]=2004;
                        mGridlist.get(3).setImg(R.drawable.state_wancheng2);
                    }else {
                        state[3]=0;
                        mGridlist.get(3).setImg(R.drawable.state_wancheng1);
                    }
                    getMapdata("", "");
                }
                gridAdapter.notifyDataSetChanged();
            }
        });
        initBaiduMap();

        mMapEdit.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mMapEdit.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    // 先隐藏键盘
                    ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getActivity().getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    String s = mMapEdit.getText().toString();
                    LogUtils.d(s);
                    if (!TextUtils.isEmpty(s)) {
                        getMapdata(s, "");
                        mMapEdit.getText().clear();
                    }
                }
                return false;
            }
        });
    }

    private void initBaiduMap() {

        mBaiduMap = mMapView.getMap();
//        mMapView.showZoomControls(false);
        mBaiduMap.setMyLocationEnabled(true);
        LatLng cenpt = new LatLng(24.787996, 118.558403);
        setbaiduCenter(cenpt, 13);
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (userRole == 5 || userRole == 6) {
                    if (mBaiduMap != null&&marker_dot!=null)
                        marker_dot.remove();

                    MarkerOptions option = new MarkerOptions()
                            .position(latLng)
                            .icon(bitmapDescriptor_location);
                    marker_dot = (Marker) mBaiduMap.addOverlay(option);
                    Bundle bundle = new Bundle();
                    bundle.putString("LatLng","");
                    marker_dot.setExtraInfo(bundle);
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
        icon1.recycle();
        icon2.recycle();
        icon3.recycle();
        icon4.recycle();
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
        } else if (code == 1) {
            if (data != null) {
                mPointList = JSON.parseObject(data.toString(), new TypeReference<ArrayList<MapData>>() {
                });
                mBaiduMap.clear();
                if (mPointList != null && mPointList.size() > 0) {

                    for (MapData d : mPointList) {
                        LatLng point = new LatLng(d.getLatitude(), d.getLongitude());
                        //构建MarkerOption，用于在地图上添加Marker
                        OverlayOptions option = null;
                        if (d.getOrderStatus() == 2001) {//未处理
                            option = new MarkerOptions()
                                    .position(point)
                                    .icon(icon1);
                        } else if (d.getOrderStatus() == 2002) {//未处理
                            option = new MarkerOptions()
                                    .position(point)
                                    .icon(icon2);
                        } else if (d.getOrderStatus() == 2003) {//未处理
                            option = new MarkerOptions()
                                    .position(point)
                                    .icon(icon3);
                        } else if (d.getOrderStatus() == 2004) {//未处理
                            option = new MarkerOptions()
                                    .position(point)
                                    .icon(icon4);
                        }
                        Marker marker = (Marker) mBaiduMap.addOverlay(option);
                    }
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
