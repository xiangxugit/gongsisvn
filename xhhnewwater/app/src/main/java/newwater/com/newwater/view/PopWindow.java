package newwater.com.newwater.view;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import newwater.com.newwater.MainActivity;
import newwater.com.newwater.R;
import newwater.com.newwater.TestJSON;
import newwater.com.newwater.utils.Create2QR2;
import newwater.com.newwater.utils.TimeBack;
import newwater.com.newwater.utils.TimeUtils;

/**
 * 自定义的PopupWindow
 */
public class PopWindow extends PopupWindow {
    private LinearLayout freegetWater;
    private LinearLayout paygetWater;
    private Activity context;
    TextView  timebacktextView;
    public PopWindow(Activity context) {
        this.context = context;
        // 通过layout的id找到布局View
        View contentView = LayoutInflater.from(context).inflate(R.layout.free_pay_pop, null);

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
        freegetWater = (LinearLayout) contentView.findViewById(R.id.leftpop);
        freegetWater.setOnClickListener(onclick);
        paygetWater = (LinearLayout) contentView.findViewById(R.id.rightpop);
        paygetWater.setOnClickListener(onclick);
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
                case R.id.leftpop:
                    dismiss();
                    Pop_TimeBack timebackpop = new Pop_TimeBack(context);
                    timebackpop.showPopupWindow(new View(context));
                    timebacktextView = Pop_TimeBack.seeaddtimeback;
//                    TimeBack timeback = new TimeBack(timebacktextView,6000,1000);
                    TimeCount timecount =  new TimeCount(6000, 1000);
                    timecount.start();
                    break;
                case R.id.rightpop:


                    PopWindowChooseWaterGetWay popChooseWatera = new PopWindowChooseWaterGetWay(context);
                    popChooseWatera.showPopupWindow(new View(context));

                    ImageView qcode = PopWindowChooseWaterGetWay.qrcode;
                    String qcodestring = TestJSON.getWeiXinQcode();
                    Bitmap qcodebitmap = Create2QR2.createBitmap(qcodestring);
                    qcode.setImageBitmap(qcodebitmap);

                    TextView rightText = PopWindowChooseWaterGetWay.getwater;
                    rightText.setText("扫码关注，完成用户绑定");

                    //获取imageView设置二维码 改变textView的文字



//                    Pop_LeftOperate leftpop = new Pop_LeftOperate(context);
//                    leftpop.showPopupWindow(new View(context));
//
//                    Pop_RightOperate rightPop = new Pop_RightOperate(context);
//                    rightPop.showPopupWindow(new View(context));


                    break;
            }
        }

    };


    public class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub

        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
            timebacktextView.setClickable(false);
            timebacktextView.setText(millisUntilFinished/1000+"秒");
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            timebacktextView.setText("");
            Pop_LeftOperate leftpop = new Pop_LeftOperate(context);
            leftpop.showPopupWindow(new View(context));

            Pop_RightOperate rightPop = new Pop_RightOperate(context);
            rightPop.showPopupWindow(new View(context));
            timebacktextView.setClickable(true);
        }




    }

}