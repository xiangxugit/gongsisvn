package purewater.com.leadapp.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import purewater.com.leadapp.App;
import purewater.com.leadapp.MainActivity;

/**
 * 全局自动刷新Token的拦截器
 */

public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        Log.i("Interceptor", "response.code=" + response.code());

        if (isTokenExpired(response)) {//根据和服务端的约定判断token过期
            Log.i("Interceptor", "静默自动刷新Token，然后重新请求数据");
            //同步请求方式，获取最新的Token
            String newSession = getNewToken();
            //使用新的Token，创建新的请求
            Request newRequest = chain.request()
                    .newBuilder()
                    .header("authorization", newSession)
                    .build();
            //重新请求
            return chain.proceed(newRequest);
        }
        return response;
    }

    /**
     * 根据Response，判断Token是否失效
     *
     * @param response
     * @return
     */
    private boolean isTokenExpired(Response response) {
        if (response.code() != 200) {
            return true;
        }
        return false;
    }

    /**
     * 同步请求方式，获取最新的Token
     *
     * @return token
     */
    private String getNewToken() throws IOException {
        String accessToken = "";
        OkHttpClient client = new OkHttpClient();
        String url = RestUtils.getUrl(RestUtils.GETTOKEN);
        /*Map params = new HashMap<String, String>();
        params.put("loginName", "1");
        params.put("loginPassword", "2");*/

        //Request是OkHttp中访问的请求，Builder是辅助类，Response即OkHttp中的响应
        final Request request = new Request.Builder()
                .url(url + "?loginName=1&loginPassword=2")
                .build();
        final Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            final String jsonData = response.body().string();
            Log.i("getData", jsonData);

            try {
                JSONObject jsonArray = JSON.parseObject(jsonData);
                JSONObject jsonObject = jsonArray.getJSONObject("data");
//                String id = jsonObject.getString("id");
//                String token = jsonObject.getString("token");
                accessToken = jsonObject.getString("accessToken");
//                String expiresIn = jsonObject.getString("expiresIn");
                //把token存入缓存
//                BaseSharedPreferences.setString(App.getInstance(), BaseSharedPreferences.TOKEN, accessToken);
            } catch (Exception e) {
                Log.i("Error", e.getMessage());
                e.printStackTrace();
            }
        } else {
            throw new IOException("Unexpected code " + response);
        }

//        return BaseSharedPreferences.getString(App.getInstance(), "accessToken");
        return accessToken;
    }
}