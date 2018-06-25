package newwater.com.newwater.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.serialport.DevUtil;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import newwater.com.newwater.Sys_Device_Monitor_Config_DbOperate;
import newwater.com.newwater.TestJSON;
import newwater.com.newwater.beans.DeviceEntity;
import newwater.com.newwater.beans.SysDeviceNoticeAO;
import newwater.com.newwater.beans.SysDeviceWaterQualityAO;
import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.constants.UriConstant;
import newwater.com.newwater.view.activity.BreakDownActivity;
import okhttp3.Request;

/**
 * Created by Administrator on 2018/4/28 0028.
 */

public class TimeRun {

    private static final String TAG = "TimeRun";

    //    https://blog.csdn.net/qinde025/article/details/6828723
    public TimerTask task;
    private DevUtil devUtil = null;
    private Handler handler = null;

    private DbManager dbManager;
    private Date time;
    private long loopjiange = 1000 * 60 * 60 * 24;
    private Activity context;
    private Integer motCfgPpFlow;//PP棉制水总流量
    private Integer motCfgGrainCarbonFlow;//颗粒活性炭使用时间(单位L)
    private Integer motCfgPressCarbonFlow;//压缩活性炭
    private Integer motCfgPoseCarbonFlow;//后置活性炭
    private Integer motCfgRoFlow;//反渗透模

    private Integer motCfgPpFlowWarning;
    private Integer motCfgGrainCarbonFlowWarning;//颗粒活性炭使用时间(单位L)
    private Integer motCfgPressCarbonFlowWarning;//压缩活性炭
    private Integer motCfgPoseCarbonFlowWarning;//后置活性炭
    private Integer motCfgRoFlowWarning;//反渗透模

    /**
     * @param context
     * @param handler
     * @param loopjiange
     * @param what
     * @param operateflag 1:水质状态
     */

    public TimeRun(final Activity context, Date time, final Handler handler, final long loopjiange, final int what, final int operateflag) {
        if (null == devUtil) {
            devUtil = new DevUtil(null);
            motCfgPpFlow = BaseSharedPreferences.getInt(context, Constant.DEVICE_PP_FLOW_KEY);//PP棉制水总流量
            motCfgGrainCarbonFlow = BaseSharedPreferences.getInt(context, Constant.DEVICE_GRAIN_CARBON_KEY);
            motCfgPressCarbonFlow = BaseSharedPreferences.getInt(context, Constant.DEVICE_PRESS_CARBON_KEY);//压缩活性炭
            motCfgPoseCarbonFlow = BaseSharedPreferences.getInt(context, Constant.DEVICE_POSE_CARBON_KEY);//后置活性炭
            motCfgRoFlow = BaseSharedPreferences.getInt(context, Constant.DEVICE_RO_FLOW_KEY);//反渗透模

            motCfgPpFlowWarning = (int) (motCfgPpFlow * Constant.PERCENT);
            motCfgGrainCarbonFlowWarning = (int) (motCfgGrainCarbonFlow * Constant.PERCENT);
            motCfgPressCarbonFlowWarning = (int) (motCfgPressCarbonFlow * Constant.PERCENT);
            motCfgPoseCarbonFlowWarning = (int) (motCfgPoseCarbonFlow * Constant.PERCENT);
            motCfgRoFlowWarning = (int) (motCfgRoFlow * Constant.PERCENT);
        }
        this.time = time;
        this.loopjiange = loopjiange;
        this.context = context;

        dbManager = new XutilsInit(context).getDb();
        this.task = new TimerTask() {
            @Override
            public void run() {
                if (Constant.TIME_OPERATE_UPDATEWATER == operateflag) {
                    //上传水质
                    SysDeviceWaterQualityAO sysDeviceWaterQualityAO = new SysDeviceWaterQualityAO();
                    String deviceId = "";
                    if (Constant.TEST == true) {
                        deviceId = "1";
                    } else {
                        deviceId = BaseSharedPreferences.getString(context, Constant.DEVICE_ID_KEY);
                    }

                    sysDeviceWaterQualityAO.setDeviceRawWater(devUtil.get_run_sTDS_value());
                    sysDeviceWaterQualityAO.setDevicePureWater(devUtil.get_run_sTDS_value());
                    sysDeviceWaterQualityAO.setDeviceWaterQualityTime(TimeUtils.getCurrentTime());
                    sysDeviceWaterQualityAO.setHotTemp(devUtil.get_run_hotTemp_value());
                    sysDeviceWaterQualityAO.setColdTemp(devUtil.get_run_coolTemp_value());
                    sysDeviceWaterQualityAO.setHeatingStatus(devUtil.get_run_bHot_value());
                    sysDeviceWaterQualityAO.setCoolingStatus(devUtil.get_run_bCool_value());
                    sysDeviceWaterQualityAO.setWaterPurificationStatus(devUtil.get_run_bWater_value());
                    sysDeviceWaterQualityAO.setFlushStatus(devUtil.get_run_bRinse_value());
                    sysDeviceWaterQualityAO.setRawWaterStatus(devUtil.get_run_bFault_value());
                    sysDeviceWaterQualityAO.setWaterLeakageStatus(devUtil.get_run_bLeak_value());
                    sysDeviceWaterQualityAO.setSwitchStatus(devUtil.get_run_bSwitch_value());
                    sysDeviceWaterQualityAO.setWaterCupStatus(devUtil.get_run_bCup_value());
                    String hotoutornot = "";
                    if (devUtil.get_run_hotWaterSW_value() == true) {
                        sysDeviceWaterQualityAO.setHotWaterOutletStatus(1);
                    } else {
                        sysDeviceWaterQualityAO.setHotWaterOutletStatus(0);
                    }
                    if (devUtil.get_run_coolWaterSW_value() == true) {
                        sysDeviceWaterQualityAO.setColdWaterOutletStatus(1);
                    } else {
                        sysDeviceWaterQualityAO.setColdWaterOutletStatus(0);
                    }
                    //温水出水状态
                    if (devUtil.get_run_normalWaterSW_value() == true) {
                        sysDeviceWaterQualityAO.setWarmWaterOutletStatus(1);
                    } else {
                        sysDeviceWaterQualityAO.setWarmWaterOutletStatus(0);
                    }
                    //加热设备温度
                    sysDeviceWaterQualityAO.setHeatingTemp(devUtil.get_pam_hotTemp_value());
                    //制冷设备温度
                    sysDeviceWaterQualityAO.setCoolingTemp(devUtil.get_pam_coolTemp_value());
                    //单位是分钟
                    sysDeviceWaterQualityAO.setFlushInterval(devUtil.get_pam_rinseTimeLong_value());
                    sysDeviceWaterQualityAO.setFlushDuration(devUtil.get_pam_rinseInterval_value());
//                    DeviceEntity test = new DeviceEntity();
////                    test.setDevice_number("aaaa");
//                    test.setDeviceId(1);
//                    test.setColdTemp(10);
                    try {
//                        dbManager.dropTable(SysDeviceWaterQualityAO.class);
                        dbManager.save(sysDeviceWaterQualityAO);
//                        List<SysDeviceWaterQualityAO> deviceEntityList = dbManager.findAll(SysDeviceWaterQualityAO.class);

//                        LogUtils.e(TAG, "deviceentityList: "+ deviceEntityList.toString());
                    } catch (DbException e) {
                        e.printStackTrace();
                    }

//                    try {
////                        List<DeviceEntity> testlist = dbManager.findAll(DeviceEntity.class);
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }

                }

                if (Constant.TIME_OPETATE_UPDATESCODE == operateflag) {
                    String sCodeUrl = "";
                    int deviceId = BaseSharedPreferences.getInt(context, Constant.DEVICE_ID_KEY);
                    if (Constant.TEST == true) {
                        sCodeUrl = RestUtils.getUrl(UriConstant.GETTEMPQCODE) + "/1";
                    } else {
                        sCodeUrl = RestUtils.getUrl(UriConstant.GETTEMPQCODE) + deviceId;
                    }

                    OkHttpUtils.getAsyn(sCodeUrl, new OkHttpUtils.StringCallback() {
                        @Override
                        public void onFailure(int errCode, Request request, IOException e) {

                        }

                        @Override
                        public void onResponse(String response) {

                            JSONObject scodeobj = JSONObject.parseObject(response);
                            if (null == scodeobj) {
                                return;
                            }
                            if ("0".equals(scodeobj.getString("code"))) {
                                String data = scodeobj.getString("data");
                                BaseSharedPreferences.setString(context, Constant.SCODEKEY, data);
                            }
                        }
                    });
                }

                if (Constant.TIME_OPETATE_WARNING == operateflag) {
                    String deviceNoticeUrl = RestUtils.getUrl(UriConstant.NOTICEQUALITY);
                    String postdata = "";
                    boolean updateflag = false;
                    boolean operateflag = false;
                    SysDeviceNoticeAO sysDeviceNoticeAO = new SysDeviceNoticeAO();
                    sysDeviceNoticeAO.setDeviceId(Integer.parseInt(TestJSON.getDeviceid()));
                    if (DevUtil.ERR_TIMEOUT == 1) {
                        sysDeviceNoticeAO.setDeviceNoticeType(Constant.NOTICE_TYPE_NO_NETWORK);
                        sysDeviceNoticeAO.setDeviceNoticeLeve(Constant.NOTICE_LEVEL_BREAK_DOWN);
                        updateflag = true;
                        sysDeviceNoticeAO.setDeviceNoticeContent("断网");
                        sysDeviceNoticeAO.setDeviceNoticeSubject("设备维护");
                        breakDown();
                    }


                      //滤芯过了
//                    boolean filterflag = filterOver();
//                    if (false == filterflag) {
//                        sysDeviceNoticeAO.setDeviceNoticeType(Constant.NOTICE_TYPE_LESS_FILTER);
//                        sysDeviceNoticeAO.setDeviceNoticeLeve(Constant.NOTICE_LEVEL_ABNORMAL);
//                        sysDeviceNoticeAO.setDeviceNoticeContent("滤芯用完");
//                        sysDeviceNoticeAO.setDeviceNoticeSubject("设备维护");
//                        updateflag = true;
//
//                    }
//                    //滤芯是否用完
//                    boolean filterend = filterend();
//                    if (false == filterend) {
//                        sysDeviceNoticeAO.setDeviceNoticeType(Constant.NOTICE_TYPE_NO_FILTER);
//                        operateflag = true;
//                        updateflag = true;
//                        sysDeviceNoticeAO.setDeviceNoticeLeve(Constant.NOTICE_LEVEL_BREAK_DOWN);
//                        sysDeviceNoticeAO.setDeviceNoticeContent("滤芯是否用完");
//                        sysDeviceNoticeAO.setDeviceNoticeSubject("设备维护");
//                        breakDown();
//                    }
                    //水质是否异常
                    if (Constant.TDSERROR > devUtil.get_run_oTDS_value()) {
                        sysDeviceNoticeAO.setDeviceNoticeType(Constant.NOTICE_TYPE_WATER_QUALITY_UNUSUAL);
                        updateflag = true;
                        operateflag = true;
                        sysDeviceNoticeAO.setDeviceNoticeLeve(Constant.NOTICE_LEVEL_BREAK_DOWN);
                        sysDeviceNoticeAO.setDeviceNoticeContent("水质是否异常");
                        sysDeviceNoticeAO.setDeviceNoticeSubject("设备维护");
                        breakDown();

                    }
                    //纸杯不足
                    if (devUtil.get_run_bCup_value() == 0) {
                        sysDeviceNoticeAO.setDeviceNoticeType(Constant.NOTICE_TYPE_NO_CUP);
                        sysDeviceNoticeAO.setDeviceNoticeLeve(Constant.NOTICE_LEVEL_ABNORMAL);
                        updateflag = true;
                        sysDeviceNoticeAO.setDeviceNoticeContent("纸杯不足");
                        sysDeviceNoticeAO.setDeviceNoticeSubject("现场维护");
                    }
                    //耗水量异常
                    //漏电
                    if (devUtil.get_run_bLeak_value() == 02) {
                        sysDeviceNoticeAO.setDeviceNoticeType(Constant.NOTICE_TYPE_WATER_LEAK);
                        updateflag = true;
                        operateflag = true;
                        sysDeviceNoticeAO.setDeviceNoticeLeve(Constant.NOTICE_LEVEL_BREAK_DOWN);
                        sysDeviceNoticeAO.setDeviceNoticeContent("漏电");
                        sysDeviceNoticeAO.setDeviceNoticeSubject("设备维护");
                        breakDown();
                    }
                    //原水缺水
                    if (devUtil.get_run_bLeak_value() == 02) {
                        sysDeviceNoticeAO.setDeviceNoticeType(Constant.NOTICE_TYPE_ORIGIN_WATER_LACK);
                        updateflag = true;
                        operateflag = true;
                        sysDeviceNoticeAO.setDeviceNoticeLeve(Constant.NOTICE_LEVEL_BREAK_DOWN);
                        sysDeviceNoticeAO.setDeviceNoticeContent("原水缺水");
                        sysDeviceNoticeAO.setDeviceNoticeSubject("设备维护");
                        breakDown();
                    }
                    //警告的类型
                    String warningtime = TimeUtils.getCurrentTime();
                    sysDeviceNoticeAO.setDeviceNoticeTime(warningtime);
                    sysDeviceNoticeAO.setDeviceNoticeStatus(0);
                    sysDeviceNoticeAO.setAdminUserId(0);

                    try {
                        LogUtils.e(TAG,"执行水质保存");
//                        dbManager.dropTable(SysDeviceNoticeAO.class);
                        dbManager.save(sysDeviceNoticeAO);
//                        dbManager.save(test);
                    } catch (DbException e) {

                        e.printStackTrace();
                    }
                }
            }
        };
    }


    public void breakDown() {
//        ControllerUtils.operateDevice(3, false);
//        Intent it = new Intent(context, BreakDownActivity.class);
//        context.startActivity(it);


    }

    public void startTimer() {
        Timer timer = new Timer(true);
        timer.schedule(task, time, loopjiange);
    }

    ;


    public boolean filterOver() {
        Boolean filterOverflag = true;
        Sys_Device_Monitor_Config_DbOperate SysDeviceMonitorConfig_dbOperate = new Sys_Device_Monitor_Config_DbOperate(context);
        String[][] data = devUtil.toArray();
        if (motCfgPpFlow - Integer.parseInt(data[17][1]) < motCfgPpFlowWarning) {
            filterOverflag = false;
        }

        if (motCfgGrainCarbonFlow - Integer.parseInt(data[17][1]) < motCfgGrainCarbonFlowWarning) {
            filterOverflag = false;
        }

        if (motCfgPressCarbonFlow - Integer.parseInt(data[17][1]) < motCfgPressCarbonFlowWarning) {
            filterOverflag = false;
        }

        if (motCfgPoseCarbonFlow - Integer.parseInt(data[17][1]) < motCfgPoseCarbonFlowWarning) {
            filterOverflag = false;
        }

        if (motCfgRoFlow - Integer.parseInt(data[17][1]) < motCfgRoFlowWarning) {
            filterOverflag = false;
        }


//            if (monitor.getMotCfgRoFlow() - Integer.parseInt(data[17][1]) < Constant.MOT_CFG_RO_FLOW) {
//                monitor.setMotCfgRoFlow(monitor.getMotCfgRoFlow() - Integer.parseInt(data[17][1]));
//                SysDeviceMonitorConfig_dbOperate.update(monitor);
//                filterOverflag = false;
//            }
        return filterOverflag;
    }


    public boolean filterend() {

        Boolean filterend = false;
        Sys_Device_Monitor_Config_DbOperate SysDeviceMonitorConfig_dbOperate = new Sys_Device_Monitor_Config_DbOperate(context);
        String[][] data = devUtil.toArray();
        if (motCfgPpFlow - Integer.parseInt(data[17][1]) < 0) {
            filterend = true;
        }

        if (motCfgGrainCarbonFlow - Integer.parseInt(data[17][1]) < 0) {
            filterend = true;
        }

        if (motCfgPressCarbonFlow - Integer.parseInt(data[17][1]) < 0) {
            filterend = true;
        }

        if (motCfgRoFlow - Integer.parseInt(data[17][1]) < 0) {
            filterend = true;
        }


//            if (monitor.getMotCfgRoFlow() - Integer.parseInt(data[17][1]) < Constant.MOT_CFG_RO_FLOW) {
//                monitor.setMotCfgRoFlow(monitor.getMotCfgRoFlow() - Integer.parseInt(data[17][1]));
//                SysDeviceMonitorConfig_dbOperate.update(monitor);
//                filterOverflag = false;
//            }
        return filterend;
    }


    public void cancelTimer() {
        this.task.cancel();
    }

    //TODO 封装一个获取date的方法
    public static Date tasktime(int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour); //凌晨1点
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        Date date = calendar.getTime(); //第一次执行定时任务的时间
        return date;
    }


}
