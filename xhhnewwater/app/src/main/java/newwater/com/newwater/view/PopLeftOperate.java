package newwater.com.newwater.view;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import newwater.com.newwater.R;
import newwater.com.newwater.beans.ViewShow;
import newwater.com.newwater.broadcast.UpdateBroadcast;
import newwater.com.newwater.interfaces.OnUpdateUI;
import newwater.com.newwater.view.activity.BaseActivity;

/**
 * 自定义的PopupWindow
 */
public class PopLeftOperate extends PopupWindow {
    public UpdateBroadcast myBroadcast;
    public static final String FLAG = "UPDATE";
    private TextView hotwatertext;

    private TextView coolwatertext;
    private TextView ppmvalue;
    private TextView ppm;//下方的
    private Button exit;

    private ImageView hotico;//是否加热的imageview
    private TextView hotornot;//是否加热text

    private ImageView coolico;//是否制冷的imageView
    private TextView cooltext;//是否制冷text

    private ImageView zhishuiico;//是否制水的imageView
    private TextView zhishuitext;//是佛止水的text

    private ImageView chongxiimage;//冲洗imageView
    private TextView  chongxitext;//冲洗text;
    private BaseActivity context;

    public PopLeftOperate(final BaseActivity context) {
        this.context = context;
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
        coolwatertext = (TextView)contentView.findViewById(R.id.coolwatertext);
        ppmvalue = (TextView)contentView.findViewById(R.id.ppmvalue);
        ppm = (TextView)contentView.findViewById(R.id.ppm);

        hotico = (ImageView) contentView.findViewById(R.id.hotico);
        hotornot = (TextView)contentView.findViewById(R.id.hotornot);
        coolico = (ImageView) contentView.findViewById(R.id.coolico);
        cooltext = (TextView)contentView.findViewById(R.id.cooltext);
        zhishuiico = (ImageView) contentView.findViewById(R.id.zhishuiico);
        zhishuitext =(TextView) contentView.findViewById(R.id.zhishuitext);
        chongxiimage = (ImageView)contentView.findViewById(R.id.chongxiimage);
        chongxitext = (TextView)contentView.findViewById(R.id.chongxitext);
        exit = (Button)contentView.findViewById(R.id.exit);
        exit.setOnClickListener(onclick);

        myBroadcast.SetOnUpdateUI(new OnUpdateUI() {
            @Override
            public void updateUI(ViewShow data ) {
                Toast.makeText(context,data.toString(),Toast.LENGTH_LONG).show();

                hotwatertext.setText(data.getHotwatertextvalue());
                coolwatertext.setText(data.getCoolwatertextvalue());
                ppmvalue.setText(data.getPpmvalue());
                ppm.setText(data.getPpm());
                hotornot.setText(data.getHotornot());
                cooltext.setText(data.getCooltext());
                zhishuitext.setText(data.getZhishuitext());
                chongxitext.setText(data.getChongxitext());

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
//            this.dismiss();
        }
    }


    View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            switch (v.getId()) {
                case R.id.exit:
//                    context.dismissPop();
                    dismiss();
                    break;

            }
        }

    };
}