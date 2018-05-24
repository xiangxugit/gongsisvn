package newwater.com.newwater.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import newwater.com.newwater.interfaces.OnUpdateUI;

/**
 * Created by Administrator on 2018/5/24 0024.
 */

public class UpdateBroadcast extends BroadcastReceiver {

    OnUpdateUI onUpdateUI;


    @Override
    public void onReceive(Context context, Intent intent) {
        String progress = intent.getStringExtra("progress");
        onUpdateUI.updateUI(progress);
    }


    public void SetOnUpdateUI(OnUpdateUI onUpdateUI){
        this.onUpdateUI = onUpdateUI;
    }

}
