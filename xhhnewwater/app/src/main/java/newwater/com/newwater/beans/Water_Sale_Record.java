package newwater.com.newwater.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2018/5/25 0025.
 */
@Table(name = "Water_Sale_Record")
public class Water_Sale_Record {
    @Column(name = "water_record_id", isId = true, autoGen = true)
    private int  water_record_id;
    @Column(name = "user_id")
    private int  user_id;
    @Column(name = "device_id")
    private int device_id;
    @Column(name = "department_id")
    private int  department_id;
    @Column(name = "product_charg_mode")
    private int product_charg_mode;
    @Column(name = "water_record_type")
    private int water_record_type;
    @Column(name = "water_record_is_cup")
    private int water_record_is_cup;
    @Column(name = "water_flow")
    private int water_flow;
    @Column(name = "water_record_cup_num")
    private int water_record_cup_num;
    @Column(name = "water_record_money")
     private double  water_record_money;
    @Column(name = "water_record_time")
    private String water_record_time;

    public int getWater_record_id() {
        return water_record_id;
    }

    public void setWater_record_id(int water_record_id) {
        this.water_record_id = water_record_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public int getProduct_charg_mode() {
        return product_charg_mode;
    }

    public void setProduct_charg_mode(int product_charg_mode) {
        this.product_charg_mode = product_charg_mode;
    }

    public int getWater_record_type() {
        return water_record_type;
    }

    public void setWater_record_type(int water_record_type) {
        this.water_record_type = water_record_type;
    }

    public int getWater_record_is_cup() {
        return water_record_is_cup;
    }

    public void setWater_record_is_cup(int water_record_is_cup) {
        this.water_record_is_cup = water_record_is_cup;
    }

    public int getWater_flow() {
        return water_flow;
    }

    public void setWater_flow(int water_flow) {
        this.water_flow = water_flow;
    }

    public int getWater_record_cup_num() {
        return water_record_cup_num;
    }

    public void setWater_record_cup_num(int water_record_cup_num) {
        this.water_record_cup_num = water_record_cup_num;
    }

    public double getWater_record_money() {
        return water_record_money;
    }

    public void setWater_record_money(double water_record_money) {
        this.water_record_money = water_record_money;
    }

    public String getWater_record_time() {
        return water_record_time;
    }

    public void setWater_record_time(String water_record_time) {
        this.water_record_time = water_record_time;
    }


    @Override
    public String toString() {
        return "Water_Sale_Record [water_record_id=" + water_record_id + ", user_id=" + user_id +", device_id=" + device_id + ", department_id=" + department_id
                +",product_charg_mode="+product_charg_mode+
                ",water_record_type="+water_record_type+
                ",water_record_is_cup="+water_record_is_cup+
                ",water_record_is_cup="+water_record_is_cup+
                ",water_record_cup_num="+water_record_cup_num+
                ",water_record_money="+water_record_money+
                ",water_record_time="+water_record_time+
                "]";
    }


}
