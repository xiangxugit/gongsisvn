package newwater.com.newwater.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

import newwater.com.newwater.MainActivity;

/**
 * Created by Administrator on 2018/5/31 0031.
 */

public class ToKenUtils {
    private static Context activity;
    private static String accessToken;
    public ToKenUtils(Context activity){
        this.activity = activity;
    }
    public static String getToken(){

        String urltest = RestUtils.getUrl(RestUtils.NEWAPK);


        XutilsHttp.getInstance().get(true,activity,urltest, null, new XCallBack() {
            @Override
            public void onResponse(String result) {
                // 成功获取数据

            }

            @Override
            public void onFail(String result) {
                Log.e("获取数据失败",""+result);
                String url = RestUtils.getUrl(RestUtils.GETTOKEN);
                Map params = new HashMap<String,String>();
                params.put("loginName","1");
                params.put("loginPassword","2");
                XutilsHttp.getInstance().get(false,activity,url, params, new XCallBack() {
                    @Override
                    public void onResponse(String result) {
                        // 成功获取数据

                        com.alibaba.fastjson.JSONObject getdata = JSON.parseObject(result);
                        String data = getdata.getString("data");
                        com.alibaba.fastjson.JSONObject dataobj = JSON.parseObject(data);
                        accessToken = dataobj.getString("accessToken");
                        //把token存入缓存
                        BaseSharedPreferences.setString(activity,BaseSharedPreferences.TOKEN,accessToken);
                        Toast.makeText(activity,""+result,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(String result) {
                        Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        return BaseSharedPreferences.getString(activity,BaseSharedPreferences.TOKEN);
    }

}
