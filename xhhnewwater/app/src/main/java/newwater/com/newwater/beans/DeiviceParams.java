package newwater.com.newwater.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/10 0010.
 */
@Table(name = "DeiviceParams'")
public class DeiviceParams implements Serializable {
    //xutls规定必须要有一个无参的构造方法
    public DeiviceParams(){
        this.id = id;
        this.waterquality = waterquality;
    }

    @Column(name = "id", isId = true)
    private int id;
    //水质
    @Column(name = "waterquality")
    private String waterquality;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWaterquality() {
        return waterquality;
    }

    public void setWaterquality(String waterquality) {
        this.waterquality = waterquality;
    }

    @Override
    public String toString() {
        return "DeiviceParams[" +
                "id=" + id +
                ", waterquality='" + waterquality + '\'' +
                ']';
    }


}
