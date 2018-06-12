package newwater.com.newwater.utils;

import android.app.Activity;
import android.serialport.DevUtil;
import android.widget.Toast;

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
    public static  DevUtil devUtil = null;
    public ControllerUtils(){
        if(null==devUtil){
            devUtil = new DevUtil(null);
        }
    }
    public static void operateDevice(int directive, boolean flag){
            if(directive==SET_IOHEATENABLED){
                //加热
                devUtil.set_ioHeatEnabled(flag);
            }
                //制冷使能
            if(directive == SET_IOCOLDEMABLED){
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
    }





}
