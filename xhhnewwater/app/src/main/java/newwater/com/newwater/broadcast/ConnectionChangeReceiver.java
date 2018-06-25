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
 * Created by Administrator on 2018/6/4.
 */

public class ConnectionChangeReceiver extends BroadcastReceiver {
    private final static String TAG = ConnectionChangeReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        Intent it = new Intent();

        if (networkInfo != null && networkInfo.isAvailable()) {
            switch (networkInfo.getType()) {
                case ConnectivityManager.TYPE_MOBILE:
                    Toast.makeText(context, "正在使用2G/3G/4G网络", Toast.LENGTH_SHORT).show();
                    break;
                case ConnectivityManager.TYPE_WIFI:
                    Toast.makeText(context, "上传数据到服务器，删除本地数据库", Toast.LENGTH_SHORT).show();
//                    it.setClass(context, MainActivity.class);
//                    context.startActivity(it);
                    break;
                default:
                    break;
            }
        } else {
            Toast.makeText(context, "当前无网络连接", Toast.LENGTH_SHORT).show();
            it.setClass(context, BreakDownActivity.class);
            context.startActivity(it);
        }
    }
}
