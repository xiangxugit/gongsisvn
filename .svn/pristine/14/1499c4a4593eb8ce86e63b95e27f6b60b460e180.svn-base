package newwater.com.newwater.utils;

import android.util.Log;

import com.tencent.android.tpush.service.cache.CacheManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.xutils.common.util.LogUtil;

import java.io.IOException;

import newwater.com.newwater.App;
import newwater.com.newwater.constants.UriConstant;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;

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
        boolean test = false;
        if (response.code() == 403) {
            test = true;
        }
        else{
            test = false;
        }
//        test false;
        return  test;
    }


    /**
     * 同步请求方式，获取最新的Token
     *
     * @return token
     */
    private String getNewToken() throws IOException {
        String accessToken = "";
        OkHttpClient client = new OkHttpClient();
        String url = RestUtils.getUrl(UriConstant.GETTOKEN)+"?loginName=123&loginPassword=456";

        //Request是OkHttp中访问的请求，Builder是辅助类，Response即OkHttp中的响应
        final Request request = new Request.Builder()
                .url(url)
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
//                BaseSharedPreferences.setString(App.getInstance(), "accessToken", accessToken);
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