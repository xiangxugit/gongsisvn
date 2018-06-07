package newwater.com.newwater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.danikula.videocache.HttpProxyCacheServer;

import newwater.com.newwater.beans.DispenserCache;
import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.constants.UriConstant;
import newwater.com.newwater.manager.IjkManager;
import newwater.com.newwater.view.CircleTextProgressbar;

public class FreeAdActivity extends Activity implements IjkManager.PlayerStateListener, CircleTextProgressbar.OnCountdownProgressListener, View.OnClickListener {

    private static final String TAG = "FreeAdActivity";

    private CircleTextProgressbar cpbProgress;
    private Button btnQuit;
    private Context mContext;
    private IjkManager playerManager;
    private HttpProxyCacheServer proxy;
    private int playDuration;  // 秒


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        initCountDown();
        initVideo();
    }

    @Override
    protected void onDestroy() {
        if (null != playerManager) {
            playerManager.stop();
            playerManager.onDestroy();
            playerManager = null;
        }
        super.onDestroy();
    }

    private void initData() {
        mContext = FreeAdActivity.this;
        proxy = App.getProxy(mContext);
        playDuration = Constant.DEFAULT_FREE_AD_DURATION;
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = intent.getExtras();
            if (null != bundle && bundle.containsKey(Constant.FREE_AD_DURATION)) {
                playDuration = bundle.getInt(Constant.FREE_AD_DURATION);
            }
        }
    }

    private void initView() {
        setContentView(R.layout.activity_free_ad);
        cpbProgress = findViewById(R.id.free_ad_progress_cpb);
        btnQuit = findViewById(R.id.free_ad_quit_btn);
        cpbProgress.setOutLineColor(getResources().getColor(R.color.dark_gray));
        cpbProgress.setInCircleColor(getResources().getColor(R.color.light_gray));
        cpbProgress.setProgressColor(getResources().getColor(R.color.royalblue));
        cpbProgress.setProgressLineWidth(5);
        cpbProgress.setText(playDuration + "");
        cpbProgress.setVisibility(View.VISIBLE);
        cpbProgress.setCountdownProgressListener(1, this);
        btnQuit.setVisibility(View.GONE);
        btnQuit.setOnClickListener(this);
    }

    private void initCountDown() {
        CountDownTimer timer = new CountDownTimer(playDuration * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int leftSec = (int) (millisUntilFinished / 1000);
                Log.d(TAG, "onTick: left = " + millisUntilFinished);
                cpbProgress.setProgress((playDuration - leftSec) * 100 / playDuration);
                cpbProgress.setText(leftSec + "");
            }

            @Override
            public void onFinish() {
                cpbProgress.setVisibility(View.GONE);
                btnQuit.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void initVideo() {
        // 初始化播放器
        playerManager = new IjkManager(this, R.id.free_ad_video);
        playerManager.setFullScreenOnly(true);
        playerManager.setScaleType(IjkManager.SCALETYPE_FILLPARENT);
        playerManager.playInFullScreen(true);
        playerManager.setOnPlayerStateChangeListener(this);
//        String proxyUrl = proxy.getProxyUrl(DispenserCache.freeAdVideoList
//                .get(DispenserCache.freeAdIndex).getAdvs_video_localtion_path());
        String proxyUrl = UriConstant.APP_ROOT_PATH + UriConstant.VIDEO_DIR + "AnnMarieThomas_2011-480p.mp4";
        playerManager.play(proxyUrl);
    }

    // ------------ ijk 监听 start ------------
    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete: index = " + DispenserCache.freeAdIndex);
        DispenserCache.freeAdIndex ++;
        // TODO: 2018/6/5 0005 更新播放记录，定时向服务器发送数据
        String proxyUrl = proxy.getProxyUrl(DispenserCache.freeAdVideoList
                .get(DispenserCache.freeAdIndex).getAdvs_video_localtion_path());
        playerManager.play(proxyUrl);
    }

    @Override
    public void onError(int what, int extra) {
        Log.d(TAG, "onError: what = " + what + ", extra = " + extra);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onPlay() {

    }

    @Override
    public void onInfo(int what, int extra) {

    }
    // ------------ ijk 监听 end ------------

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onProgress(int what, int progress) {
        Log.d(TAG, "onProgress: what = " + what + ", progress = " + progress);
    }
}
