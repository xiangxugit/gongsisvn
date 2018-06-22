package newwater.com.newwater.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.serialport.DevUtil;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import newwater.com.newwater.processpreserve.ComThread;
import newwater.com.newwater.processpreserve.DaemonService;
import newwater.com.newwater.R;
import newwater.com.newwater.TestJSON;
import newwater.com.newwater.beans.DispenserCache;
import newwater.com.newwater.beans.WaterSaleRecordAO;
import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.constants.UriConstant;
import newwater.com.newwater.utils.BaseSharedPreferences;
import newwater.com.newwater.utils.CommonUtil;
import newwater.com.newwater.utils.OkHttpUtils;
import newwater.com.newwater.utils.RestUtils;
import newwater.com.newwater.utils.TimeRun;
import newwater.com.newwater.view.activity.BaseActivity;
import okhttp3.Request;

/**
 * 自定义的PopupWindow
 */
public class PopRightOperate extends PopupWindow {
    private final PopupWindow pop;
//    private final PopupWindow leftpop;
    public static TextView hotwater;//取热水
    private TextView warmwater;//温水
    private TextView coolwater;//冷水
    private TextView getcup;//取纸杯
    private BaseActivity context;
    private DevUtil devUtil;

    private boolean chushuiflag = false;
    private boolean getCupfalg = false;
    private ComThread comThread;

    private WaterSaleRecordAO waterSaleRecordAO;
    private int waterRecordType = 10;//售水模式
    private int waterRecordIsCup = 0;//是否有纸杯
    private int waterFlow = 0;//售水量
    private int waterRecordCupNum = 0;//纸杯数量

    private TimerTask task;
    private Timer timer;

    private String operateType;

    // TODO: 2018/6/15 0015 这个handler没有写回收
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.MSG_QUIT_DRINK_INTERFACE:
                    DispenserCache.isFreeAdDone = false;
                    context.dismissOperatePop();
                    context.showPopWantWater();
                    turnoff();
                    break;


                case Constant.MSG_CHECK_OVERFLOW:
                    //进行uI操作
                    Toast.makeText(context,"不能超过"+BaseSharedPreferences.getInt(context,Constant.DEVICE_MAX_FLOW_KEY),Toast.LENGTH_SHORT).show();
                    Toast.makeText(context,"超过没"+(BaseSharedPreferences.getInt(context,Constant.DEVICE_MAX_FLOW_KEY)-devUtil.get_run_waterFlow_value()),Toast.LENGTH_SHORT).show();
                    if(BaseSharedPreferences.getInt(context,Constant.DEVICE_MAX_FLOW_KEY)-devUtil.get_run_waterFlow_value()<0){
                        context.dismissOperatePop();
                        turnoff();
                        task.cancel();
                    }
                    else{
                        Log.e("没有超过","没有超过");
                    }

                    break;
            }
        }
    };

    public PopRightOperate(final BaseActivity context, PopupWindow pop) {
        // 通过layout的id找到布局View
        this.context = context;
        this.pop = pop;
//        this.leftpop = popleft;
        comThread = DaemonService.comThread;
        if (null == comThread) {
            comThread = new ComThread(context, null);
        }
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_right, null);
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

        hotwater = (TextView) contentView.findViewById(R.id.hot_water);
        hotwater.setOnClickListener(onclick);
        //温水控制
        warmwater = (TextView) contentView.findViewById(R.id.warm_water);
        warmwater.setOnClickListener(onclick);
        //冷水控制
        coolwater = (TextView) contentView.findViewById(R.id.cool_water);
        coolwater.setOnClickListener(onclick);
        //出杯按钮
        getcup = (TextView) contentView.findViewById(R.id.get_cup);
        getcup.setOnClickListener(onclick);

    }


    public boolean canOperateOrNot() {
        boolean canoperate;
        if (true == chushuiflag || true == getCupfalg) {
            canoperate = true;
        } else {
            canoperate = false;
        }
        return canoperate;

    }

    //flag 1出水 flag2 出杯
    public void toastUtils(int flag) {
        if (flag == 1) {
            Toast.makeText(context, context.getString(R.string.getwatering), Toast.LENGTH_SHORT).show();
        }
        if (flag == 2) {
            Toast.makeText(context, context.getString(R.string.getcuping), Toast.LENGTH_SHORT).show();
        }
    }

    // 显示PopupWindow，有两种方法：showAsDropDown、showAtLocation
    public void showPopupWindow(View parent) {
        if (handler.hasMessages(Constant.MSG_QUIT_DRINK_INTERFACE)) {
            handler.removeMessages(Constant.MSG_QUIT_DRINK_INTERFACE);
        }
        handler.sendEmptyMessageDelayed(Constant.MSG_QUIT_DRINK_INTERFACE,
                Constant.QUIT_DRINK_INTERFACE_WAIT_TIME * 1000);
        if (!this.isShowing()) {
            // showAsDropDown方法，在parent下方的(x,y)位置显示，x、y是第二和第三个参数
            // this.showAsDropDown(parent, parent.getWidth() / 2 - 400, 18);

            // showAtLocation方法，在parent的某个位置参数，具体哪个位置由后三个参数决定
            this.showAtLocation(parent, Gravity.RIGHT, 0, 0);
        } else {
//            this.dismiss();
        }
    }


    View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            dismiss();
            String operateType = "1";//饮水操作 1：饮水 2出杯
            if (handler.hasMessages(Constant.MSG_QUIT_DRINK_INTERFACE)) {
                handler.removeMessages(Constant.MSG_QUIT_DRINK_INTERFACE);
            }
            handler.sendEmptyMessageDelayed(Constant.MSG_QUIT_DRINK_INTERFACE,
                    Constant.QUIT_DRINK_INTERFACE_WAIT_TIME * 1000);

            if (null == devUtil) {
                devUtil = new DevUtil(null);
            }
            if (null == waterSaleRecordAO) {
                waterSaleRecordAO = new WaterSaleRecordAO();
            }

            if (null == comThread) {
                Toast.makeText(context, "通讯未启动启动中", Toast.LENGTH_SHORT).show();
                comThread = new ComThread(context, null);
            } else {
                Toast.makeText(context, "通讯启动", Toast.LENGTH_SHORT).show();
                comThread.setActive(false);
            }
            if(null==DispenserCache.userIdTemp){

            }
            else{
                waterSaleRecordAO.setUserId(Integer.parseInt(DispenserCache.userIdTemp));
            }
            if(0 == BaseSharedPreferences.getInt(context,Constant.DEVICE_ID_KEY)){

            }else{
                waterSaleRecordAO.setDeviceId(BaseSharedPreferences.getInt(context,Constant.DEVICE_ID_KEY));
            }
            //支付模式饮水
//            if (Integer.parseInt(TestJSON.getSaletYpe()) == 1) {
//            }
            waterSaleRecordAO.setProductChargMode(BaseSharedPreferences.getInt(context,Constant.DRINK_MODE_KEY));
            switch (v.getId()) {
                case R.id.hot_water:
                    if (CommonUtil.isFastClick()) {
                        Toast.makeText(context, "操作过快，请稍后再取", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (hotwater.getText().toString().equals("停止取水")) {
                            Toast.makeText(context, "停止取水", Toast.LENGTH_SHORT).show();
                            hotwater.setText("热水");
                            hotwater.setBackgroundResource(R.drawable.hot_water);
                            waterRecordType = 1;
                            //停止取热水
                            //关闭取热水
                            hotwater.setText("热水");
                            String s = "出热水指令执行";
                            int sw = 2;
                            try {
                                if (devUtil.do_ioWater(1, sw) == 0) {
                                    Toast.makeText(context, s + "成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, s + "失败", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                            } finally {
                                comThread.setActive(true);
                            }
                            //获取到本次出水的量
                            String[][] data = devUtil.toArray();
                            int getwaterdata = Integer.parseInt(data[17][1]);


                        } else {

                            chushuiflag = false;
                            context.showPopLeftOperate();
                            context.showPopRightOperate();
                            context.showPopWarning();
                            return;
                        }
                    }
                    break;

                case R.id.warm_water://出温水
                    waterRecordType = 2;
                    if (null == comThread) {
                        Toast.makeText(context, "通讯未启动", Toast.LENGTH_SHORT).show();
                    } else {
                        comThread.setActive(false);
                    }

                    if (CommonUtil.isFastClick()) {
                        Toast.makeText(context, "操作过快，请稍后再取", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (warmwater.getText().toString().equals(context.getString(R.string.warm_water))) {
                            //出温水
                            if (canOperateOrNot()) {
                                toastUtils(1);
                                return;
                            }
                            chushuiflag = true;
                            String s = "出温水指令执行";
                            int sw = 1;
                            warmwater.setBackgroundResource(R.drawable.hotwaterstop);
                            try {
                                if (devUtil.do_ioWater(2, sw) == 0) {
                                    Toast.makeText(context, s + "成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, s + "失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                            } finally {
                                comThread.setActive(true);
                            }
                            warmwater.setText(context.getString(R.string.stopgetwater));
                        } else {
                            warmwater.setBackgroundResource(R.drawable.warm_water);
                            String s = "出温水停止指令执行";
                            int sw = 2;
                            try {
                                if (devUtil.do_ioWater(2, sw) == 0) {
                                    Toast.makeText(context, s + "成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, s + "失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                            } finally {
                                comThread.setActive(true);
                            }
                            warmwater.setText(context.getString(R.string.warm_water));
                            chushuiflag = false;


                        }
                    }
                    break;

                case R.id.cool_water://调用
                    waterRecordType = 3;
                    if (null == comThread) {
                        Toast.makeText(context, "通讯未启动", Toast.LENGTH_SHORT).show();
                    } else {
                        comThread.setActive(false);
                    }

                    if (CommonUtil.isFastClick()) {
                        Toast.makeText(context, "操作过快，请稍后再取", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (coolwater.getText().toString().equals(context.getString(R.string.coolwater))) {
                            //取冷水
                            if (canOperateOrNot()) {
                                toastUtils(1);
                                return;
                            }
                            chushuiflag = true;
                            String s = "出冷水";
                            int sw = 1;
                            coolwater.setBackgroundResource(R.drawable.hotwaterstop);
                            try {
                                if (devUtil.do_ioWater(3, sw) == 0) {
                                    Toast.makeText(context, s + "成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, s + "失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                            } finally {
                                comThread.setActive(true);
                            }
                            coolwater.setText(context.getString(R.string.stopgetwater));

                        } else {
                            String s = "停止出水";
                            int sw = 2;
                            coolwater.setBackgroundResource(R.drawable.cool_water);
                            try {
                                if (devUtil.do_ioWater(3, sw) == 0) {
                                    Toast.makeText(context, s + "成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, s + "失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                            } finally {
                                comThread.setActive(true);
                            }
                            coolwater.setText(context.getString(R.string.coolwater));

                            chushuiflag = false;
                        }
                    }
                    break;

                case R.id.get_cup:
                    operateType = "2";
                    if (null == comThread) {
                        Toast.makeText(context, "通讯未启动", Toast.LENGTH_SHORT).show();
                    } else {
                        comThread.setActive(false);
                    }

                    if (CommonUtil.isFastClick()) {
                        Toast.makeText(context, "操作过快，请稍后再取", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        String s = "出杯子";
                        if (getcup.getText().toString().equals(context.getString(R.string.getcup))) {
                            //出杯子
                            getcup.setBackgroundResource(R.drawable.hotwaterstop);
                            try {
                                if (devUtil.do_ioCup() == 0) {
                                    getcup.setText(context.getString(R.string.getcup));
                                    Toast.makeText(context, s + "成功", Toast.LENGTH_SHORT).show();
                                    getcup.setBackgroundResource(R.drawable.getcup);
                                } else {
                                    getcup.setText(context.getString(R.string.getcup));
                                    Toast.makeText(context, s + "失败", Toast.LENGTH_SHORT).show();
                                    getcup.setBackgroundResource(R.drawable.getcup);
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                            } finally {
                                comThread.setActive(true);
                            }
                            if (canOperateOrNot()) {
                                toastUtils(1);
                            }
                        }
                    }
                    break;
            }
            waterSaleRecordAO.setWaterRecordType(waterRecordType);
            //售水中是否缺纸杯
            int nocupflag = devUtil.get_run_bCup_value();
            if (01 == nocupflag) {
                waterSaleRecordAO.setWaterRecordIsCup(1);
            } else {
                //缺纸杯
                waterSaleRecordAO.setWaterRecordIsCup(0);
            }
            //获取售水量
            waterSaleRecordAO.setWaterFlow(devUtil.get_run_waterFlow_value());
            //设置纸杯数量
            waterSaleRecordAO.setWaterRecordCupNum(2);
            //开始监听出水量
            Time warningTime = new Time();
            warningTime.setToNow();
//            final  MyHandler myHandler = new MyHandler();
//            Date waringTimeUpdate = TimeRun.tasktime(warningTime.hour, warningTime.minute, warningTime.second);
             task = new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = Constant.MSG_CHECK_OVERFLOW;
                    handler.sendMessage(msg);
                }
            };
//            TimeRun waringRunScode = new TimeRun(MainActivity.this, waringTimeUpdate, myHandler, Constant.UPLOAD_TIME, Constant.MSG_UPDATE_SCODE, Constant.TIME_OPETATE_WARNING);
//            waringRunScode.startTimer();

            if (chushuiflag == false) {

                //所有的设置为不可用
                //重新设置各种过滤设备的数值
                //重新设置pp棉制水量
                Toast.makeText(context,"水量"+devUtil.get_run_waterFlow_value(),Toast.LENGTH_SHORT).show();
                BaseSharedPreferences.setInt(context,Constant.DEVICE_PP_FLOW_KEY,BaseSharedPreferences.getInt(context, Constant.DEVICE_PP_FLOW_KEY)-waterSaleRecordAO.getWaterFlow());
                //重新设置
                BaseSharedPreferences.setInt(context,Constant.DEVICE_GRAIN_CARBON_KEY,BaseSharedPreferences.getInt(context,Constant.DEVICE_GRAIN_CARBON_KEY)-waterSaleRecordAO.getWaterFlow());

                BaseSharedPreferences.setInt(context,Constant.DEVICE_PRESS_CARBON_KEY,BaseSharedPreferences.getInt(context,Constant.DEVICE_PRESS_CARBON_KEY)-waterSaleRecordAO.getWaterFlow());

                BaseSharedPreferences.setInt(context,Constant.DEVICE_POSE_CARBON_KEY,BaseSharedPreferences.getInt(context,Constant.DEVICE_POSE_CARBON_KEY)-waterSaleRecordAO.getWaterFlow());

                BaseSharedPreferences.setInt(context,Constant.DEVICE_RO_FLOW_KEY,BaseSharedPreferences.getInt(context,Constant.DEVICE_RO_FLOW_KEY)-waterSaleRecordAO.getWaterFlow());
                //上传售水记录
                String salewater;
                if(operateType=="1"){
                    salewater  = RestUtils.getUrl(UriConstant.WATERSALE) + "?userId=" + waterSaleRecordAO.getUserId() + "&deviceId=" + waterSaleRecordAO.getDeviceId() +
                            "&productChargMode=" + waterSaleRecordAO.getProductChargMode() + "&waterRecordType=" + waterSaleRecordAO.getWaterRecordType() +
                            "&waterRecordIsCup=" + waterSaleRecordAO.getWaterRecordIsCup() + "&waterFlow=" + waterSaleRecordAO.getWaterFlow() +
                            "&waterRecordCupNum=" + waterSaleRecordAO.getWaterRecordCupNum();

                }else{
                     salewater = RestUtils.getUrl(UriConstant.WATERSALE) + "?userId=" + waterSaleRecordAO.getUserId() + "&deviceId=" + waterSaleRecordAO.getDeviceId() +
                            "&productChargMode=" + waterSaleRecordAO.getProductChargMode() + "&waterRecordType=" + waterSaleRecordAO.getWaterRecordType() +
                            "&waterRecordIsCup=" + waterSaleRecordAO.getWaterRecordIsCup() + "&waterFlow=" + 0 +
                            "&waterRecordCupNum=" + waterSaleRecordAO.getWaterRecordCupNum();
                }
                if(operateType=="2"){
                }else{
                    context.dismissOperatePop();
                    context.showPopWantWater();
                }
                if(null == timer){
                }else{
                    timer.cancel();
                }

                String salewaterString = JSON.toJSONString(waterSaleRecordAO);
                Log.e("salewaterString", "salewaterString" + salewaterString);
                OkHttpUtils.getAsyn(salewater, new OkHttpUtils.StringCallback() {
                    @Override
                    public void onFailure(int errCode, Request request, IOException e) {
//                        Toast.makeText(context,request.toString(), Toast.LENGTH_LONG).show();
                        Log.e("responsea", "errCode = " + errCode + "response" + request.toString());
                    }
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(context,response, Toast.LENGTH_LONG).show();
                        Log.e("response", "response" + response);
                    }
                });


            } else {
                Log.e("操作中", "操作中");
                Date time = TimeRun.tasktime(warningTime.hour, warningTime.minute, warningTime.second);
                timer = new Timer(true);
                timer.schedule(task, time, 3000);

            }


        }

    };

    /**
     * 为避免handler造成的内存泄漏
     * 1、使用静态的handler，对外部类不保持对象的引用
     * 2、但Handler需要与Activity通信，所以需要增加一个对Activity的弱引用
     */
    public class MyHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {    //获取消息，更新UI
                case 0:
                    //进行uI操作

                    if(BaseSharedPreferences.getInt(context,Constant.DEVICE_MAX_FLOW_KEY)-devUtil.get_run_waterFlow_value()<0){
                        context.dismissOperatePop();
                        turnoff();
                        task.cancel();
                    }
                    else{
                        Log.e("没有超过","没有超过");
                    }

                    break;
            }
        }
    }

    public void turnoff(){
        coolwater.setBackgroundResource(R.drawable.cool_water);
        coolwater.setText(context.getString(R.string.coolwater));
        warmwater.setText(context.getString(R.string.warm_water));
        warmwater.setBackgroundResource(R.drawable.warm_water);
        hotwater.setBackgroundResource(R.drawable.hot_water);
        hotwater.setText("热水");
         if(null==timer){}else{
             timer.cancel();
         }
        int sw = 2;
        try {
            //关闭取热水
            if (devUtil.do_ioWater(1, sw) == 0) {

            } else {

            }
            //关闭取温水

            if (devUtil.do_ioWater(2, sw) == 0) {

            } else {

            }
            //关闭冷水
            if (devUtil.do_ioWater(3, sw) == 0) {

            } else {

            }
            //关闭所有

            if (devUtil.do_ioWater(255, sw) == 0) {

            } else {

            }
            //自动扣费
            waterSaleRecordAO.setWaterRecordType(waterRecordType);
            //售水中是否缺纸杯
            int nocupflag = devUtil.get_run_bCup_value();
            if (01 == nocupflag) {
                waterSaleRecordAO.setWaterRecordIsCup(1);
            } else {
                //缺纸杯
                waterSaleRecordAO.setWaterRecordIsCup(0);
            }

            //获取售水量
            waterSaleRecordAO.setWaterFlow(devUtil.get_run_waterFlow_value());
            //设置纸杯数量
            waterSaleRecordAO.setWaterRecordCupNum(2);
            String saleWater;
            if(operateType=="1"){
                 saleWater  = RestUtils.getUrl(UriConstant.WATERSALE) + "?userId=" + waterSaleRecordAO.getUserId() + "&deviceId=" + waterSaleRecordAO.getDeviceId() +
                        "&productChargMode=" + waterSaleRecordAO.getProductChargMode() + "&waterRecordType=" + waterSaleRecordAO.getWaterRecordType() +
                        "&waterRecordIsCup=" + waterSaleRecordAO.getWaterRecordIsCup() + "&waterFlow=" + waterSaleRecordAO.getWaterFlow() +
                        "&waterRecordCupNum=" + waterSaleRecordAO.getWaterRecordCupNum();

            }else{
                saleWater = RestUtils.getUrl(UriConstant.WATERSALE) + "?userId=" + waterSaleRecordAO.getUserId() + "&deviceId=" + waterSaleRecordAO.getDeviceId() +
                        "&productChargMode=" + waterSaleRecordAO.getProductChargMode() + "&waterRecordType=" + waterSaleRecordAO.getWaterRecordType() +
                        "&waterRecordIsCup=" + waterSaleRecordAO.getWaterRecordIsCup() + "&waterFlow=" + 0 +
                        "&waterRecordCupNum=" + waterSaleRecordAO.getWaterRecordCupNum();
            }

            OkHttpUtils.getAsyn(saleWater, new OkHttpUtils.StringCallback() {
                @Override
                public void onFailure(int errCode, Request request, IOException e) {
//                        Toast.makeText(context,request.toString(), Toast.LENGTH_LONG).show();
                    Log.e("responsea", "errCode = " + errCode + "response" + request.toString());
                }
                @Override
                public void onResponse(String response) {
//                        Toast.makeText(context,response, Toast.LENGTH_LONG).show();
                    Log.e("response", "response" + response);
                }
            });


        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            comThread.setActive(true);
        }

    }
//   public class  Ha





}