package newwater.com.newwater.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2018/05/30.
 */

@Table(name = "DeviceEntity")
public class DeviceEntity {
    //设备编号
    @Column(name = "device_number")
    private String device_number;

    public String getDevice_number() {
        return device_number;
    }
    public void setDevice_number(String device_number) {
        this.device_number = device_number;
    }

    @Override
    public String toString() {
        return "DeviceEntity{" +
                "device_number='" + device_number + '\'' +
                '}';
    }
}
