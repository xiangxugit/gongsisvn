package newwater.com.newwater.Processpreserving;

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

import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import newwater.com.newwater.DataBaseUtils.Sys_Device_Monitor_Config_DbOperate;
import newwater.com.newwater.DataBaseUtils.XutilsInit;
import newwater.com.newwater.FreeAdActivity;
import newwater.com.newwater.TestJSON;
import newwater.com.newwater.beans.SysDeviceNoticeAO;
import newwater.com.newwater.beans.Sys_Device_Monitor_Config;
import newwater.com.newwater.beans.ViewShow;
import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.constants.UriConstant;
import newwater.com.newwater.utils.OkHttpUtils;
import newwater.com.newwater.utils.RestUtils;
import newwater.com.newwater.utils.TimeUtils;
import newwater.com.newwater.utils.UploadLocalData;
import okhttp3.Request;

/**
 * Created by Administrator on 2018/5/22 0022.
 */

public   class ComThread extends Thread {
    private SysDeviceNoticeAO sysDeviceNoticeAO;
    private final int LoopIdle = 50;//线程空闲时间ms
    private DevUtil devUtil=null;
    private final int MAXERR=5;
    private int errCount=0;
    private final int PollTime = 800;//轮询get_ioRunData()时间间隔ms
    private boolean active = true;//轮询标志
    public Context context;
    public Handler myhandler;
    public boolean updateflag = false;
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
        super.run();

        long pollTick = SystemClock.uptimeMillis();
        long nowTick;

        while (!isInterrupted()){
            nowTick = SystemClock.uptimeMillis();
            if(null==devUtil){
                devUtil = new DevUtil(null);
            }
            if(active && nowTick- pollTick > PollTime) {
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
                pollTick = SystemClock.uptimeMillis();                    ;
            }
            else {
                //线程空闲
                ComUtil.delay(LoopIdle);
            }
        }
    }



    public void updateRunData(boolean poll) {
//        if(poll)
//            return;

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
        viewShow.setPpmvalue("未知");
        viewShow.setPpm(data[2][1]);
        viewShow.setZhishuitext(data[8][1]);
//        b.putSerializable("viewShowa", viewShow);
//        msg.setData(b);
        msg.obj = viewShow;
        msg.what =0;
        myhandler.sendMessage(msg);
        //TODO  https://www.jianshu.com/p/7d3ff0a11ab8

        String deviceNoticeUrl = RestUtils.getUrl(UriConstant.NOTICEQUALITY);
        String postdata = "";
        sysDeviceNoticeAO.setDeviceId(Integer.parseInt(TestJSON.getDeviceid()));
        if(DevUtil.ERR_TIMEOUT==1){
            sysDeviceNoticeAO.setDeviceNoticeType(1);
            updateflag = true;
        }
        //滤芯过了
        boolean filterflag = filterOver();
        if(false == filterflag){
            sysDeviceNoticeAO.setDeviceNoticeType(2);
            updateflag = true;
        }
        //滤芯是否用完
        boolean filterend = filterend();
        if(false == filterend){
            sysDeviceNoticeAO.setDeviceNoticeType(3);
            updateflag = true;

        }
        //水质是否异常
         if(Constant.TDSERROR>devUtil.get_run_oTDS_value()){
            sysDeviceNoticeAO.setDeviceNoticeType(4);
             updateflag = true;

         }
         //纸杯不足
        if(devUtil.get_run_bCup_value()==2){
             sysDeviceNoticeAO.setDeviceNoticeType(5);
            updateflag = true;

        }
        //耗水量异常
        //漏电
        if(devUtil.get_run_bLeak_value()==02){
            sysDeviceNoticeAO.setDeviceNoticeType(9);
            updateflag = true;

        }

         //原水缺水
        if(devUtil.get_run_bLeak_value()==02){
            sysDeviceNoticeAO.setDeviceNoticeType(10);
            updateflag = true;

        }

        //警告的类型
        sysDeviceNoticeAO.setDeviceNoticeLeve(0);

        sysDeviceNoticeAO.setDeviceNoticeSubject("");
        sysDeviceNoticeAO.setDeviceNoticeContent("");

        String warningtime = TimeUtils.getCurrentTime();
        sysDeviceNoticeAO.setDeviceNoticeTime(warningtime);
        postdata = JSON.toJSONString(sysDeviceNoticeAO,true);

        //TODO 存入数据库 定时上报
        try {
            dbManager.save(sysDeviceNoticeAO);
        } catch (DbException e) {
            e.printStackTrace();
        }
        updateflag = true;
        if(updateflag == true){
            OkHttpUtils.postAsyn(deviceNoticeUrl, new OkHttpUtils.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Toast.makeText(context,"成功",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response) {Toast.makeText(context,"成功",Toast.LENGTH_SHORT).show();
                }
            },postdata);
        }else{
            Log.e("不需要上传","不需要上传");
        }



    }

    public boolean filterOver() {
        Boolean filterOverflag = true;
        Sys_Device_Monitor_Config_DbOperate sys_device_monitor_config_dbOperate = new Sys_Device_Monitor_Config_DbOperate(context);
        String[][] data=devUtil.toArray();
        try {
            List warningdata = sys_device_monitor_config_dbOperate.find();
            Sys_Device_Monitor_Config monitor = (Sys_Device_Monitor_Config) warningdata.get(0);
            if (monitor.getMot_cfg_grain_carbon_flow() - Integer.parseInt(data[17][1]) < Constant.MOT_CFG_GRAIN_CARBON_FLOW) {
                monitor.setMot_cfg_grain_carbon_flow(monitor.getMot_cfg_grain_carbon_flow() - Integer.parseInt(data[17][1]));
                sys_device_monitor_config_dbOperate.update(monitor);
                //TODO 准备上传
                filterOverflag = false;
            }

            if (monitor.getMot_cfg_pose_carbon_flow() - Integer.parseInt(data[17][1]) < Constant.MOT_CFG_POSE_CARBON_FLOW) {
                monitor.setMot_cfg_pose_carbon_flow(monitor.getMot_cfg_pose_carbon_flow() - Integer.parseInt(data[17][1]));
                sys_device_monitor_config_dbOperate.update(monitor);
                //TODO 准备上传
                filterOverflag = false;
            }

            if (monitor.getMot_cfg_press_carbon_flow() - Integer.parseInt(data[17][1]) < Constant.MOT_CFG_PRESS_CARBON_FLOW) {
                monitor.setMot_cfg_press_carbon_flow(monitor.getMot_cfg_press_carbon_flow() - Integer.parseInt(data[17][1]));
                sys_device_monitor_config_dbOperate.update(monitor);
                //TODO 准备上传
                filterOverflag = false;
            }

            if (monitor.getMot_cfg_pp_flow() - Integer.parseInt(data[17][1]) < Constant.MOT_CFG_PP_FLOW) {
                monitor.setMot_cfg_pp_flow(monitor.getMot_cfg_pp_flow() - Integer.parseInt(data[17][1]));
                sys_device_monitor_config_dbOperate.update(monitor);
                //TODO 准备上传
                filterOverflag = false;
            }


            if (monitor.getMot_cfg_ro_flow() - Integer.parseInt(data[17][1]) < Constant.MOT_CFG_RO_FLOW) {
                monitor.setMot_cfg_pp_flow(monitor.getMot_cfg_ro_flow() - Integer.parseInt(data[17][1]));
                sys_device_monitor_config_dbOperate.update(monitor);
                //TODO 准备上传
                filterOverflag = false;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return  filterOverflag;
    }


    //滤芯是否用完


    public boolean filterend() {
        Boolean filterOverflag = true;
        Sys_Device_Monitor_Config_DbOperate sys_device_monitor_config_dbOperate = new Sys_Device_Monitor_Config_DbOperate(context);
        String[][] data=devUtil.toArray();
        try {
            List warningdata = sys_device_monitor_config_dbOperate.find();
            Sys_Device_Monitor_Config monitor = (Sys_Device_Monitor_Config) warningdata.get(0);
            if (monitor.getMot_cfg_grain_carbon_flow() - Integer.parseInt(data[17][1]) <= 0) {
                monitor.setMot_cfg_grain_carbon_flow(monitor.getMot_cfg_grain_carbon_flow() - Integer.parseInt(data[17][1]));
                sys_device_monitor_config_dbOperate.update(monitor);
                //TODO 准备上传
                filterOverflag = false;
            }

            if (monitor.getMot_cfg_pose_carbon_flow() - Integer.parseInt(data[17][1]) <= 0) {
                monitor.setMot_cfg_pose_carbon_flow(monitor.getMot_cfg_pose_carbon_flow() - Integer.parseInt(data[17][1]));
                sys_device_monitor_config_dbOperate.update(monitor);
                //TODO 准备上传
                filterOverflag = false;
            }

            if (monitor.getMot_cfg_press_carbon_flow() - Integer.parseInt(data[17][1]) <= 0) {
                monitor.setMot_cfg_press_carbon_flow(monitor.getMot_cfg_press_carbon_flow() - Integer.parseInt(data[17][1]));
                sys_device_monitor_config_dbOperate.update(monitor);
                //TODO 准备上传
                filterOverflag = false;
            }

            if (monitor.getMot_cfg_pp_flow() - Integer.parseInt(data[17][1]) <= 0) {
                monitor.setMot_cfg_pp_flow(monitor.getMot_cfg_pp_flow() - Integer.parseInt(data[17][1]));
                sys_device_monitor_config_dbOperate.update(monitor);
                //TODO 准备上传
                filterOverflag = false;
            }


            if (monitor.getMot_cfg_ro_flow() - Integer.parseInt(data[17][1]) <= 0) {
                monitor.setMot_cfg_pp_flow(monitor.getMot_cfg_ro_flow() - Integer.parseInt(data[17][1]));
                sys_device_monitor_config_dbOperate.update(monitor);
                //TODO 准备上传
                filterOverflag = false;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return  filterOverflag;
    }

}
