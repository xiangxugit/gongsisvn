package newwater.com.newwater.Processpreserving;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.serialport.ComUtil;
import android.serialport.DevUtil;
import android.util.Log;

import java.util.HashMap;

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
    public ComThread(Context context,Handler handler){
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
        msg.what =0;
        myhandler.sendMessage(msg);
//        for (int i = 0; i < data.length;i++) {
//            map = mItemList.get(i);
//            if(!map.get("ItemValue").toString().equals( data[i][1])){
//                map.put("ItemValue", data[i][1]);
//            }
//            if(i>1){
//                if (!map.get("ItemOnline").toString().equals(sOn))
//                    map.put("ItemOnline", sOn);
//                if (!map.get("ItemOffline").toString().equals(sOff))
//                    map.put("ItemOffline", sOff);
//            }
//
//        }


    }
}
