package newwater.com.newwater.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import newwater.com.newwater.view.activity.BreakDownActivity;
import newwater.com.newwater.view.activity.MainActivity;

/**
 * Created by Administrator on 2018/6/4 0004.
 */

public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
//            MainActivity.moveToBreakDownActivity();
//            Intent it = new Intent();
//            it.setClass(context, BreakDownActivity.class);
//            context.startActivity(it);

            //改变背景或者 处理网络的全局变量
        }else {
            //改变背景或者 处理网络的全局变量
            Toast.makeText(context, "上传数据到服务器，删除本地数据库", Toast.LENGTH_SHORT).show();
//            Intent it = new Intent();
//            it.setClass(context, MainActivity.class);
//            context.startActivity(it);
        }
    }


}
