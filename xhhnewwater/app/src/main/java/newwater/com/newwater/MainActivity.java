package newwater.com.newwater;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.serialport.ComUtil;
import android.serialport.DevUtil;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import org.xutils.ex.DbException;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import newwater.com.newwater.Processpreserving.DaemonService;
import newwater.com.newwater.Processpreserving.PollingUtils;
import newwater.com.newwater.Processpreserving.Service1;
import newwater.com.newwater.adapter.VideoAdapter;
import newwater.com.newwater.beans.DeiviceParams;
import newwater.com.newwater.beans.person;
import newwater.com.newwater.broadcast.UpdateBroadcast;
import newwater.com.newwater.interfaces.OnUpdateUI;
import newwater.com.newwater.utils.TimeBack;
import newwater.com.newwater.utils.TimeRun;
import newwater.com.newwater.utils.VideoUtils;
import newwater.com.newwater.view.CustomerVideoView;
import newwater.com.newwater.view.PopWindow;
import newwater.com.newwater.view.PopWindowChooseWaterGetWay;
import newwater.com.newwater.view.Pop_LeftOperate;
import newwater.com.newwater.view.Pop_WantWater;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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
    private CustomerVideoView videoplay;


    //viewpager 视频
    private ViewPager viewpager;

    private Timer timer;//定时器，用于实现轮播

    static int pos = 0;
    private HttpProxyCacheServer proxy;
    private final static  int DATADELETE = 2;

    Pop_WantWater pop_wantWater = null;

    //付工那边的代码start

    private final int PollTime = 800;//轮询get_ioRunData()时间间隔ms


    private DevUtil devUtil=null;

    public static final String FLAG = "UPDATE";

    private UpdateBroadcast myBroadcast;
    //左边操作窗口的组件

    private TextView hotwatertext;
    private TextView coolwatertext;
    private TextView ppmvalue;
    private TextView ppm;//下方的

    //四个使能按钮

    private LinearLayout tobehot;
    private LinearLayout tobecool;
    private LinearLayout zhishui;
    private LinearLayout chongxi;
    private Button exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initbroadcast();
    }


    public void initbroadcast(){
        myBroadcast = new UpdateBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FLAG);
        registerReceiver(myBroadcast, intentFilter);


        myBroadcast.SetOnUpdateUI(new OnUpdateUI() {
            @Override
            public void updateUI(String i) {
//                tip.setText(i);
                hotwatertext.setText(i);
                coolwatertext.setText(i);
                ppmvalue.setText(i);
                ppm.setText(i);
//                Toast.makeText(MainActivity.this,"aaa"+i,Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void initView() {
        root = (RelativeLayout) findViewById(R.id.root);

        videoplay = (CustomerVideoView) findViewById(R.id.playvideo);
        videoplay.setZOrderOnTop(true);
        //视频播放
        proxy = App.getProxy(MainActivity.this);
        // 获取当前设备是哪一种模式
        int model = TestJSON.getModel();
        if (model == 0) {
            //售水模式
        }

        //视频播放
        playVideo();
        //监控设备
//        listenDevice();

        //缓存视频 https://blog.csdn.net/qingwenje2008/article/details/76186727
        //监控设备
//        startService(new Intent(MainActivity.this, Service1.class));

        startService(new Intent(MainActivity.this, DaemonService.class));
//        String ACTION = "com.ryantang.service.PollingService";
//        PollingUtils.startPollingService(this, 5, Service1.class, ACTION);
          //下载视频
//        VideoUtils videodownload = new VideoUtils(MainActivity.this);
//        videodownload.downloadvideo();
         //左边的操作界面组件获得

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.left_pop, null);

        hotwatertext = view.findViewById(R.id.hotwatertext);
        coolwatertext = view.findViewById(R.id.coolwatertext);
        ppmvalue = view.findViewById(R.id.ppmvalue);
        ppm = view.findViewById(R.id.ppm);

        tobehot = view.findViewById(R.id.tobehot);
        tobecool = view.findViewById(R.id.tobecool);
        zhishui = view.findViewById(R.id.zhishui);
        chongxi = view.findViewById(R.id.chongxi);
        exit = view.findViewById(R.id.exit);

        tobehot.setOnClickListener(this);
        tobecool.setOnClickListener(this);
        zhishui.setOnClickListener(this);
        chongxi.setOnClickListener(this);
        exit.setOnClickListener(this);

//        lv_group = (ListView) view.findViewById(R.id.lvGroup);




    }

    //付工begin




    //付工end

    /**
     * 这个函数在Activity创建完成之后会调用。购物车悬浮窗需要依附在Activity上，如果Activity还没有完全建好就去
     * 调用showCartFloatView()，则会抛出异常
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(null==popChooseWater){
            Pop_WantWater pop_wantWater = new Pop_WantWater(MainActivity.this);
            pop_wantWater.showPopupWindow(new View(this));
        }else{
            Pop_WantWater pop_wantWater = new Pop_WantWater(MainActivity.this);
            pop_wantWater.showPopupWindow(new View(this));
        }

    }

        public void playVideo(){

          //是否需要播放的资源
        final int maxloop;
        String testa = TestJSON.strategy();
        JSONArray alldata = JSON.parseArray(testa);
        String test = alldata.getString(0);
        com.alibaba.fastjson.JSONObject testobj = JSON.parseObject(test);
        String videoListString = testobj.getString("videoList");
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
                pos = pos+1;
                if(pos==maxloop){
                    pos =0;
                    String proxyUrl = proxy.getProxyUrl(videolist.getString(pos));
                    videoplay.setVideoPath(proxyUrl);
                    videoplay.start();
                }else{
                    videoplay.setVideoPath(videolist.getString(pos));
                    videoplay.start();
                }
            }
        });


            //视频播放的错误处
            videoplay.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {

                    if(what==MediaPlayer.MEDIA_ERROR_SERVER_DIED){
                        //媒体服务器挂掉了。此时，程序必须释放MediaPlayer 对象，并重新new 一个新的。
                        Toast.makeText(MainActivity.this,"网络服务错误",Toast.LENGTH_LONG).show();
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



//        popChooseWater = new PopWindow(MainActivity.this);
//        contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.free_pay_pop, null);

//        https://blog.csdn.net/qq_35952946/article/details/78863871  串口通信


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exit:
                TimeBack timeback = new TimeBack(exit,30000,1000);
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
                popChooseWater.showPopupWindow(new View(MainActivity.this));
                break;
            case R.id.tobehot:
                //加热使能
                break;

            case R.id.tobecool:
                //制冷使能
                break;
            case R.id.chongxi:

                //冲洗使能
                break;

        }

    }


    public void listenDevice(){
        //https://www.jianshu.com/p/7d3ff0a11ab8  数据库操作

        //闹钟定时播放视频

        // TODO 此处应该是读取本地的配置，本地的配置应该是在服务器获取的
        //开机器隔一段时间去监听查询


        Time t=new Time();
        t.setToNow();
        int hout = t.hour;
        int minute = t.minute;
        int second = t.second;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hout); //凌晨1点
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        Date date=calendar.getTime(); //第一次执行定时任务的时间

        //        TimeRun timeRun = new TimeRun();
        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
       /* if(date.before(new Date())){
            date = this.addDay(date, 1);
        }*/
        Timer timer = new Timer();
        final MyHandler myHandler = new MyHandler();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                Message msg = new Message();
                msg.what =0;
                myHandler.sendMessage(msg);

                 //获取设备参数
//                String quality = "好";
//                DeiviceParams deviceparams = new DeiviceParams();
//                deviceparams.setWaterquality(quality);
                person p  = new person();
                p.setName("好");
                try {
                    App.getdb().save(p);
                } catch (DbException e) {
                    e.printStackTrace();
                }

            }
        };
        final long PERIOD_DAY =20 * 1000;
        timer.schedule(task,date,PERIOD_DAY);


        Time tupload=new Time();
        tupload.setToNow();
        int houtupload = t.hour;
        int minuteupload = t.minute+1;
        int seconduplod = t.second;

        if(houtupload>24){
            houtupload = 0;
        }
        if(minuteupload>60){
            minuteupload = 0;
        }
        //获取数据库的数据在特定的时候上传到服务端并且删除数据库里面的内容
        Date updatetime = TimeRun.tasktime(houtupload,minuteupload,seconduplod);
//        if(updatetime.before(new Date())){
//            updatetime = this.addDay(updatetime, 1);
//        }
        TimeRun  timeRun = new TimeRun(MainActivity.this,updatetime,myHandler,60*60*24*1000,DATADELETE);





    }

    public Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

public class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case 0:
                    Toast.makeText(MainActivity.this,"定时检测水质",Toast.LENGTH_SHORT).show();


                    break;
            }

        }
    }


}
