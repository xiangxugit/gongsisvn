package newwater.com.newwater.Processpreserving;

import android.serialport.DevUtil;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/5/22 0022.
 */

public class DeviceLog {
    private static ComThread comThread=null;
    private static DevUtil devUtil=null;
    public static String updateRunData(boolean poll) {
        if(poll && comThread==null)
//            Log.e("不会吧","不会是空吧");
            return "进程没通信啊";

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

//        for (int i = 0; i < data.length;i++) {

            Log.e("data",""+data.toString());
//        }
      return data.toString();

    }

}
