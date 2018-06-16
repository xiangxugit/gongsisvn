package newwater.com.newwater.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2018/05/30.
 */

@Table(name = "DeviceEntity")
public class DeviceEntity {
    //设备编号
    @Column(name = "device_number",isId = true,autoGen = true)
    private int device_number;
    @Column(name = "test")
    private String test;

    public int getDevice_number() {
        return device_number;
    }
    public void setDevice_number(int device_number) {
        this.device_number = device_number;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return "DeviceEntity [device_number=" + device_number +",test="+test+ "]";
    }

}
