package purewater.com.leadapp.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/15 0015.
 * https://blog.csdn.net/z740852294/article/details/77700310?locationNum=9&fps=1
 */

public class XutilsHttp {
    private volatile static XutilsHttp instance;
    private Handler handler;
    private XutilsHttp(){
        handler = new Handler(Looper.getMainLooper());
    }

    public static XutilsHttp getInstance(){
        if (instance == null){
            synchronized (XutilsHttp.class){
                if (instance == null){
                    instance = new XutilsHttp();
                }
            }
        }
        return instance;
    }



    private void onSuccessResponse(final String result, final XCallBack callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onResponse(result);
                }
            }
        });
    }


    private void onFailResponse(final String result, final XCallBack callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onFail(result);
                }
            }
        });
    }






    //    http://www.jb51.net/article/101697.htm
    public void get(boolean gettoken,Context context, String url, Map<String,String> maps, final XCallBack callback){
        RequestParams params = new RequestParams(url);
        if (null != maps && !maps.isEmpty()){
            for (Map.Entry<String,String> entry : maps.entrySet()){
                params.addQueryStringParameter(entry.getKey(),entry.getValue());
            }
        }
        if(gettoken){
            String test = BaseSharedPreferences.getString(context,BaseSharedPreferences.TOKEN);
            params.addHeader("authorization",BaseSharedPreferences.getString(context,BaseSharedPreferences.TOKEN));
        }
        x.http().get(params, new Callback.CommonCallback<String>() {
            private boolean hasError = false;
            private String result = null;
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    this.result = result;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    onSuccessResponse(result,callback);
                }
            }
        });
    }

}
