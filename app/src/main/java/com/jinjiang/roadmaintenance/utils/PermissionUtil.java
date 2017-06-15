package com.jinjiang.roadmaintenance.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.jinjiang.roadmaintenance.base.ClientApp;

/**
 * Created by Administrator on 2016/11/30.
 */
public class PermissionUtil {

    public static String[] PERMISSION = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
    };

    public static boolean isLacksOfPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(
                    ClientApp.getInstance().getApplicationContext(), permission) == PackageManager.PERMISSION_DENIED;
        }
        return false;
    }
}
