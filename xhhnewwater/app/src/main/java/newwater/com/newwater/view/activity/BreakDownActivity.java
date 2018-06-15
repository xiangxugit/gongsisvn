package newwater.com.newwater.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import newwater.com.newwater.R;
import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.utils.BaseSharedPreferences;

public class BreakDownActivity extends Activity {

    private static final String TAG = "BreakDownActivity";
    private TextView tvReason;
    private TextView tvContract;
    private Context mContext;
    private String errReason;
    private String contractInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: 设备已坏，无法返回！");
    }

    private void initData() {
        mContext = BreakDownActivity.this;
        contractInfo = BaseSharedPreferences.getString(mContext, Constant.CONTRACT_INFO_KEY);
        if (TextUtils.isEmpty(contractInfo)) {
            contractInfo = "维护人员";
        }
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                errReason = bundle.getString(Constant.KEY_BREAK_DOWN_ERR_REASON, getString(R.string.break_down_reason_default));
            }
        }
    }

    private void initView() {
        setContentView(R.layout.activity_break_down);
        tvReason = findViewById(R.id.break_down_reason_tv);
        tvContract = findViewById(R.id.break_down_contract_info_tv);
        tvReason.setText(errReason);
        tvContract.setText(contractInfo);
    }
}
