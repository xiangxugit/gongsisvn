package newwater.com.newwater.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2018/5/25 0025.
 */
@Table(name = "Sys_Device")
public class Sys_Device {
    @Column(name = "device_id")
    private int device_id;
    @Column(name = "work_id")
    private int work_id;
    @Column(name = "order_id")
    private int order_id;
    @Column(name = "device_identification")
    private String device_identification;
    @Column(name = "device_number")
    private String device_number;//设备编号（激活时上报或自动生成）
    @Column(name = "device_location")
    private String device_location;
    @Column(name = "device_install_time")
    private String device_install_time;
    @Column(name = "device_status")
    private String device_status;

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getWork_id() {
        return work_id;
    }

    public void setWork_id(int work_id) {
        this.work_id = work_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getDevice_identification() {
        return device_identification;
    }

    public void setDevice_identification(String device_identification) {
        this.device_identification = device_identification;
    }

    public String getDevice_number() {
        return device_number;
    }

    public void setDevice_number(String device_number) {
        this.device_number = device_number;
    }

    public String getDevice_location() {
        return device_location;
    }

    public void setDevice_location(String device_location) {
        this.device_location = device_location;
    }

    public String getDevice_install_time() {
        return device_install_time;
    }

    public void setDevice_install_time(String device_install_time) {
        this.device_install_time = device_install_time;
    }

    public String getDevice_status() {
        return device_status;
    }

    public void setDevice_status(String device_status) {
        this.device_status = device_status;
    }

    @Override
    public String toString() {
        return "Sys_Device [device_id=" + device_id + ", work_id=" + work_id + ", order_id=" + order_id+",device_identification=" + device_identification +
                ",device_number=" +device_number+
                ",device_location="+device_location+
                ",device_install_time="+device_install_time+
                ",device_status="+device_status+
                 "]";
    }
}
