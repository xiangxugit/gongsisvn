package newwater.com.newwater;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import newwater.com.newwater.beans.DeviceInitParams;
import newwater.com.newwater.beans.Strategy;
import newwater.com.newwater.beans.Sys_Device_Monitor_Config;

/**
 * Created by Administrator on 2018/4/27 0027.
 */

public class TestJSON {

    public static Strategy strategy;

    public static String strategy() {

        ArrayList<Strategy> stragegyList = new ArrayList<Strategy>();
        strategy = new Strategy();
        ArrayList<String> VideList = new ArrayList<String>();
        VideList.add("http://mirror.aarnet.edu.au/pub/TED-talks/911Mothers_2010W-480p.mp4");
        VideList.add("http://mirror.aarnet.edu.au/pub/TED-talks/VilayanurRamachandran_2009I.mp4");


        //封装播放时段
        String videoplaytime = "0000000017000000000000190000";
        strategy.setVideoList(VideList);
        strategy.setVideoplayTime(videoplaytime);
        stragegyList.add(strategy);

        //下发的策略二
        ArrayList<String> VideList2 = new ArrayList<String>();
        Strategy strategy2 = new Strategy();
        VideList2.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        VideList2.add("http://flv2.bn.netease.com/videolib3/1604/28/fVobI0704/SD/fVobI0704-mobile.mp4");

        //封装播放时段
        String videoplaytime2 = "0000000019000000000000200000";
        strategy2.setVideoList(VideList2);
        strategy2.setVideoplayTime(videoplaytime2);
        stragegyList.add(strategy2);


        //下发的策略三
        ArrayList<String> VideList3 = new ArrayList<String>();
        Strategy strategy3 = new Strategy();
        VideList3.add("http://mirror.aarnet.edu.au/pub/TED-talks/MalteSpitz_2012G.mp4");

        //封装播放时段
        String videoplaytime3 = "0000000005000000000000070000";
        strategy3.setVideoList(VideList3);
        strategy3.setVideoplayTime(videoplaytime3);
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

    public static String getSys_device_monitor_config() {
        Sys_Device_Monitor_Config deviceInitParams = new Sys_Device_Monitor_Config();
        deviceInitParams.setDevice_id(123456);
        deviceInitParams.setMot_cfg_network_time(30);
        deviceInitParams.setMot_cfg_network_times(3);//巡检次数
        deviceInitParams.setMot_cfg_pp_time(100);//pp棉使用时间100天
        deviceInitParams.setMot_cfg_pp_flow(5680);//pp棉使用流量
//        SimpleDateFormat    sDateFormat    =   new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
//        String    date    =    sDateFormat.format(new    java.util.Date());
        deviceInitParams.setMot_cfg_pp_change_time(new Date());
        deviceInitParams.setMot_cfg_grain_carbon_time(100);//活性炭使用时间
        deviceInitParams.setMot_cfg_grain_carbon_flow(11355);//活性炭使用流量
        deviceInitParams.setMot_cfg_grain_carbon_change_time(new Date());
        deviceInitParams.setMot_cfg_press_carbon_change_time(0);
        deviceInitParams.setMot_cfg_press_carbon_time(100);
        deviceInitParams.setMot_cfg_grain_carbon_flow(11355);
        deviceInitParams.setMot_cfg_pose_carbon_change_time(0);
        deviceInitParams.setMot_cfg_pose_carbon_flow(11355);
        deviceInitParams.setMot_cfg_pose_carbon_time(100);
        deviceInitParams.setMot_cfg_ro_time(540);
        deviceInitParams.setMot_cfg_ro_flow(11355);
        deviceInitParams.setMot_cfg_ro_change_time(0);
        deviceInitParams.setMot_cfg_up_time(30);
        deviceInitParams.setMot_cfg_max_flow(30);//最大出水量30Ml
//        deviceInitParams.setMot_cfg_
//        mot_cfg_up_time
//                mot_cfg_max_flow

//        com.alibaba.fastjson.JSONObject deviceinitString = com.alibaba.fastjson.JSONObject.toJSON(deviceInitParams);
//        String  devicestring = com.alibaba.fastjson.JSONObject.
        return "";
    }
}
