package newwater.com.newwater.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2018/5/25 0025.
 */
@Table(name = "Sys_Device_Water_Quality")
public class Sys_Device_Water_Quality {
    @Column(name = "device_water_quality_id")
    private int device_water_quality_id;//自己增长
    @Column(name = "device_id")
    private int device_id;//设备ID
    @Column(name = "device_raw_water")
    private double device_raw_water;//原水水质
    @Column(name = "device_pure_water")
    private double device_pure_water;//纯水水质
    @Column(name = "device_water_quality_time")
    private String device_water_quality_time;//检测时间

    public int getDevice_water_quality_id() {
        return device_water_quality_id;
    }

    public void setDevice_water_quality_id(int device_water_quality_id) {
        this.device_water_quality_id = device_water_quality_id;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public double getDevice_raw_water() {
        return device_raw_water;
    }

    public void setDevice_raw_water(double device_raw_water) {
        this.device_raw_water = device_raw_water;
    }

    public double getDevice_pure_water() {
        return device_pure_water;
    }

    public void setDevice_pure_water(double device_pure_water) {
        this.device_pure_water = device_pure_water;
    }

    public String getDevice_water_quality_time() {
        return device_water_quality_time;
    }

    public void setDevice_water_quality_time(String device_water_quality_time) {
        this.device_water_quality_time = device_water_quality_time;
    }

    @Override
    public String toString() {
        return "Sys_Device_Water_Quality [device_water_quality_id=" + device_water_quality_id + ", device_id=" + device_id + ", device_raw_water=" + device_raw_water + ", device_pure_water=" + device_pure_water +
                ",device_water_quality_time="+device_water_quality_time+
                "]";
    }
}
