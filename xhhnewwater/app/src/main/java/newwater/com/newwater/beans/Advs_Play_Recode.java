package newwater.com.newwater.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2018/5/25 0025.
 */
@Table(name = "Advs_Play_Recode")
public class Advs_Play_Recode {
     @Column(name = "advs_id", isId = true, autoGen = true)
     private int  advs_id;
     @Column(name = "device_id")
     private int  device_id;
     @Column(name = "advs_play_time")
     private int advs_play_time;
     @Column(name = "advs_play_length_of_time")
     private int advs_play_length_of_time;
     @Column(name = "advs_play_device_charg_mode")
     private int advs_play_device_charg_mode;
     @Column(name = "advs_play_device_industry")
     private int advs_play_device_industry;
     @Column(name = "advs_play_scene")
     private int advs_play_scene;

    public int getAdvs_id() {
        return advs_id;
    }

    public void setAdvs_id(int advs_id) {
        this.advs_id = advs_id;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getAdvs_play_time() {
        return advs_play_time;
    }

    public void setAdvs_play_time(int advs_play_time) {
        this.advs_play_time = advs_play_time;
    }

    public int getAdvs_play_length_of_time() {
        return advs_play_length_of_time;
    }

    public void setAdvs_play_length_of_time(int advs_play_length_of_time) {
        this.advs_play_length_of_time = advs_play_length_of_time;
    }

    public int getAdvs_play_device_charg_mode() {
        return advs_play_device_charg_mode;
    }

    public void setAdvs_play_device_charg_mode(int advs_play_device_charg_mode) {
        this.advs_play_device_charg_mode = advs_play_device_charg_mode;
    }

    public int getAdvs_play_device_industry() {
        return advs_play_device_industry;
    }

    public void setAdvs_play_device_industry(int advs_play_device_industry) {
        this.advs_play_device_industry = advs_play_device_industry;
    }

    public int getAdvs_play_scene() {
        return advs_play_scene;
    }

    public void setAdvs_play_scene(int advs_play_scene) {
        this.advs_play_scene = advs_play_scene;
    }

    @Override
    public String toString() {
        return "Advs_Play_Recode [advs_id=" + advs_id + ", device_id=" + device_id + ", advs_play_time=" + advs_play_time + ", advs_play_length_of_time=" + advs_play_length_of_time +

                ",advs_play_device_charg_mode="+advs_play_device_charg_mode+
                ",advs_play_device_industry="+advs_play_device_industry+
                ",advs_play_scene="+advs_play_scene+"]";
    }


}
