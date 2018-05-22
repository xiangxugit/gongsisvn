package newwater.com.newwater.Processpreserving;

import android.os.SystemClock;
import android.serialport.ComUtil;
import android.serialport.DevUtil;
import android.util.Log;

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
    public boolean getActive() {
        return active;
    }
    public void setActive(boolean b) {
        active = b;
    }

    @Override
    public void run() {
        super.run();

        long pollTick = SystemClock.uptimeMillis();
        long nowTick;

        while (!isInterrupted()){
            nowTick = SystemClock.uptimeMillis();

            if(active && nowTick- pollTick > PollTime) {
                try {
                    devUtil.get_ioRunData();
                   String datajiankong =  DeviceLog.updateRunData(true);
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
}
