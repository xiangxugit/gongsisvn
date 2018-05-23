package newwater.com.newwater.view;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.serialport.DevUtil;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Handler;

import newwater.com.newwater.MainActivity;
import newwater.com.newwater.R;

import static newwater.com.newwater.Processpreserving.DaemonService.comThread;

/**
 * 自定义的PopupWindow
 */
public class PopWarning extends PopupWindow {
    private Activity context;
    private RelativeLayout outcupleft;
    private RelativeLayout outcupright;
    private TextView bottomsure;
    private DevUtil devUtil;
    public PopWarning(Activity context) {
        // 通过layout的id找到布局View
        this.context = context;
        View contentView = LayoutInflater.from(context).inflate(R.layout.prompt_pop, null);
        // 获取PopupWindow的宽高
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置PopupWindow的View
        this.setContentView(contentView);
        // 设置PopupWindow弹出窗体的宽高
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置PopupWindow弹出窗体可点击（下面两行代码必须同时出现）
        this.setFocusable(true);
        this.setOutsideTouchable(true); // 当点击外围的时候隐藏PopupWindow
        // 刷新状态
        this.update();
        // 设置PopupWindow的背景颜色为半透明的黑色
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#66000000"));
        this.setBackgroundDrawable(dw);
        // 设置PopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopWindowAnimStyle);

        // 这里也可以从contentView中获取到控件，并为它们绑定控件
        outcupleft = (RelativeLayout) contentView.findViewById(R.id.outcupleft);
        outcupleft.setOnClickListener(onclick);
        outcupright = (RelativeLayout) contentView.findViewById(R.id.outcupright);
        outcupright.setOnClickListener(onclick);
        bottomsure = (TextView) contentView.findViewById(R.id.bottomsure);

    }

    // 显示PopupWindow，有两种方法：showAsDropDown、showAtLocation
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // showAsDropDown方法，在parent下方的(x,y)位置显示，x、y是第二和第三个参数
            // this.showAsDropDown(parent, parent.getWidth() / 2 - 400, 18);

            // showAtLocation方法，在parent的某个位置参数，具体哪个位置由后三个参数决定
            this.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else {
            this.dismiss();
        }
    }



    View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            switch (v.getId()) {

                case R.id.outcupleft:
//                    PopWindow popWindow = new PopWindow(context);
//                    popWindow.showPopupWindow(new View(context));
                    if(comThread == null){
                        Toast.makeText(context, "通讯未启动", Toast.LENGTH_SHORT).show();
                    }else{
                        comThread.setActive(false);
                    }


                    String hotwatertext = Pop_RightOperate.hotwater.getText().toString();
                    if("热水".equals(hotwatertext)){
                        Pop_RightOperate.hotwater.setText("停止取水");
                        Pop_RightOperate.hotwater.setBackgroundResource(R.drawable.hotwaterstop);
                    }else{
                        Pop_RightOperate.hotwater.setText("热水");
                    }

//                    Toast.makeText(context,"出热水",Toast.LENGTH_SHORT).show();
//                    int sw=devUtil.get_run_hotWaterSW_value()?2:1;//取反：当前为开，则发送关
//                    String s;
//                    if(sw==1)
//                        s="出热水指令执行";
//                    else
//                        s="停止出热水执行";
                    String s = "出热水指令执行";
                    int sw = 1;

                    devUtil = new DevUtil(null);
                    if(devUtil.isComOpened()==true){
                        if (devUtil.do_ioWater(1, sw) == 0) {
                            Toast.makeText(context, s + "成功", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(context, s + "失败", Toast.LENGTH_SHORT).show();
                        }
                    }else{

                        if (devUtil.do_ioWater(1, sw) == 0) {
                            Toast.makeText(context, s + "成功", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(context, s + "失败", Toast.LENGTH_SHORT).show();
                        }

                    }



                    // 延迟15秒
                    break;
                case R.id.outcupright:
                    Toast.makeText(context,"取消水",Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };


}