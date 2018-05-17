package newwater.com.newwater;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.util.ArrayList;

import newwater.com.newwater.beans.Strategy;

/**
 * Created by Administrator on 2018/4/27 0027.
 */

public class TestJSON {
     public static Strategy strategy;
     public static  String strategy(){

         ArrayList<Strategy>  stragegyList = new ArrayList<Strategy>();
         strategy = new Strategy();
         ArrayList<String> VideList = new ArrayList<String>();
         VideList.add("http://flv2.bn.netease.com/videolib3/1604/28/fVobI0704/SD/fVobI0704-mobile.mp4");
         VideList.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
         VideList.add("http://mirror.aarnet.edu.au/pub/TED-talks/911Mothers_2010W-480p.mp4");
         //封装播放时段
         String videoplaytime = "18:00--19:00";
         strategy.setVideoList(VideList);
         strategy.setVideoplayTime(videoplaytime);
         stragegyList.add(strategy);

         //下发的策略二
         ArrayList<String> VideList2 = new ArrayList<String>();
         Strategy strategy2 = new Strategy();
         VideList2.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
         VideList2.add("http://flv2.bn.netease.com/videolib3/1604/28/fVobI0704/SD/fVobI0704-mobile.mp4");
         VideList2.add("http://mirror.aarnet.edu.au/pub/TED-talks/911Mothers_2010W-480p.mp4");
         //封装播放时段
         String videoplaytime2 = "19:00--20:00";
         strategy2.setVideoList(VideList2);
         strategy2.setVideoplayTime(videoplaytime);
         stragegyList.add(strategy2);
         String jsonString2 = JSON.toJSONString(stragegyList);
         return jsonString2;
    }


    //集中模式

    /**
     *
     * @return   0 售水模式
     *  0 售水模式
       1 零售模式
       2 租赁模式
     */
    public static int getModel(){

         return 0;
    }


    public static String getWeiXinQcode(){
        return "erweimacode";
    }
}
