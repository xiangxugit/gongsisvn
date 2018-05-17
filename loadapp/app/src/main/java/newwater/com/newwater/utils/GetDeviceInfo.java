package newwater.com.newwater.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import newwater.com.newwater.App;

/**
 * Created by Administrator on 2018/5/7 0007.
 */

public class GetDeviceInfo extends App{

    //获取经纬度
    private int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    String str = "";

    public String getIMEI(Activity context) {
        int osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        if (osVersion > 22) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_PHONE_STATE},
                        WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            } else {
                TelephonyManager tm = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
                str = tm.getDeviceId();
                String mtype = android.os.Build.MODEL;
                Log.d("Main",mtype);
            }
        } else {
            //如果SDK小于6.0则不去动态申请权限
            getImei(context);
        }

        return "";

    }



    public static String getlongitudelatitude(Activity context){

        return "";

    }


//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
//
//            Toast.makeText(getApplicationContext(),"授权成功",Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(getApplicationContext(),"授权拒绝",Toast.LENGTH_SHORT).show();
//        }
//    }



    public void getImei(Activity context){
//        TelephonyManager tm = (TelephonyManager)this.getSystemService(this.TELEPHONY_SERVICE);
//        IMEI = tm.getDeviceId();
//        String mtype = android.os.Build.MODEL;
//        Log.d("Main",mtype);
//        Toast.makeText(this,"IMEI的值为："+mtype,Toast.LENGTH_SHORT).show();
    }


}





    //isOK

