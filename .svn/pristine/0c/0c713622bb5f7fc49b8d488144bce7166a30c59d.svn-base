package newwater.com.newwater.utils;

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
 * Xutils3框架网络请求封装(单例模式):https://blog.csdn.net/z740852294/article/details/77700310?locationNum=9&fps=1
 */

public class XutilsHttp {
    private volatile static XutilsHttp instance;
    private Handler handler;

    private XutilsHttp() {
        handler = new Handler(Looper.getMainLooper());
    }

    public static XutilsHttp getInstance() {
        if (instance == null) {
            synchronized (XutilsHttp.class) {
                if (instance == null) {
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

    /**
     * get请求
     * @param gettoken
     * @param context
     * @param url
     * @param maps
     * @param callback
     */
    public void get(boolean gettoken, Context context, String url, Map<String, String> maps, final XCallBack callback) {
        RequestParams params = new RequestParams(url);
        if (null != maps && !maps.isEmpty()) {
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        if (gettoken) {
            String test = BaseSharedPreferences.getString(context, BaseSharedPreferences.TOKEN);
            params.addHeader("authorization", BaseSharedPreferences.getString(context, BaseSharedPreferences.TOKEN));
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
                    onSuccessResponse(result, callback);
                }
            }
        });
    }

    /**
     * 异步post请求
     * @param url
     * @param maps
     * @param callback
     */
    public void post(String url, Map<String, String> maps, final XCallBack callback) {
        RequestParams params = new RequestParams(url);
        if (null != maps && !maps.isEmpty()) {
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
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
                onFailResponse(ex.getMessage(), callback);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    onSuccessResponse(result, callback);
                }
            }
        });
    }

    /**
     * 下载文件
     * @param url
     * @param filePath
     * @param callback
     */
    public void downFile(String url, String filePath, final XDownLoadCallBack callback) {
        RequestParams params = new RequestParams(url);
        final File filepath = new File(filePath);
        if (!filepath.exists()) {
            filepath.mkdir();//如果路径不存在就先创建路径
        }
        params.setSaveFilePath(filePath);
        params.setAutoRename(true);
        x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(final File result) {
                //下载完成会走该方法
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onSuccess(result);
                        }
                    }
                });
            }

            @Override
            public void onError(final Throwable ex, boolean isOnCallback) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (null != callback) {
                            callback.onFailure(ex.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFinished();
                        }
                    }
                });
            }

            //网络请求之前回调
            @Override
            public void onWaiting() {
            }

            //网络请求开始的时候回调
            @Override
            public void onStarted() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (null != callback) {
                            callback.onstart();
                        }
                    }
                });
            }

            //下载的时候不断回调的方法
            @Override
            public void onLoading(final long total, final long current, final boolean isDownloading) {
                //当前进度和文件总大小
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onLoading(total, current, isDownloading);
                        }
                    }
                });
            }
        });
    }

    //下载的接口回调
    public interface XDownLoadCallBack {
        void onstart();

        void onLoading(long total, long current, boolean isDownloading);

        void onSuccess(File file);

        void onFailure(String result);

        void onFinished();
    }

}
