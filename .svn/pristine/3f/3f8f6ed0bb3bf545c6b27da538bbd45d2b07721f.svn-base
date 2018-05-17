package purewater.com.leadapp.utils;

import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * 控制读写缓存
 * @author z00218627
 * 
 */
@SuppressLint("NewApi") public class BaseSharedPreferences
{
    
    
    
    // 公共的用户信息缓�?
    public static final String COMMON = "common";
    public static final String TOKEN = "token";//key值


    /**
     * 
     * @param context
     * @return 获取公共数据
     */
    public static String getString(Context context, String key)
    {
        String value = context.getSharedPreferences(COMMON, Context.MODE_PRIVATE).getString(key, "");
        return value;
    }
    
    /**
     * 
     * @param context
     * @return long型数�?
     */
    public static long getLong(Context context, String key)
    {
        long value = context.getSharedPreferences(COMMON, Context.MODE_PRIVATE).getLong(key, 0);
        return value;
    }
    

    /**
     * 
     * @param context
     * @return 设置公共缓存数据
     */
    public static void setString(Context context, String key, String keyValue)
    {
        context.getSharedPreferences(COMMON, Context.MODE_PRIVATE).edit().putString(key, keyValue).commit();
    }
    
    /**
     * 
     * @param context
     * @return long型数�?
     */
    public static void setLong(Context context, String key, Long keyValue)
    {
        context.getSharedPreferences(COMMON, Context.MODE_PRIVATE).edit().putLong(key, keyValue).commit();
    }
    

    
    /**
     * 
     * @param context
     * @return 删除公共缓存数据
     */
    public static void removeString(Context context, String key)
    {
        context.getSharedPreferences(COMMON, Context.MODE_PRIVATE).edit().remove(key).commit();
    }
    
    /**
     * 
     * @param context
     * @return 删除公共缓存数据
     */
    public static void removelong(Context context, String key)
    {
        context.getSharedPreferences(COMMON, Context.MODE_PRIVATE).edit().remove(key).commit();
    }
    

    /**
     * 
     * @param context
     * @return 是否包含私有缓存数据
     */
    public static boolean containString(Context context, String key)
    {
        return context.getSharedPreferences(COMMON, Context.MODE_PRIVATE).contains(key);
    }
    
    /**
     * 
     * @param context
     * @return 是否包含私有缓存数据
     */
    public static boolean containLong(Context context, String key)
    {
        return context.getSharedPreferences(COMMON, Context.MODE_PRIVATE).contains(key);
    }
    

    

    

    

    
    



}
