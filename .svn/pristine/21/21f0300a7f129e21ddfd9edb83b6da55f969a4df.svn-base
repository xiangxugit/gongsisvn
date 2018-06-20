package newwater.com.newwater.utils;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import newwater.com.newwater.TestJSON;
import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.constants.UriConstant;
import newwater.com.newwater.view.activity.BaseActivity;
import newwater.com.newwater.view.PopQrCode;
import okhttp3.Request;

public class CommonUtil {

    private static final String TAG = "CommonUtil";

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

    // "^-?\\d+$"
    // /^-?[0-9]\d*$/
    // ^\d+$
    // [0-9]*
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("^-?\\d+$");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 是否是快速点击（是则return，防止快速连续点击）
     * @return
     */
    public static long lastClickTime;
    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= Constant.FAST_CLICK_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    public static void showQrCode(BaseActivity mContext) {
        mContext.showPopQrCode();

        final ImageView qcode = PopQrCode.qrcode;
//        String qcodestring = TestJSON.getWeiXinQcode();
        String qcodestring  = BaseSharedPreferences.getString(mContext,Constant.SCODEKEY);
        Bitmap qcodebitmap = Create2QR2.createBitmap(qcodestring);
        qcode.setImageBitmap(qcodebitmap);
        TextView rightText = PopQrCode.getwater;
        rightText.setText("扫码关注，完成用户绑定");
        //请求二维码
//        String getTempQCodeurl = RestUtils.getUrl(UriConstant.GETTEMPQCODE);
//        OkHttpUtils.getAsyn(getTempQCodeurl, new OkHttpUtils.StringCallback() {
//            @Override
//            public void onFailure(int errCode, Request request, IOException e) {
//
//            }
//            @Override
//            public void onResponse(String response) {
//                Bitmap qcodebitmap = Create2QR2.createBitmap(response);
//                qcode.setImageBitmap(qcodebitmap);
//                TextView rightText = PopQrCode.getwater;
//                rightText.setText("扫码关注，完成用户绑定");
//            }
//        });
    }
}

