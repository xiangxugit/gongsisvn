package newwater.com.newwater.view;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.serialport.DevUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Handler;

import newwater.com.newwater.MainActivity;
import newwater.com.newwater.Processpreserving.ComThread;
import newwater.com.newwater.R;

import static newwater.com.newwater.Processpreserving.DaemonService.comThread;

/**
 * 自定义的PopupWindow
 */
public class Pop_RightOperate extends PopupWindow {
    public static TextView hotwater;//取热水
    private TextView warmwater;//温水
    private TextView coolwater;//冷水
    private TextView dixieccup;//取纸杯
    private Activity context;
    private DevUtil devUtil;
    public Pop_RightOperate(final Activity context) {
        // 通过layout的id找到布局View
        this.context = context;
        View contentView = LayoutInflater.from(context).inflate(R.layout.right_pop, null);
        // 获取PopupWindow的宽高
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置PopupWindow的View
        this.setContentView(contentView);
        // 设置PopupWindow弹出窗体的宽高
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置PopupWindow弹出窗体可点击（下面两行代码必须同时出现）
//        this.setFocusable(true);
        this.setOutsideTouchable(false); // 当点击外围的时候隐藏PopupWindow
        // 刷新状态
        this.update();
        // 设置PopupWindow的背景颜色为半透明的黑色
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#66000000"));
        this.setBackgroundDrawable(dw);
        // 设置PopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopWindowAnimStyle);

        // 这里也可以从contentView中获取到控件，并为它们绑定控件

        hotwater = (TextView) contentView.findViewById(R.id.hotwater);
        hotwater.setOnClickListener(onclick);
        //温水控制
        warmwater = (TextView) contentView.findViewById(R.id.warmwater);
        warmwater.setOnClickListener(onclick);

        coolwater = (TextView) contentView.findViewById(R.id.coolwater);
        coolwater.setOnClickListener(onclick);

    }

    // 显示PopupWindow，有两种方法：showAsDropDown、showAtLocation
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // showAsDropDown方法，在parent下方的(x,y)位置显示，x、y是第二和第三个参数
            // this.showAsDropDown(parent, parent.getWidth() / 2 - 400, 18);

            // showAtLocation方法，在parent的某个位置参数，具体哪个位置由后三个参数决定
            this.showAtLocation(parent, Gravity.RIGHT, 0, 0);
        } else {
            this.dismiss();
        }
    }




    View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            dismiss();
            if(null==devUtil){
                devUtil = new DevUtil(null);
            }

            switch (v.getId()) {
                case R.id.hotwater:
                    if(hotwater.getText().toString().equals("停止取水")){
                        Toast.makeText(context,"停止取水",Toast.LENGTH_SHORT).show();
                        hotwater.setText("热水");
                        hotwater.setBackgroundResource(R.drawable.hotwater);
                        //做假的数据弹出来余额不足
//                        final  MyHandler  handler = new MyHandler();
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    Thread.sleep(4000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                Message msg =Message.obtain();
//                                msg.what=1;   //标志消息的标志
//                                handler.sendMessage(msg);
//                            }
//                        }).start();
                    }
                    else{
                        Pop_LeftOperate leftpop = new Pop_LeftOperate(context);
                        leftpop.showPopupWindow(new View(context));

                        Pop_RightOperate rightPop = new Pop_RightOperate(context);
                        rightPop.showPopupWindow(new View(context));

                        PopWarning PopWarning = new PopWarning(context);
                        PopWarning.showPopupWindow(new View(context));

                    }

                    break;
                case R.id.warmwater://出温水

                    if(null==comThread){
                        Toast.makeText(context,"通讯未启动",Toast.LENGTH_SHORT).show();
                    }else{
                        comThread.setActive(false);
                    }
                    if(warmwater.getText().toString().equals(context.getString(R.string.warmwater))){
                        //出温水
                        String s = "出温水指令执行";
                        int sw = 1;
                        warmwater.setBackgroundResource(R.drawable.hotwaterstop);
                        if (devUtil.do_ioWater(1, sw) == 0) {
                            Toast.makeText(context, s + "成功", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(context, s + "失败", Toast.LENGTH_SHORT).show();
                        }
                        warmwater.setText(context.getString(R.string.stopgetwater));
                    }else{
                        warmwater.setBackgroundResource(R.drawable.wenshui);
                        String s = "出温水停止指令执行";
                        int sw = 2;
                        if (devUtil.do_ioWater(2, sw) == 0) {
                            Toast.makeText(context, s + "成功", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(context, s + "失败", Toast.LENGTH_SHORT).show();
                        }
                        warmwater.setText(context.getString(R.string.warmwater));

                    }
                    break;

                    //调用冷水
                case R.id.coolwater:
                    if(null==comThread){
                        Toast.makeText(context,"通讯未启动",Toast.LENGTH_SHORT).show();
                    }else{
                        comThread.setActive(false);
                    }

                    if(coolwater.getText().toString().equals(context.getString(R.string.coolwater))){
                        //取水
                        String s = "出冷水";
                        int sw = 1;
                        coolwater.setBackgroundResource(R.drawable.hotwaterstop);
                        if (devUtil.do_ioWater(3, sw) == 0) {
                            Toast.makeText(context, s + "成功", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(context, s + "失败", Toast.LENGTH_SHORT).show();
                        }
                        coolwater.setText(context.getString(R.string.stopgetwater));
                    }

                    else{
                        String s = "停止出水";
                        int sw = 2;
                        coolwater.setBackgroundResource(R.drawable.coolwater);
                        if (devUtil.do_ioWater(3, sw) == 0) {
                            Toast.makeText(context, s + "成功", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(context, s + "失败", Toast.LENGTH_SHORT).show();
                        }
                        coolwater.setText(context.getString(R.string.coolwater));
                    }
                    break;
                case R.id.dixieccup:

                    if(null==comThread){
                        Toast.makeText(context,"通讯未启动",Toast.LENGTH_SHORT).show();
                    }else{
                        comThread.setActive(false);
                    }

                    if(dixieccup.getText().toString().equals(context.getString(R.string.getcup))){
                        //取水
                        String s = "出冷水";
                        int sw = 1;
                        coolwater.setBackgroundResource(R.drawable.hotwaterstop);
                        if (devUtil.do_ioWater(3, sw) == 0) {
                            Toast.makeText(context, s + "成功", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(context, s + "失败", Toast.LENGTH_SHORT).show();
                        }
                        coolwater.setText(context.getString(R.string.stopgetwater));
                    }else{

                    }



                    break;
            }

        }

    };

    /**
     * 为避免handler造成的内存泄漏
     * 1、使用静态的handler，对外部类不保持对象的引用
     * 2、但Handler需要与Activity通信，所以需要增加一个对Activity的弱引用
     */
    public   class MyHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {    //获取消息，更新UI
                case 1:
                    //进行uI操作
                    Toast.makeText(context,"余额不足处理",Toast.LENGTH_SHORT).show();
                    Pop_Buy popbug = new Pop_Buy(context,"test");
                    popbug.showPopupWindow(new View(context));
                    break;
            }
        }
    }







}