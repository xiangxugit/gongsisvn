package newwater.com.newwater.utils;

import android.app.Activity;
import android.content.Context;
import android.serialport.DevUtil;
import android.util.Log;
import android.widget.Toast;

import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.processpreserve.ComThread;
import newwater.com.newwater.processpreserve.DaemonService;

/**
 * Created by Administrator on 2018/4/19 0019.
 */

public class ControllerUtils {
    public static int SET_IOHEATENABLED = 0;//加热使能
    public static int SET_IOCOLDEMABLED = 1;//制冷使能
    public static int DO_IOSWITCH = 3;//开关机指令
    public static int DO_IOEMPTYING = 4;//排空指令
    public static int DO_IOCOVER = 5;//做开盖指令
    public static int DO_IORINSE = 6;

//    使能操作
    public static  DevUtil devUtil = null;
    public static  ComThread comThread;
    public static int DO_HOTTING = 7;//允许加热
    public static int DO_TURNOFFHOTTING = 8;//禁止加热
    public static int DO_COOLING = 9;//允许制冷
    public static int DO_TURNOFFCOOLING = 10;//禁止制冷

    public static int SETTING = 11;//设置
    public static int SHUTDOWN = 12;//关机
    public static int OPENING = 13;//开机；
    private  static Context context;

    public ControllerUtils(Context context){
        this.context = context;
        if(null==devUtil){
            devUtil = new DevUtil(null);
        }
        if(null== DaemonService.comThread){
            comThread = new ComThread(context,null);
        }else {
            comThread  = DaemonService.comThread;
        }
    }
    public static void operateDevice(int directive, boolean flag){
        comThread.setActive(false);
        try {
            if(directive==SET_IOHEATENABLED){
                //加热
                Toast.makeText(context,"加热使能",Toast.LENGTH_SHORT).show();

                devUtil.set_ioHeatEnabled(flag);
            }
                //制冷使能
            if(directive == SET_IOCOLDEMABLED){
                Toast.makeText(context,"制冷使能",Toast.LENGTH_SHORT).show();
                devUtil.set_ioColdEnabled(flag);
            }
            //开关机
            if(directive == DO_IOSWITCH){
                devUtil.do_ioSWitch(flag);
            }
            //排空
            if(directive==DO_IOEMPTYING){
                devUtil.do_ioEmptying();
            }
            //开盖
            if(directive == DO_IOCOVER){
                devUtil.do_ioCover();
            }
            if(directive == DO_IORINSE){
                devUtil.do_ioCover();
            }

            if(directive == DO_HOTTING){
                devUtil.set_ioHeatEnabled(true);
            }

            if(directive == DO_TURNOFFHOTTING){
                devUtil.set_ioHeatEnabled(false);
            }

            if(directive == DO_COOLING){
                devUtil.set_ioColdEnabled(true);
            }

            if(directive == DO_TURNOFFCOOLING){
                devUtil.set_ioColdEnabled(false);
            }

            if(directive == SETTING){
//                devUtil.set_ioColdEnabled(true);
               /* rIntv：冲洗间隔
                rLong：冲洗时长
                hTemp：加热温度
                cTemp：制冷温度*/

//                int rIntv = 75;
                int rIntv = BaseSharedPreferences.getInt(context, Constant.DEVICE_FLUSH_INTERVAL_KEY);
//                int rLong = 20;
                int rLong = BaseSharedPreferences.getInt(context,Constant.DEVICE_FLUSH_DURATION_KEY);
//                int  hTemp = 30;
                int hTemp = BaseSharedPreferences.getInt(context,Constant.DEVICE_HEATING_TEMP_KEY);
//                int cTemp = 10;
                int cTemp = BaseSharedPreferences.getInt(context,Constant.DEVICE_COOLING_TEMP_KEY);
                if (devUtil.set_ioParam(rIntv, rLong, hTemp, cTemp) == 0) {
                    Log.e("设置参数成功","设置参数成功");
                }else{
                    Log.e("设置参数失败","设置参数失败");
                }
            }


            if(directive == OPENING){
                devUtil.set_ioColdEnabled(true);
            }
        }catch (Exception e){
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
        finally {
            comThread.setActive(true);
        }
    }





}
