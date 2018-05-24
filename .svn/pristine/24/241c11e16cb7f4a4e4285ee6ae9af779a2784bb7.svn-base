package newwater.com.newwater.view;
import android.app.Activity;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import newwater.com.newwater.R;
import newwater.com.newwater.broadcast.UpdateBroadcast;
import newwater.com.newwater.interfaces.OnUpdateUI;

/**
 * 自定义的PopupWindow
 */
public class Pop_LeftOperate extends PopupWindow {
    public UpdateBroadcast myBroadcast;
    public static final String FLAG = "UPDATE";
    private TextView hotwatertext;

    public Pop_LeftOperate(Activity context) {
        // 通过layout的id找到布局View
        View contentView = LayoutInflater.from(context).inflate(R.layout.left_pop, null);
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


        myBroadcast = new UpdateBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FLAG);
        context.registerReceiver(myBroadcast, intentFilter);
        // 这里也可以从contentView中获取到控件，并为它们绑定控件
        hotwatertext = (TextView) contentView.findViewById(R.id.hotwatertext);
        myBroadcast.SetOnUpdateUI(new OnUpdateUI() {
            @Override
            public void updateUI(String i) {
                hotwatertext.setText(i);
            }
        });

    }

    // 显示PopupWindow，有两种方法：showAsDropDown、showAtLocation
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // showAsDropDown方法，在parent下方的(x,y)位置显示，x、y是第二和第三个参数
            // this.showAsDropDown(parent, parent.getWidth() / 2 - 400, 18);

            // showAtLocation方法，在parent的某个位置参数，具体哪个位置由后三个参数决定
            this.showAtLocation(parent, Gravity.LEFT, 0, 0);
        } else {
            this.dismiss();
        }
    }
}