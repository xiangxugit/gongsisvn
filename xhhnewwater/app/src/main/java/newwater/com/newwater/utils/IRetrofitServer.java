package newwater.com.newwater.utils;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2018/06/01.
 */

public interface IRetrofitServer {

    String getUrl = "list.from";
    String postUrl = "list.from";

    /**
     * 传递参数的Get请求
     * @param key
     * @param sort
     * @param time
     * @return
     */
    @GET(getUrl)
    Call<ResponseBody> get(@Query("key") String key, @Query("sort") String sort, @Query("time") String time);

    /**
     * 封装好Url的Get的请求
     * @return
     */
    @GET(getUrl + "?key=488c65f3230c0280757b50686d1f1cd5&&sort=asc&&time=1418816972")
    Call<ResponseBody> get();

    /**
     * 传递Map键值对的Get请求
     * @param params
     * @return
     */
    @GET(getUrl)
    Call<ResponseBody> get(@QueryMap Map<String, String> params);

    /**
     * 传递参数的Post请求
     * @param key
     * @param sort
     * @param time
     * @return
     */
    @FormUrlEncoded
    @POST(postUrl)
    Call<ResponseBody> post(@Field("key") String key, @Field("sort") String sort, @Field("time") String time);

    /**
     * 传递Map键值对的Post请求
     * @param map
     * @return 对应的字符串数据
     */
    @FormUrlEncoded
    @POST(postUrl)
    Call<ResponseBody> post(@FieldMap Map<String, String> map);

    /**
     * 传递Map键值对和Header的Post请求
     * @param key
     * @param sort
     * @param time
     * @return
     */
    @Headers({"os:Android", "version:2.0"})
    @FormUrlEncoded
    @POST(postUrl)
    Call<ResponseBody> postWithHeader(@Field("key") String key, @Field("sort") String sort, @Field("time") String time);

    /**
     * 传递Map键值对和Header的Post请求
     * @param os
     * @param key
     * @param sort
     * @param time
     * @return
     */
    @FormUrlEncoded
    @POST(postUrl)
    Call<ResponseBody> postWithHeader(@Header("os") String os, @Field("key") String key, @Field("sort") String sort, @Field("time") String time);

    /**
     * 传递Map键值对和Header的Post请求
     * @param map
     * @param key
     * @param sort
     * @param time
     * @return
     */
    @FormUrlEncoded
    @POST(postUrl)
    Call<ResponseBody> postWithHeader(@HeaderMap Map<String, String> map, @Field("key") String key, @Field("sort") String sort, @Field("time") String time);

    /**
     * 传递访问路径和键值对的Post请求
     * @param path
     * @param key
     * @param sort
     * @param time
     * @return
     */
    @FormUrlEncoded
    @POST("{path}")
    Call<ResponseBody> post(@Path("path") String path, @Field("key") String key, @Field("sort") String sort, @Field("time") String time);
}
