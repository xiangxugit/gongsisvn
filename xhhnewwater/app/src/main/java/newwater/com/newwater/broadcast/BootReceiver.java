package newwater.com.newwater.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import newwater.com.newwater.view.activity.MainActivity;

/**
 * Created by Administrator on 2018/6/4 0004.
 */

public class BootReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            Intent mainactivity = new Intent(context, MainActivity.class);
            mainactivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainactivity);
        }
    }
}
