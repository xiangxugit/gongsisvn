package newwater.com.newwater.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import newwater.com.newwater.beans.ViewShow;
import newwater.com.newwater.interfaces.OnUpdateUI;
import newwater.com.newwater.utils.LogUtils;

/**
 * Created by Administrator on 2018/5/24 0024.
 */
public class UpdateBroadcast extends BroadcastReceiver {

    private static final String TAG = "UpdateBroadcast";

    OnUpdateUI onUpdateUI;

    @Override
    public void onReceive(Context context, Intent intent) {
        ViewShow viewShow = (ViewShow)intent.getSerializableExtra("progress");
        if(null==viewShow||null==onUpdateUI){
            LogUtils.e(TAG,"one");
        }else{
            onUpdateUI.updateUI(viewShow);
        }
    }


    public void SetOnUpdateUI(OnUpdateUI onUpdateUI){
        this.onUpdateUI = onUpdateUI;
    }

}
