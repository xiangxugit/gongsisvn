package newwater.com.newwater.view;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import newwater.com.newwater.MainActivity;
import newwater.com.newwater.R;

/**
 * 自定义的PopupWindow
 */
public class PopWindowChooseWaterGetWay extends PopupWindow {
    private Activity context;
    public static ImageView qrcode;
    public  static TextView getwater;

    // 弹窗子类项选中时的监听
    private OnItemOnClickListener mItemOnClickListener;

    public PopWindowChooseWaterGetWay(Activity context) {
        this.context = context;
        // 通过layout的id找到布局View
        View contentView = LayoutInflater.from(context).inflate(R.layout.scan_qrcode, null);
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
        this.setTouchable(true);
        // 刷新状态
        this.update();
        // 设置PopupWindow的背景颜色为半透明的黑色
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#66000000"));
        this.setBackgroundDrawable(dw);
        // 设置PopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopWindowAnimStyle);

        // 这里也可以从contentView中获取到控件，并为它们绑定控件
        qrcode = (ImageView) contentView.findViewById(R.id.qrcode);
        qrcode.setOnClickListener(onclick);

        getwater = (TextView) contentView.findViewById(R.id.getwater);


    }

    // 显示PopupWindow，有两种方法：showAsDropDown、showAtLocation
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // showAsDropDown方法，在parent下方的(x,y)位置显示，x、y是第二和第三个参数
            // this.showAsDropDown(parent, parent.getWidth() / 2 - 400, 18);

            // showAtLocation方法，在parent的某个位置参数，具体哪个位置由后三个参数决定
            this.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else {
            Toast.makeText(context, "二维码消失，显示主界面", Toast.LENGTH_SHORT).show();
            this.dismiss();
        }
    }


    public void setItemOnClickListener(
            OnItemOnClickListener onItemOnClickListener) {
        this.mItemOnClickListener = onItemOnClickListener;
    }

    public static interface OnItemOnClickListener {
        public void onItemClick();
    }

    View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            switch (v.getId()) {
                case R.id.qrcode:
                    MainActivity.leftoperate.setVisibility(View.VISIBLE);
                    MainActivity.rightoperate.setVisibility(View.VISIBLE);
//                    MainActivity.wantwater.setVisibility(View.GONE);
                    break;

            }
        }

    };




}