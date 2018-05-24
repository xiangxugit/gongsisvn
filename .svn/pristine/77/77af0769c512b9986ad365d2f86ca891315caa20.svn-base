package newwater.com.newwater.Processpreserving;

import android.serialport.DevUtil;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/5/22 0022.
 */

public class DeviceLog {
    public static ComThread comThread = DaemonService.comThread;
    public static DevUtil devUtil = null;
    private static ArrayList<HashMap<String, Object>> mItemList = new ArrayList<HashMap<String, Object>>();;

    public static String updateRunData(boolean poll) {
        if (poll && DaemonService.comThread == null)
//            Log.e("不会吧","不会是空吧");
            return "进程没通信啊";

        HashMap<String, Object> map = new HashMap<>();
        if(null==devUtil){
            devUtil = new DevUtil(null);
        }
        String[][] data = devUtil.toArray();
        String sOn, sOff;
        int sta = devUtil.get_run_bSta_value();
        if (sta == 1) {
            sOn = "Online";
            sOff = "";
        } else {
            sOn = "";
            sOff = "Offline";
        }

//        for (int i = 0; i < data.length; i++) {
//            map = mItemList.get(i);
//            if (!map.get("ItemValue").toString().equals(data[i][1])) {
//                map.put("ItemValue", data[i][1]);
//            }
//            if (i > 1) {
//                if (!map.get("ItemOnline").toString().equals(sOn))
//                    map.put("ItemOnline", sOn);
//                if (!map.get("ItemOffline").toString().equals(sOff))
//                    map.put("ItemOffline", sOff);
//            }
//
//
//
//        }


        Log.e("alldata", "alldata" + mItemList.toString());
        return mItemList.toString();

    }
}
