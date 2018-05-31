package newwater.com.newwater.DataBaseUtils;

import android.app.Activity;
import android.content.Context;

import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;

import java.util.Date;
import java.util.List;

import newwater.com.newwater.App;
import newwater.com.newwater.beans.Sys_Device_Monitor_Config;

/**
 * Created by Administrator on 2018/5/30 0030.
 */

public class Sys_Device_Monitor_Config_DbOperate {

    private Context activity;
    private XutilsInit xutilsInit;
    public Sys_Device_Monitor_Config_DbOperate(Context activity){
        this.activity = activity;
         xutilsInit = new XutilsInit(activity);
    }
    /**
     * 增加预计信息
     * @param monitor_config
     * @throws DbException
     */
    public  void add(Sys_Device_Monitor_Config monitor_config) throws DbException {
        Sys_Device_Monitor_Config deviceInitParams = new Sys_Device_Monitor_Config();
        deviceInitParams.setDevice_id(123456);
        deviceInitParams.setMot_cfg_network_time(30);
        deviceInitParams.setMot_cfg_network_times(3);//巡检次数
        deviceInitParams.setMot_cfg_pp_time(100);//pp棉使用时间100天
        deviceInitParams.setMot_cfg_pp_flow(5680);//pp棉使用流量
//        SimpleDateFormat    sDateFormat    =   new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
//        String    date    =    sDateFormat.format(new    java.util.Date());
        deviceInitParams.setMot_cfg_pp_change_time(new Date());
        deviceInitParams.setMot_cfg_grain_carbon_time(100);//活性炭使用时间
        deviceInitParams.setMot_cfg_grain_carbon_flow(11355);//活性炭使用流量
        deviceInitParams.setMot_cfg_grain_carbon_change_time(new Date());
        deviceInitParams.setMot_cfg_press_carbon_change_time(0);
        deviceInitParams.setMot_cfg_press_carbon_time(100);
        deviceInitParams.setMot_cfg_grain_carbon_flow(11355);
        deviceInitParams.setMot_cfg_pose_carbon_change_time(0);
        deviceInitParams.setMot_cfg_pose_carbon_flow(11355);
        deviceInitParams.setMot_cfg_pose_carbon_time(100);
        deviceInitParams.setMot_cfg_ro_time(540);
        deviceInitParams.setMot_cfg_ro_flow(11355);
        deviceInitParams.setMot_cfg_ro_change_time(0);
        deviceInitParams.setMot_cfg_up_time(30);
        deviceInitParams.setMot_cfg_max_flow(30);//最大出水量30Ml

        xutilsInit.getDb().save(deviceInitParams);
    }

    /**
     * 查询预警信息
     * @return
     * @throws DbException
     */
    public List<Sys_Device_Monitor_Config> find() throws DbException {
        List<Sys_Device_Monitor_Config> list = xutilsInit.getDb().findAll(Sys_Device_Monitor_Config.class);
        return  list;

    }


    public void update(Sys_Device_Monitor_Config monitor_config) throws DbException {
//        List<Sys_Device_Monitor_Config> list = App.getdb().findAll(Sys_Device_Monitor_Config.class);
//        Sys_Device_Monitor_Config user = list.get(0);
//        Sys_Device_Monitor_Config.setName("更换姓名");
        xutilsInit.getDb().update(monitor_config);

    }
    public void delete() throws DbException {
        List<Sys_Device_Monitor_Config> list = xutilsInit.getDb().findAll(Sys_Device_Monitor_Config.class);
        xutilsInit.getDb().delete(list.get(0));
    }


}
