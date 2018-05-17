package newwater.com.newwater.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/4/19 0019.
 */

public class ControllerUtils {

        //取热水
         public String getHotwater(Activity context){
             Toast.makeText(context,"取热水",Toast.LENGTH_SHORT).show();
             //TODO 和android板子通讯
             return "true";
         }


        public String getWenshui(Activity context){
            Toast.makeText(context,"取温水",Toast.LENGTH_SHORT).show();
            //TODO 和android板子通讯
            return "true";
        }


        public String getCoolwater(Activity context){
            Toast.makeText(context,"取冷水",Toast.LENGTH_SHORT).show();
            //TODO 和android板子通讯
            return "true";
        }

        public String getCup(Activity context){
        Toast.makeText(context,"取纸杯",Toast.LENGTH_SHORT).show();
        //TODO 和android板子通讯
        return "true";
       }


}
