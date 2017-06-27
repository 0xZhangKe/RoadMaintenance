package com.jinjiang.roadmaintenance.ui.activity;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jinjiang.roadmaintenance.R;
import com.jinjiang.roadmaintenance.base.BaseActivity;
import com.jinjiang.roadmaintenance.base.ExitUtils;
import com.jinjiang.roadmaintenance.data.TabEntity;
import com.jinjiang.roadmaintenance.ui.fragment.MapFragment;
import com.jinjiang.roadmaintenance.ui.fragment.MyFragment;
import com.jinjiang.roadmaintenance.ui.fragment.RateFragment;
import com.jinjiang.roadmaintenance.ui.fragment.TaskFragment;
import com.jinjiang.roadmaintenance.ui.view.myToast;
import com.jinjiang.roadmaintenance.utils.PermissionUtil;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private CommonTabLayout mTab;
    private String[] mTitles = {"地图", "任务","进度", "我"};
    private int[] mIconUnselectIds = {
            R.drawable.tab_home_unselect, R.drawable.tab_speech_unselect,
            R.drawable.tab_more_unselect,R.drawable.tab_contact_unselect};
    private int[] mIconSelectIds = {
            R.drawable.tab_home_select, R.drawable.tab_speech_select,
            R.drawable.tab_more_select,R.drawable.tab_contact_select};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private Fragment mapFragment;
    private Fragment taskFragment;
    private Fragment myFragment;
    private RateFragment rateFragment;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initUI() {
        mTab = (CommonTabLayout) findViewById(R.id.act_main_tab);

        if (PermissionUtil.isLacksOfPermission(PermissionUtil.PERMISSION[0]) ||
                PermissionUtil.isLacksOfPermission(PermissionUtil.PERMISSION[1]) ||
                PermissionUtil.isLacksOfPermission(PermissionUtil.PERMISSION[2]) ||
                PermissionUtil.isLacksOfPermission(PermissionUtil.PERMISSION[3]) ||
                PermissionUtil.isLacksOfPermission(PermissionUtil.PERMISSION[4]) ||
                PermissionUtil.isLacksOfPermission(PermissionUtil.PERMISSION[5]) ||
                PermissionUtil.isLacksOfPermission(PermissionUtil.PERMISSION[6])
                ) {
            ActivityCompat.requestPermissions(MainActivity.this, PermissionUtil.PERMISSION, 0x12);
        }

    }

    @Override
    protected void initData() {
        initTab();
        mapFragment = MapFragment.newInstance();
        taskFragment = TaskFragment.newInstance();
        rateFragment = RateFragment.newInstance();
        myFragment = MyFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.act_main_content, mapFragment)
                .add(R.id.act_main_content,taskFragment)
                .add(R.id.act_main_content,rateFragment)
                .add(R.id.act_main_content,myFragment)
                .hide(taskFragment)
                .hide(rateFragment)
                .hide(myFragment)
                .show(mapFragment)
                .commit();
    }

    /**
     * 初始化tab栏
     */
    private void initTab(){
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mTab.setTabData(mTabEntities);

        mTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position){
                    case 0:
                        getSupportFragmentManager().beginTransaction()
                                .hide(taskFragment)
                                .hide(rateFragment)
                                .hide(myFragment)
                                .show(mapFragment)
                                .commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction()
                                .show(taskFragment)
                                .hide(rateFragment)
                                .hide(myFragment)
                                .hide(mapFragment)
                                .commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction()
                                .hide(taskFragment)
                                .show(rateFragment)
                                .hide(myFragment)
                                .hide(mapFragment)
                                .commit();
                        break;
                    case 3:
                        getSupportFragmentManager().beginTransaction()
                                .hide(taskFragment)
                                .hide(rateFragment)
                                .show(myFragment)
                                .hide(mapFragment)
                                .commit();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            myToast.toast(getApplicationContext(), "再按一次退出程序！");
            exitTime = System.currentTimeMillis();
        } else {
            ExitUtils.getInstance().exit();
        }
    }
}
