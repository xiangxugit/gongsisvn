package newwater.com.newwater.processpreserve;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.serialport.ComUtil;
import android.serialport.DevUtil;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import newwater.com.newwater.Sys_Device_Monitor_Config_DbOperate;
import newwater.com.newwater.beans.SysDeviceWaterQualityAO;
import newwater.com.newwater.utils.UploadLocalData;
import newwater.com.newwater.utils.XutilsInit;
import newwater.com.newwater.TestJSON;
import newwater.com.newwater.beans.SysDeviceNoticeAO;

import newwater.com.newwater.beans.ViewShow;
import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.constants.UriConstant;
import newwater.com.newwater.utils.ControllerUtils;
import newwater.com.newwater.utils.OkHttpUtils;
import newwater.com.newwater.utils.RestUtils;
import newwater.com.newwater.utils.TimeUtils;
import okhttp3.Request;

/**
 * Created by Administrator on 2018/5/22 0022.
 */
public class ComThread extends Thread {
    private SysDeviceNoticeAO sysDeviceNoticeAO;
    private final int LoopIdle = 50;//线程空闲时间ms
    private DevUtil devUtil=null;
    private final int MAXERR=5;
    private int errCount=0;
    private boolean active = true;//轮询标志
    public Context context;
    public Handler myhandler;
    public boolean updateflag = false;
    public boolean operateflag = false;
    private DbManager dbManager;
    public boolean getActive() {
        return active;
    }
    public void setActive(boolean b) {
        active = b;
    }

    public ComThread(Context context, Handler handler){
        this.context = context;
        this.myhandler = handler;
        if(null == this.sysDeviceNoticeAO){
            this.sysDeviceNoticeAO = new SysDeviceNoticeAO();
        }
        dbManager = new XutilsInit(context).getDb();
    }

    @Override
    public void run() {
        long pollTick = SystemClock.uptimeMillis();
        long nowTick;

        while (!isInterrupted()){
            nowTick = SystemClock.uptimeMillis();
            if(null==devUtil){
                devUtil = new DevUtil(null);
            }
            if(active && nowTick- pollTick > Constant.POOL_TIME) {
                try {
                    devUtil.get_ioRunData();
//                    String datajiankong =  DeviceLog.updateRunData(true);
                    updateRunData(true);
                    Log.e("datajiankong","datajiankong");
                } catch (NullPointerException e) {
//                    addCode(false, ComUtil.getCodeHead() + e.toString());
                    ComUtil.delay(2000);
                }
                pollTick = SystemClock.uptimeMillis();
            }
            else if(!active){
                //停止轮询，可能主线程在调用devUtil发送指令
                pollTick = SystemClock.uptimeMillis();
            }
            else {
                //线程空闲
                ComUtil.delay(LoopIdle);
            }
        }




//        try {
//            //TODO 获取值
//            List<SysDeviceWaterQualityAO> listQualityAO = dbManager.findAll(SysDeviceWaterQualityAO.class);
//            if(null==listQualityAO){
//
//            }else{
//                UploadLocalData.getInstance(context,waterqualitylist,listQualityAO,Constant.UPLOAD_TIME).upload();
//            }
//
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
        //启动售水上报

        //预警定时上报
//        String noticequalitylist = RestUtils.getUrl(UriConstant.NOTICEQUALITY);
//        try {
//            //TODO 获取值
//            List<SysDeviceNoticeAO> sysDeviceNoticeAOList = dbManager.findAll(SysDeviceNoticeAO.class);
//            if(null==sysDeviceNoticeAOList){
//
//            }else{
//                UploadLocalData.getInstance(context,noticequalitylist,sysDeviceNoticeAOList, Constant.UPLOAD_TIME).upload();
//            }
//
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
        super.run();
    }

    public void updateRunData(boolean poll) {
        HashMap<String, Object> map;
        String[][] data=devUtil.toArray();
        String sOn, sOff;
        int sta = devUtil.get_run_bSta_value();
        if(sta==1) {
            sOn = "Online";
            sOff = "";
        }
        else{
            sOn = "";
            sOff = "Offline";
        }
        Message msg = new Message();
        Bundle b = new Bundle();
        ViewShow viewShow = new ViewShow();
        viewShow.setChongxitext(data[9][1]);
        viewShow.setCooltext(data[7][1]);//是否制冷
        viewShow.setCoolwatertextvalue(data[5][1]);//
        viewShow.setHotornot(data[6][0]);//是否加热
        viewShow.setHotwatertextvalue(data[4][1]);
        viewShow.setPpmvalue(""+devUtil.get_run_sTDS_value());
        viewShow.setPpm(data[2][1]);
        viewShow.setZhishuitext(data[8][1]);
        msg.obj = viewShow;
        msg.what =0;
        myhandler.sendMessage(msg);
    }

    //关机
    public void onOrOff(boolean operateflag){
        if(true == operateflag){
            ControllerUtils.operateDevice(3,true);
        }
    }
    //滤芯是否用完




}
