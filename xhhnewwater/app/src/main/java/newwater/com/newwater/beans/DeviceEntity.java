package newwater.com.newwater.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2018/05/30.
 */

@Table(name = "DeviceEntity")
public class DeviceEntity {

    @Column(name="id",isId=true,autoGen=true)
    private int id;
    //姓名
    @Column(name = "deviceId")
    private Integer deviceId;
    @Column(name = "hotTemp")
    private Integer coldTemp;

    public Integer getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getColdTemp() {
        return coldTemp;
    }

    public void setColdTemp(Integer coldTemp) {
        this.coldTemp = coldTemp;
    }
}
