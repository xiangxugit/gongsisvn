package newwater.com.newwater.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2018/5/18 0018.
 *但个视频播放次数记录
 */
@Table(name = "VideoRecord")
public class VideoRecord {
    @Column(name = "ID", isId = true, autoGen = true)
    public int id;
    @Column(name = "VIDEONAME")
    public String videoname; // 姓名
    @Column(name="TIME")
    public String time;//播放的开始时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoname() {
        return videoname;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "VideoRecord [id=" + id + ", videoname=" + videoname + ", time=" + time +  "]";
    }


}
