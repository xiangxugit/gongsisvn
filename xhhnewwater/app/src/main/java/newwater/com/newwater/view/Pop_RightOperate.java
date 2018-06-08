package newwater.com.newwater.view;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.serialport.DevUtil;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.logging.Handler;

import newwater.com.newwater.MainActivity;
import newwater.com.newwater.Processpreserving.ComThread;
import newwater.com.newwater.Processpreserving.DaemonService;
import newwater.com.newwater.R;
import newwater.com.newwater.TestJSON;
import newwater.com.newwater.beans.WaterSaleRecordAO;
import newwater.com.newwater.beans.Water_Sale_Record;
import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.constants.UriConstant;
import newwater.com.newwater.utils.OkHttpUtils;
import newwater.com.newwater.utils.RestUtils;
import okhttp3.Request;

import static newwater.com.newwater.Processpreserving.DaemonService.comThread;

/**
 * 自定义的PopupWindow
 */
public class Pop_RightOperate extends PopupWindow {
    public static TextView hotwater;//取热水
    private TextView warmwater;//温水
    private TextView coolwater;//冷水
    private TextView getcup;//取纸杯
    private Activity context;
    private DevUtil devUtil;

    private boolean chushuiflag = false;
    private boolean getCupfalg = false;
    private ComThread comThread;

    private WaterSaleRecordAO waterSaleRecordAO;
    private int waterRecordType = 1;//售水模式
    private int waterRecordIsCup = 0;//是否有纸杯
    private int waterFlow = 0;//售水量
    private int waterRecordCupNum = 0;//纸杯数量

    public Pop_RightOperate(final Activity context) {
        // 通过layout的id找到布局View
        this.context = context;
        comThread = DaemonService.comThread;
        if(null==comThread){
            comThread = new ComThread(context, null);
        }
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
        //冷水控制
        coolwater = (TextView) contentView.findViewById(R.id.coolwater);
        coolwater.setOnClickListener(onclick);
        //出杯按钮
        getcup = (TextView) contentView.findViewById(R.id.getcup);
        getcup.setOnClickListener(onclick);

    }


    public boolean canOperateOrNot(){
        boolean canoperate;
        if(true ==chushuiflag||true == getCupfalg){
            canoperate =  true;
        }else{
            canoperate = false;
        }
        return  canoperate;

    }
    //flag 1出水 flag2 出杯
    public void toastUtils(int flag){
         if(flag==1){
             Toast.makeText(context,context.getString(R.string.getwatering),Toast.LENGTH_SHORT).show();
         }
         if(flag==2){
             Toast.makeText(context,context.getString(R.string.getcuping),Toast.LENGTH_SHORT).show();
         }
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
            if(null==waterSaleRecordAO){
                waterSaleRecordAO = new WaterSaleRecordAO();
            }
            waterSaleRecordAO.setUserId(Integer.parseInt(TestJSON.getUserid()));
            waterSaleRecordAO.setDeviceId(Integer.parseInt(TestJSON.getDeviceid()));
            //支付模式饮水
            if(Integer.parseInt(TestJSON.getSaletYpe())==1){

            }
            waterSaleRecordAO.setProductChargMode(Integer.parseInt(TestJSON.getSaletYpe()));
            switch (v.getId()) {
                case R.id.hotwater:
                    if(hotwater.getText().toString().equals("停止取水")){
                        Toast.makeText(context,"停止取水",Toast.LENGTH_SHORT).show();
                        hotwater.setText("热水");
                        hotwater.setBackgroundResource(R.drawable.hotwater);

                        waterRecordType = 1;
                    }
                    else{
                        chushuiflag = false;
                        Pop_LeftOperate leftpop = new Pop_LeftOperate(context);
                        leftpop.showPopupWindow(new View(context));

                        Pop_RightOperate rightPop = new Pop_RightOperate(context);
                        rightPop.showPopupWindow(new View(context));

                        PopWarning PopWarning = new PopWarning(context);
                        PopWarning.showPopupWindow(new View(context));

                    }

                    break;
                case R.id.warmwater://出温水
                    waterRecordType = 2;
                    if(null==comThread){
                        Toast.makeText(context,"通讯未启动",Toast.LENGTH_SHORT).show();
                    }else{
                        comThread.setActive(false);
                    }
                    if(warmwater.getText().toString().equals(context.getString(R.string.warmwater))){
                        //出温水

                        if(canOperateOrNot()){
                            toastUtils(1);
                            return;
                        }
                        chushuiflag = true;
                        String s = "出温水指令执行";
                        int sw = 1;
                        warmwater.setBackgroundResource(R.drawable.hotwaterstop);
                        try{
                        if (devUtil.do_ioWater(1, sw) == 0) {
                            Toast.makeText(context, s + "成功", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(context, s + "失败", Toast.LENGTH_SHORT).show();
                        }}catch (Exception e){
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        finally {
                            comThread.setActive(true);
                        }
                        warmwater.setText(context.getString(R.string.stopgetwater));
                    }else{
                        warmwater.setBackgroundResource(R.drawable.wenshui);
                        String s = "出温水停止指令执行";
                        int sw = 2;
                        try{
                        if (devUtil.do_ioWater(2, sw) == 0) {
                            Toast.makeText(context, s + "成功", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(context, s + "失败", Toast.LENGTH_SHORT).show();
                        }}
                        catch (Exception e){
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        finally {
                            comThread.setActive(true);
                        }
                        warmwater.setText(context.getString(R.string.warmwater));
                        chushuiflag = false;

                    }
                    break;

                    //调用冷水
                case R.id.coolwater:
                    waterRecordType = 3;
                    if(null==comThread){
                        Toast.makeText(context,"通讯未启动",Toast.LENGTH_SHORT).show();
                    }else{
                        comThread.setActive(false);
                    }

                    if(coolwater.getText().toString().equals(context.getString(R.string.coolwater))){
                        //取水
                        if(canOperateOrNot()){
                            toastUtils(1);
                            return;
                        }
                        chushuiflag = true;
                        String s = "出冷水";
                        int sw = 1;
                        coolwater.setBackgroundResource(R.drawable.hotwaterstop);
                        try{
                        if (devUtil.do_ioWater(3, sw) == 0) {
                            Toast.makeText(context, s + "成功", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(context, s + "失败", Toast.LENGTH_SHORT).show();
                        }}catch (Exception e){
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        finally {
                            comThread.setActive(true);
                        }
                        coolwater.setText(context.getString(R.string.stopgetwater));



                    }

                    else{
                        String s = "停止出水";
                        int sw = 2;
                        coolwater.setBackgroundResource(R.drawable.coolwater);
                        try{
                        if (devUtil.do_ioWater(3, sw) == 0) {
                            Toast.makeText(context, s + "成功", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(context, s + "失败", Toast.LENGTH_SHORT).show();
                        }}catch (Exception e){
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        finally {
                            comThread.setActive(true);
                        }
                        coolwater.setText(context.getString(R.string.coolwater));

                        chushuiflag = false;
                    }
                    break;
                case R.id.getcup:

                    if(null==comThread){
                        Toast.makeText(context,"通讯未启动",Toast.LENGTH_SHORT).show();
                    }else{
                        comThread.setActive(false);
                    }
                    String s = "出杯子";
                    if(getcup.getText().toString().equals(context.getString(R.string.getcup))){
                        //出杯子
                        getcup.setBackgroundResource(R.drawable.hotwaterstop);
                        try{
                        if (devUtil.do_ioCup() == 0) {
                            getcup.setText(context.getString(R.string.getcup));
                            Toast.makeText(context, s + "成功", Toast.LENGTH_SHORT).show();
                            getcup.setBackgroundResource(R.drawable.getcup);
                        } else{
                            getcup.setText(context.getString(R.string.getcup));
                            Toast.makeText(context, s + "失败", Toast.LENGTH_SHORT).show();
                            getcup.setBackgroundResource(R.drawable.getcup);
                        }}catch (Exception e){
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        finally {
                            comThread.setActive(true);
                        }
                        if(canOperateOrNot()){
                            toastUtils(1);
                        }

                    }



                    break;
            }

            waterSaleRecordAO.setWaterRecordType(waterRecordType);
            //售水中是否缺纸杯
            int nocupflag = devUtil.get_run_bCup_value();
            if(01==nocupflag){
                waterSaleRecordAO.setWaterRecordIsCup(1);
            }else{
                //缺纸杯
                waterSaleRecordAO.setWaterRecordIsCup(0);
            }

//            获取售水量
            waterSaleRecordAO.setWaterFlow(devUtil.get_run_waterFlow_value());
            //设置纸杯数量
            waterSaleRecordAO.setWaterRecordCupNum(2);

            if(chushuiflag == false){
                //所有的设置为不可用

                //上传售水记录
                String salewater = RestUtils.getUrl(UriConstant.WATERSALE)+"?userId="+waterSaleRecordAO.getUserId()+"&deviceId="+waterSaleRecordAO.getDeviceId()+
                        "&productChargMode="+waterSaleRecordAO.getProductChargMode()+"&waterRecordType="+waterSaleRecordAO.getWaterRecordType()+
                        "&waterRecordIsCup="+waterSaleRecordAO.getWaterRecordIsCup()+"&waterFlow="+waterSaleRecordAO.getWaterFlow()+
                        "&waterRecordCupNum="+waterSaleRecordAO.getWaterRecordCupNum();
                String salewaterString = JSON.toJSONString(waterSaleRecordAO);
                Log.e("salewaterString","salewaterString"+salewaterString);
                OkHttpUtils.getAsyn(salewater, new OkHttpUtils.StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.e("responsea","response"+request.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                                Log.e("response","response"+response);
                    }
                });



            }else{
                Log.e("没有操作","没有操作");
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