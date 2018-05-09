package newwater.com.newwater;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.danikula.videocache.HttpProxyCacheServer;

import org.json.JSONObject;

import java.util.Timer;

import newwater.com.newwater.adapter.VideoAdapter;
import newwater.com.newwater.utils.TimeBack;
import newwater.com.newwater.view.CustomerVideoView;
import newwater.com.newwater.view.PopWindow;
import newwater.com.newwater.view.PopWindowChooseWaterGetWay;
import newwater.com.newwater.view.Pop_LeftOperate;
import newwater.com.newwater.view.Pop_WantWater;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView exit;
    private TextView dixieccup;//纸杯和我要饮水按钮
    private Boolean operateornot  = false;
    public static  LinearLayout leftoperate;//左边操作区域
    public static LinearLayout rightoperate;//右边操作区域

//    public static ImageView  wantwater;//我要饮水

    //popwindow操作
    private View contentView;
    private LinearLayout leftpop;
    private LinearLayout rightpop;


    //出杯子的提示
    private View outCupView;
    private RelativeLayout outcupleft;
    private RelativeLayout outcupright;

    private  PopWindow popChooseWater;
    private PopWindowChooseWaterGetWay popChooseWatera;

    private RelativeLayout root;
    private ImageView qrcode;

    //VideoView
    private VideoView videoplay;


    //viewpager 视频
    private ViewPager viewpager;

    private Timer timer;//定时器，用于实现轮播

    static int pos = 0;
    HttpProxyCacheServer proxy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView(){
        root =  (RelativeLayout) findViewById(R.id.root);

        videoplay = (VideoView) findViewById(R.id.playvideo);
        videoplay.setZOrderOnTop(true);
        //视频播放
        proxy = App.getProxy(MainActivity.this);
        // 获取当前设备是哪一种模式
        int   model = TestJSON.getModel();
        if(model == 0){
            //售水模式
        }


        playVideo();
    }



    /**
     * 这个函数在Activity创建完成之后会调用。购物车悬浮窗需要依附在Activity上，如果Activity还没有完全建好就去
     * 调用showCartFloatView()，则会抛出异常
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Pop_WantWater pop_wantWater = new Pop_WantWater(MainActivity.this);
        pop_wantWater.showPopupWindow(new View(this));
    }

        public void playVideo(){
          //是否需要播放的资源
        final int maxloop;
        String testa = TestJSON.strategy();
        Log.e("test","test"+testa);
        JSONArray alldata = JSON.parseArray(testa);

        String test = alldata.getString(0);
        com.alibaba.fastjson.JSONObject testobj = JSON.parseObject(test);
        String videoListString = testobj.getString("videoList");
        Log.e("videoListString","videoListString"+videoListString);
        final JSONArray videolist = JSON.parseArray(videoListString);
        //循环
        maxloop = videolist.size();
        String proxyUrl = proxy.getProxyUrl(videolist.getString(0));
        videoplay.setVideoPath(proxyUrl);
        videoplay.start();



             final int videoflag = 0;//标志播放次数
            videoplay.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                // TODO Auto-generated method stub
                pos = pos+1;
                Log.e("pos","pos"+pos);
                Log.e("maxloop","maxloop"+maxloop);

                if(pos==maxloop){
                    pos =0;
                    String proxyUrl = proxy.getProxyUrl(videolist.getString(pos));
                    videoplay.setVideoPath(proxyUrl);
                    videoplay.start();
                }else{
                    videoplay.setVideoPath(videolist.getString(pos));
                    videoplay.start();
                }
//                mPlayer.start();
//                mPlayer.setLooping(true);

            }
        });


            //视频播放的错误处
            videoplay.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {

                    if(what==MediaPlayer.MEDIA_ERROR_SERVER_DIED){
                        //媒体服务器挂掉了。此时，程序必须释放MediaPlayer 对象，并重新new 一个新的。
                        Toast.makeText(MainActivity.this,
                                "网络服务错误",
                                Toast.LENGTH_LONG).show();
                    }else if(what==MediaPlayer.MEDIA_ERROR_UNKNOWN){
                        if(extra==MediaPlayer.MEDIA_ERROR_IO){
                            //文件不存在或错误，或网络不可访问错误
                            Toast.makeText(MainActivity.this,
                                    "网络文件错误",
                                    Toast.LENGTH_LONG).show();
                        } else if(extra==MediaPlayer.MEDIA_ERROR_TIMED_OUT){
                            //超时
                            Toast.makeText(MainActivity.this,
                                    "网络超时",
                                    Toast.LENGTH_LONG).show();
                        }
                    }


                    videoplay.stopPlayback();//释放VideoView原来的MediaPlayer

                    videoplay.resume();//VideoView内部重新new MediaPlayer

                    videoplay.setVideoPath(videolist.getString(0));

                    videoplay.start();//播放


                    return false;
                }
            });



        popChooseWater = new PopWindow(MainActivity.this);
        contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.free_pay_pop, null);


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exit:
                //
                TimeBack timeback = new TimeBack(exit,30000,1000);
                break;

            case R.id.dixieccup:
                PopWindow popWindow = new PopWindow(MainActivity.this);
                popWindow.showPopupWindow(new View(MainActivity.this));
                break;

            case R.id.leftpop:
                //免费喝水跳转到广告，倒计时然后进入操作界面，隐藏操作界面
                leftoperate.setVisibility(View.GONE);
                rightoperate.setVisibility(View.GONE);
                break;

            case R.id.rightpop:
                //弹出二维码


                break;

            case R.id.wantwater:
//                if(false ==operateornot){
//                    operateornot = true;
//                    leftoperate.setVisibility(View.GONE);
//                    rightoperate.setVisibility(View.GONE);
//                }else{
//                    operateornot = false;
//                    leftoperate.setVisibility(View.VISIBLE);
//                    rightoperate.setVisibility(View.VISIBLE);
//                }

                popChooseWater.showPopupWindow(new View(MainActivity.this));
        }

    }
}
