package newwater.com.newwater;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import newwater.com.newwater.beans.AdvsVideo;
import newwater.com.newwater.beans.Strategy;
import newwater.com.newwater.beans.SysDeviceMonitorConfig;

/**
 * Created by Administrator on 2018/4/27 0027.
 */

public class TestJSON {

    public static Strategy strategy;

    public static String strategy() {

        //下发的策略一
        ArrayList<Strategy> stragegyList = new ArrayList<Strategy>();
        strategy = new Strategy();
        ArrayList<String> VideList = new ArrayList<String>();
        VideList.add("http://mirror.aarnet.edu.au/pub/TED-talks/911Mothers_2010W-480p.mp4");
        VideList.add("http://mirror.aarnet.edu.au/pub/TED-talks/VilayanurRamachandran_2009I.mp4");


        //封装播放时段
        String videoplaytime = "140000190000";
        strategy.setVideoList(VideList);
        strategy.setPlayPeriod(videoplaytime);
        stragegyList.add(strategy);

        //下发的策略二
        ArrayList<String> VideList2 = new ArrayList<String>();
        Strategy strategy2 = new Strategy();
        VideList2.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        VideList2.add("http://flv2.bn.netease.com/videolib3/1604/28/fVobI0704/SD/fVobI0704-mobile.mp4");

        //封装播放时段
        String videoplaytime2 = "190000200000";
        strategy2.setVideoList(VideList2);
        strategy2.setPlayPeriod(videoplaytime2);
        stragegyList.add(strategy2);


        //下发的策略三
        ArrayList<String> VideList3 = new ArrayList<String>();
        Strategy strategy3 = new Strategy();
        VideList3.add("http://mirror.aarnet.edu.au/pub/TED-talks/MalteSpitz_2012G.mp4");

        //封装播放时段
        String videoplaytime3 = "050000070000";
        strategy3.setVideoList(VideList3);
        strategy3.setPlayPeriod(videoplaytime3);
        stragegyList.add(strategy3);


        String jsonString = JSON.toJSONString(stragegyList);
        return jsonString;
    }


    //集中模式

    /**
     * @return 0 售水模式
     * 0 售水模式
     * 1 零售模式
     * 2 租赁模式
     */
    public static int getModel() {

        return 0;
    }


    public static String getWeiXinQcode() {
        return "erweimacode";
    }

    public static String getSysDeviceMonitorConfig() {
        SysDeviceMonitorConfig deviceInitParams = new SysDeviceMonitorConfig();
        deviceInitParams.setDeviceId(123456);
        deviceInitParams.setMotCfgNetworkTime(30);//巡检时间
        deviceInitParams.setMotCfgNetworkTimes(3);//巡检次数
        deviceInitParams.setMotCfgPpTime(100);//pp棉使用时间100天
        deviceInitParams.setMotCfgPpFlow(5680);//pp棉使用流量
//        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        String date = sDateFormat.format(new java.util.Date());
        deviceInitParams.setMotCfgPpChangeTime(new Date());
        deviceInitParams.setMotCfgGrainCarbonTime(100);//活性炭使用时间
        deviceInitParams.setMotCfgGrainCarbonFlow(11355);//活性炭使用流量
        deviceInitParams.setMotCfgGrainCarbonChangeTime(new Date());
        deviceInitParams.setMotCfgGrainCarbonFlow(11355);
        deviceInitParams.setMotCfgPressCarbonChangeTime(new Date());
        deviceInitParams.setMotCfgPressCarbonTime(100);
        deviceInitParams.setMotCfgPoseCarbonChangeTime(new Date());
        deviceInitParams.setMotCfgPoseCarbonFlow(11355);
        deviceInitParams.setMotCfgPoseCarbonTime(100);
        deviceInitParams.setMotCfgRoTime(540);
        deviceInitParams.setMotCfgRoFlow(11355);
        deviceInitParams.setMotCfgRoChangeTime(new Date());
        deviceInitParams.setMotCfgUpTime(30);
        deviceInitParams.setMotCfgMaxFlow(30);//最大出水量30Ml
//        deviceInitParams.setMot_cfg_
//        mot_cfg_up_time
//                mot_cfg_max_flow

//        com.alibaba.fastjson.JSONObject deviceinitString = com.alibaba.fastjson.JSONObject.toJSON(deviceInitParams);
//        String  devicestring = com.alibaba.fastjson.JSONObject.
        return "";
    }


    public static String getDeviceid() {
        return "1";
    }

    public static String getUserid() {
        return "1";
    }

    public static String getSaletYpe() {
        return "1";
    }

    public static String getCupNum() {
        return "2";
    }
}
