package newwater.com.newwater.utils;

import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class TimeBack {
    //timecount
    private TimeCount timecount;
    private TextView showTextView;
    private long millissInFuture;
    private long countDownInterval;
    public TimeBack(TextView showTextView,long millisInFuture,long countDownInterval)
    {
        this.showTextView = showTextView;
        this.timecount =  new TimeCount(millisInFuture, countDownInterval);
        timecount.start();
		/*showTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});*/
    }
    public class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub

        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
            showTextView.setClickable(false);
            showTextView.setText(millisUntilFinished/1000+"秒");
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            showTextView.setText("");

            showTextView.setClickable(true);
        }

    }
}
