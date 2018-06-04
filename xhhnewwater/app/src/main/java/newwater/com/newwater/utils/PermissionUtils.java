package newwater.com.newwater.utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import newwater.com.newwater.App;

/**
 * Created by Administrator on 2018/05/30.
 */

public class PermissionUtils extends App {

    private static final String TAG = PermissionUtils.class.getSimpleName();
    public static final int CODE_INTERNET = 1;
    public static final int CODE_READ_PHONE_STATE = 2;
    public static final int CODE_ACCESS_NETWORK_STATE = 3;
    public static final int CODE_WRITE_EXTERNAL_STORAGE = 4;
    public static final int CODE_MOUNT_UNMOUNT_FILESYSTEMS = 5;
    public static final int CODE_MULTI_PERMISSION = 100;

    public static final String PERMISSION_INTERNET = Manifest.permission.INTERNET;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_ACCESS_NETWORK_STATE = Manifest.permission.ACCESS_NETWORK_STATE;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String PERMISSION_MOUNT_UNMOUNT_FILESYSTEMS = Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS;

    public static final String[] permissions = {
            PERMISSION_INTERNET,
            PERMISSION_READ_PHONE_STATE,
            PERMISSION_ACCESS_NETWORK_STATE,
            PERMISSION_WRITE_EXTERNAL_STORAGE,
            PERMISSION_MOUNT_UNMOUNT_FILESYSTEMS
    };
}
