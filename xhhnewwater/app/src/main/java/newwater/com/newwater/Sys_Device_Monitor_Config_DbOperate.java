package newwater.com.newwater;

import android.content.Context;

import org.xutils.ex.DbException;

import java.util.Date;
import java.util.List;

import newwater.com.newwater.beans.SysDeviceMonitorConfig;
import newwater.com.newwater.utils.XutilsInit;

/**
 * Created by Administrator on 2018/5/30 0030.
 */

public class Sys_Device_Monitor_Config_DbOperate {

    private Context activity;
    private XutilsInit xutilsInit;

    public Sys_Device_Monitor_Config_DbOperate(Context activity) {
        this.activity = activity;
        xutilsInit = new XutilsInit(activity);
    }

    /**
     * 增加预计信息
     *
     * @param monitor_config
     * @throws DbException
     */
    public void add(SysDeviceMonitorConfig monitor_config) throws DbException {
        SysDeviceMonitorConfig deviceInitParams = new SysDeviceMonitorConfig();
        deviceInitParams.setDeviceId(123456);
        deviceInitParams.setMotCfgNetworkTime(30);//巡检时间
        deviceInitParams.setMotCfgNetworkTimes(3);//巡检次数
        deviceInitParams.setMotCfgPpTime(100);//pp棉使用时间100天
        deviceInitParams.setMotCfgPpFlow(5680);//pp棉使用流量
//        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        String date = sDateFormat.format(new java.util.Date());
        deviceInitParams.setMotCfgPpChangeTime(new Date());
        deviceInitParams.setMotCfgGrainCarbonTime(100);//活性炭使用时间
        deviceInitParams.setMotCfgGrainCarbonFlow(11355);//活性炭使用流量
        deviceInitParams.setMotCfgGrainCarbonChangeTime(new Date());
        deviceInitParams.setMotCfgGrainCarbonFlow(11355);
        deviceInitParams.setMotCfgPressCarbonChangeTime(new Date());
        deviceInitParams.setMotCfgPressCarbonTime(100);
        deviceInitParams.setMotCfgPoseCarbonChangeTime(new Date());
        deviceInitParams.setMotCfgPoseCarbonFlow(11355);
        deviceInitParams.setMotCfgPoseCarbonTime(100);
        deviceInitParams.setMotCfgRoTime(540);
        deviceInitParams.setMotCfgRoFlow(11355);
        deviceInitParams.setMotCfgRoChangeTime(new Date());
        deviceInitParams.setMotCfgUpTime(30);
        deviceInitParams.setMotCfgMaxFlow(30);//最大出水量30Ml

        xutilsInit.getDb().save(deviceInitParams);
    }

    /**
     * 查询预警信息
     *
     * @return
     * @throws DbException
     */
    public List<SysDeviceMonitorConfig> find() throws DbException {
        List<SysDeviceMonitorConfig> list = xutilsInit.getDb().findAll(SysDeviceMonitorConfig.class);
        return list;

    }


    public void update(SysDeviceMonitorConfig monitor_config) throws DbException {
//        List<SysDeviceMonitorConfig> list = App.getdb().findAll(SysDeviceMonitorConfig.class);
//        SysDeviceMonitorConfig user = list.get(0);
//        SysDeviceMonitorConfig.setName("更换姓名");
        xutilsInit.getDb().update(monitor_config);

    }

    public void delete() throws DbException {
        List<SysDeviceMonitorConfig> list = xutilsInit.getDb().findAll(SysDeviceMonitorConfig.class);
        xutilsInit.getDb().delete(list.get(0));
    }


}
