package newwater.com.newwater.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

public class CommonUtil {

    /**
     * Base64加密
     * @param str
     * @return
     */
    public static String encode(String str) {
        String result = "";
        if( str != null) {
            try {
                result = new String(Base64.encode(str.getBytes("utf-8"), Base64.NO_WRAP),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Base64解密
     * @param str
     * @return
     */

    public static String decode(String str) {
        String result = "";
        if (str != null) {
            try {
                result = new String(Base64.decode(str, Base64.NO_WRAP), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}

