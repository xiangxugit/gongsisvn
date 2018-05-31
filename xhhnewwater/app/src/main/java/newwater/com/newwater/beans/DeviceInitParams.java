package newwater.com.newwater.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2018/5/29 0029.
 */
@Table(name = "DeviceInitParams'")
public class DeviceInitParams {
        @Column(name = "id", isId = true, autoGen = true)
        public int id;
        @Column(name = "deviceid")
        private int deviceid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(int deviceid) {
        this.deviceid = deviceid;
    }


    @Override
    public String toString() {
        return "DeviceInitParams [id=" + id + ", deviceid=" + deviceid +"]";
    }

}
