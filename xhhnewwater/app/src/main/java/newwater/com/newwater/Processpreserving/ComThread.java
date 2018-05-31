package newwater.com.newwater.Processpreserving;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.serialport.ComUtil;
import android.serialport.DevUtil;
import android.util.Log;

import org.xutils.ex.DbException;

import java.util.HashMap;
import java.util.List;

import newwater.com.newwater.DataBaseUtils.Sys_Device_Monitor_Config_DbOperate;
import newwater.com.newwater.MainActivity;
import newwater.com.newwater.beans.Sys_Device_Monitor_Config;
import newwater.com.newwater.beans.ViewShow;
import newwater.com.newwater.utils.GetDeviceInfo;

/**
 * Created by Administrator on 2018/5/22 0022.
 */

public   class ComThread extends Thread {

    private final int LoopIdle = 50;//线程空闲时间ms
    private DevUtil devUtil=null;
    private final int MAXERR=5;
    private int errCount=0;
    private final int PollTime = 800;//轮询get_ioRunData()时间间隔ms
    private boolean active = true;//轮询标志
    public Context context;
    public Handler myhandler;
    public boolean getActive() {
        return active;
    }
    public void setActive(boolean b) {
        active = b;
    }
    public ComThread(Context context, Handler handler){
        this.context = context;
        this.myhandler = handler;
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


       //更新操作
        Sys_Device_Monitor_Config_DbOperate sys_device_monitor_config_dbOperate = new Sys_Device_Monitor_Config_DbOperate(context);

        try {
            List warningdata = sys_device_monitor_config_dbOperate.find();
            Sys_Device_Monitor_Config user = (Sys_Device_Monitor_Config)warningdata.get(0);
//            user.setDevice_id();

            Log.e("waringdata",warningdata.toString());
        } catch (DbException e) {
            e.printStackTrace();
        }

//        isNetworkConnected

//        mot_cfg_ network_time  检测网络状态，如果断网了，就存储
//                //如果断网轮询的断线重连 mot_cfg_ network_times次
//
//
//        mot_cfg_ pp_time 获取pp棉使用时间
//
//        mot_cfg_ pp_flow 获取pp棉制水总流量
//        mot_cfg_ pp_change_time 获取pp棉更换时间
//
//                活性炭的操作
//
//                压缩活性炭操作
//
//                        后置活性炭的操作
//
//
//        RO反渗透的操作
//
//
//        监控数据上报时间间隔（单位分钟）(激活时间开始)
//
//                备单次最大出水量（单位ml)
    }
}
