package newwater.com.newwater.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2018/5/25 0025.
 */
@Table(name = "Advs_Video")
public class Advs_Video {
    @Column(name = "advs_id", isId = true, autoGen = true)
    private int advs_id;
    @Column(name = "creat_admin_user_id")
    private int creat_admin_user_id;
    @Column(name = "pass_admin_user_id")
    private int pass_admin_user_id;
    @Column(name = "advs_industry")
    private int advs_industry;
    @Column(name = "advs_charg_mode")
    private int advs_charg_mode;
    @Column(name = "advs_is_point_device")
    private int advs_is_point_device;
    @Column(name = "advs_title")
    private String advs_title;
    @Column(name = "advs_video_download_path")
    private String advs_video_download_path;
    @Column(name = "advs_video_localtion_path")
    private String advs_video_localtion_path;
    @Column(name = "advs_video_length_of_time")
    private String advs_video_length_of_time;
    @Column(name = "advs_video_size")
    private double advs_video_size;
    @Column(name = "advs_creat_time")
    private String advs_creat_time;
    @Column(name = "advs_pass_time")
    private String advs_pass_time;
    @Column(name = "advs_play_begin_time")
    private String advs_play_begin_time;
    @Column(name = "advs_play_end_time")
    private String advs_play_end_time;
    @Column(name = "advs_play_interval_time")
    private String advs_play_interval_time;
    @Column(name = "advs_play_require_time")
    private int advs_play_require_time;
    @Column(name = "advs_status")
    private int advs_status;
    @Column(name = "advs_play_times")
    private int advs_play_times;

    public int getAdvs_id() {
        return advs_id;
    }

    public void setAdvs_id(int advs_id) {
        this.advs_id = advs_id;
    }

    public int getCreat_admin_user_id() {
        return creat_admin_user_id;
    }

    public void setCreat_admin_user_id(int creat_admin_user_id) {
        this.creat_admin_user_id = creat_admin_user_id;
    }

    public int getPass_admin_user_id() {
        return pass_admin_user_id;
    }

    public void setPass_admin_user_id(int pass_admin_user_id) {
        this.pass_admin_user_id = pass_admin_user_id;
    }

    public int getAdvs_industry() {
        return advs_industry;
    }

    public void setAdvs_industry(int advs_industry) {
        this.advs_industry = advs_industry;
    }

    public int getAdvs_charg_mode() {
        return advs_charg_mode;
    }

    public void setAdvs_charg_mode(int advs_charg_mode) {
        this.advs_charg_mode = advs_charg_mode;
    }

    public int getAdvs_is_point_device() {
        return advs_is_point_device;
    }

    public void setAdvs_is_point_device(int advs_is_point_device) {
        this.advs_is_point_device = advs_is_point_device;
    }

    public String getAdvs_title() {
        return advs_title;
    }

    public void setAdvs_title(String advs_title) {
        this.advs_title = advs_title;
    }

    public String getAdvs_video_download_path() {
        return advs_video_download_path;
    }

    public void setAdvs_video_download_path(String advs_video_download_path) {
        this.advs_video_download_path = advs_video_download_path;
    }

    public String getAdvs_video_localtion_path() {
        return advs_video_localtion_path;
    }

    public void setAdvs_video_localtion_path(String advs_video_localtion_path) {
        this.advs_video_localtion_path = advs_video_localtion_path;
    }

    public String getAdvs_video_length_of_time() {
        return advs_video_length_of_time;
    }

    public void setAdvs_video_length_of_time(String advs_video_length_of_time) {
        this.advs_video_length_of_time = advs_video_length_of_time;
    }

    public double getAdvs_video_size() {
        return advs_video_size;
    }

    public void setAdvs_video_size(double advs_video_size) {
        this.advs_video_size = advs_video_size;
    }

    public String getAdvs_creat_time() {
        return advs_creat_time;
    }

    public void setAdvs_creat_time(String advs_creat_time) {
        this.advs_creat_time = advs_creat_time;
    }

    public String getAdvs_pass_time() {
        return advs_pass_time;
    }

    public void setAdvs_pass_time(String advs_pass_time) {
        this.advs_pass_time = advs_pass_time;
    }

    public String getAdvs_play_begin_time() {
        return advs_play_begin_time;
    }

    public void setAdvs_play_begin_time(String advs_play_begin_time) {
        this.advs_play_begin_time = advs_play_begin_time;
    }

    public String getAdvs_play_end_time() {
        return advs_play_end_time;
    }

    public void setAdvs_play_end_time(String advs_play_end_time) {
        this.advs_play_end_time = advs_play_end_time;
    }

    public String getAdvs_play_interval_time() {
        return advs_play_interval_time;
    }

    public void setAdvs_play_interval_time(String advs_play_interval_time) {
        this.advs_play_interval_time = advs_play_interval_time;
    }

    public int getAdvs_play_require_time() {
        return advs_play_require_time;
    }

    public void setAdvs_play_require_time(int advs_play_require_time) {
        this.advs_play_require_time = advs_play_require_time;
    }

    public int getAdvs_status() {
        return advs_status;
    }

    public void setAdvs_status(int advs_status) {
        this.advs_status = advs_status;
    }

    public int getAdvs_play_times() {
        return advs_play_times;
    }

    public void setAdvs_play_times(int advs_play_times) {
        this.advs_play_times = advs_play_times;
    }

    @Override
    public String toString() {
        return "Advs_Video [advs_id=" + advs_id + ",creat_admin_user_id="+creat_admin_user_id+
                ",pass_admin_user_id="+pass_admin_user_id+
                ",advs_industry="+advs_industry+
                ",advs_charg_mode="+advs_charg_mode+
                ",advs_is_point_device="+advs_is_point_device+
                ",advs_title="+advs_title+
                ",advs_video_download_path="+advs_video_download_path+
                ",advs_video_localtion_path="+advs_video_localtion_path+
                ",advs_video_length_of_time="+advs_video_length_of_time+
                ",advs_video_size="+advs_video_size+
                ",advs_creat_time="+advs_creat_time+
                ",advs_pass_time="+advs_pass_time+
                ",advs_play_begin_time="+advs_play_begin_time+
                ",advs_play_end_time="+"advs_play_end_time"+
                ",advs_play_interval_time="+advs_play_interval_time+
                ",advs_play_require_time="+advs_play_require_time+
                ",advs_status="+advs_status+
                ",advs_play_times="+advs_play_times+
                "]";
    }




}
