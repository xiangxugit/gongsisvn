package newwater.com.newwater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.constants.ErrCodeConstant;

public class BreakDownActivity extends Activity {

    private TextView tvCode;
    private int errCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();

    }

    private void initData() {
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                errCode = bundle.getInt(Constant.KEY_BREAK_DOWN_ERRCODE, ErrCodeConstant.ERR_DEFAUTL);
            }
        }
    }

    private void initView() {
        setContentView(R.layout.activity_break_down);
        tvCode = findViewById(R.id.break_down_code_tv);
        tvCode.setText(errCode + "");
    }
}
