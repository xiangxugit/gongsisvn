package newwater.com.newwater.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2018/5/18 0018.
 * 获取的网络播放策略
 *
 */
@Table(name = "VideoRecord")

public class VideoConfig {
    @Column(name = "ID", isId = true, autoGen = true)
    public int id;

    @Column(name = "VIDEOLIST")
    public String videolist; // 一个播放策略下的所有需要播放的视频

    @Column(name="STARTTIME")
    public String starttime;//播放的时间段开始时间

    @Column(name="ENDTIME")
    public String endtime;//播放的时间段结束时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideolist() {
        return videolist;
    }

    public void setVideolist(String videolist) {
        this.videolist = videolist;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    @Override
    public String toString() {
        return "person [id=" + id + ", videolist=" + videolist + ", starttime=" + starttime + ", endtime=" + endtime + "]";
    }



}
