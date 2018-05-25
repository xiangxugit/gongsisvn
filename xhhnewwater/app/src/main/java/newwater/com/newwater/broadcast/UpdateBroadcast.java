package newwater.com.newwater.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import newwater.com.newwater.beans.ViewShow;
import newwater.com.newwater.interfaces.OnUpdateUI;

/**
 * Created by Administrator on 2018/5/24 0024.
 */

public class UpdateBroadcast extends BroadcastReceiver {

    OnUpdateUI onUpdateUI;


    @Override
    public void onReceive(Context context, Intent intent) {


        ViewShow viewShow = (ViewShow)intent.getSerializableExtra("progress");;
        if(null==viewShow||null==onUpdateUI){
            Log.e("one","one");
        }else{
            onUpdateUI.updateUI(viewShow);
        }

    }


    public void SetOnUpdateUI(OnUpdateUI onUpdateUI){
        this.onUpdateUI = onUpdateUI;
    }

}
