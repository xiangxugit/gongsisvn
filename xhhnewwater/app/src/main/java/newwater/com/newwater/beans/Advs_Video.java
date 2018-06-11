package newwater.com.newwater.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2018/5/25 0025.
 */
@Table(name = "Advs_Video")
public class Advs_Video {
    /*广告id -----------------------------*/
    @Column(name = "advs_id", isId = true, autoGen = false)
    private int advsId;
    /*添加者id*/
    @Column(name = "creat_admin_user_id")
    private int creatAdminUserId;
    /*审核者id*/
    @Column(name = "pass_admin_user_id")
    private int passAdminUserId;
    /*播放行业*/
    @Column(name = "advs_industry")
    private int advsIndustry;
    /*播放类型  0-闲时播放；1-免费喝水播放 -----------------------------*/
    @Column(name = "advs_play_scene")
    private int advsPlayScene;
    /*设备收费模式*/
    @Column(name = "advs_charg_mode")
    private int advsChargMode;
    /*是都指定设备播放  0-不指定；1-指定设备*/
    @Column(name = "advs_is_point_device")
    private int advsIsPointDevice;
    /*标题*/
    @Column(name = "advs_title")
    private String advsTitle;
    /*下载路径 -----------------------------*/
    @Column(name = "advs_video_download_path")
    private String advsVideoDownloadPath;
    /*本地路径 -------~~~~~~~~~~~-------*/
    @Column(name = "advs_video_localtion_path")
    private String advsVideoLocaltionPath;
    /*视频时长 -----------------------------*/
    @Column(name = "advs_video_length_of_time")
    private int advsVideoLengthOfTime;
    /*视频大小*/
    @Column(name = "advs_video_size")
    private double advsVideoSize;
    /*创建时间*/
    @Column(name = "advs_creat_time")
    private String advsCreatTime;
    /*审核时间*/
    @Column(name = "advs_pass_time")
    private String advsPassTime;
    /*自动上架日期 -----------------------------*/
    @Column(name = "advs_play_begin_date")
    private String advsPlayBeginDatetimes;
    /*自动下架日期 -----------------------------*/
    @Column(name = "advs_play_end_date")
    private String advsPlayEndDatetimes;
    /*开始播放时间 -----------------------------*/
    @Column(name = "advs_play_begin_time")
    private String advsPlayBeginTime;
    /*结束播放时间 -----------------------------*/
    @Column(name = "advs_play_end_time")
    private String advsPlayEndTime;
    /*广告状态  0-未审核;1-已审核未上架;2-已审核已上架；3-已审核已下架*/
    @Column(name = "advs_status")
    private int advsStatus;
    /*是否已下载到本地 --------~~~~~~~~~~~~------*/
    @Column(name = "is_local")
    private boolean isLocal;

    public int getAdvsId() {
        return advsId;
    }

    public void setAdvsId(int advsId) {
        this.advsId = advsId;
    }

    public int getCreatAdminUserId() {
        return creatAdminUserId;
    }

    public void setCreatAdminUserId(int creatAdminUserId) {
        this.creatAdminUserId = creatAdminUserId;
    }

    public int getPassAdminUserId() {
        return passAdminUserId;
    }

    public void setPassAdminUserId(int passAdminUserId) {
        this.passAdminUserId = passAdminUserId;
    }

    public int getAdvsIndustry() {
        return advsIndustry;
    }

    public void setAdvsIndustry(int advsIndustry) {
        this.advsIndustry = advsIndustry;
    }

    public int getAdvsPlayScene() {
        return advsPlayScene;
    }

    public void setAdvsPlayScene(int advsPlayScene) {
        this.advsPlayScene = advsPlayScene;
    }

    public int getAdvsChargMode() {
        return advsChargMode;
    }

    public void setAdvsChargMode(int advsChargMode) {
        this.advsChargMode = advsChargMode;
    }

    public int getAdvsIsPointDevice() {
        return advsIsPointDevice;
    }

    public void setAdvsIsPointDevice(int advsIsPointDevice) {
        this.advsIsPointDevice = advsIsPointDevice;
    }

    public String getAdvsTitle() {
        return advsTitle;
    }

    public void setAdvsTitle(String advsTitle) {
        this.advsTitle = advsTitle;
    }

    public String getAdvsVideoDownloadPath() {
        return advsVideoDownloadPath;
    }

    public void setAdvsVideoDownloadPath(String advsVideoDownloadPath) {
        this.advsVideoDownloadPath = advsVideoDownloadPath;
    }

    public String getAdvsVideoLocaltionPath() {
        return advsVideoLocaltionPath;
    }

    public void setAdvsVideoLocaltionPath(String advsVideoLocaltionPath) {
        this.advsVideoLocaltionPath = advsVideoLocaltionPath;
    }

    public int getAdvsVideoLengthOfTime() {
        return advsVideoLengthOfTime;
    }

    public void setAdvsVideoLengthOfTime(int advsVideoLengthOfTime) {
        this.advsVideoLengthOfTime = advsVideoLengthOfTime;
    }

    public double getAdvsVideoSize() {
        return advsVideoSize;
    }

    public void setAdvsVideoSize(double advsVideoSize) {
        this.advsVideoSize = advsVideoSize;
    }

    public String getAdvsCreatTime() {
        return advsCreatTime;
    }

    public void setAdvsCreatTime(String advsCreatTime) {
        this.advsCreatTime = advsCreatTime;
    }

    public String getAdvsPassTime() {
        return advsPassTime;
    }

    public void setAdvsPassTime(String advsPassTime) {
        this.advsPassTime = advsPassTime;
    }

    public String getAdvsPlayBeginDatetimes() {
        return advsPlayBeginDatetimes;
    }

    public void setAdvsPlayBeginDatetimes(String advsPlayBeginDatetimes) {
        this.advsPlayBeginDatetimes = advsPlayBeginDatetimes;
    }

    public String getAdvsPlayEndDatetimes() {
        return advsPlayEndDatetimes;
    }

    public void setAdvsPlayEndDatetimes(String advsPlayEndDatetimes) {
        this.advsPlayEndDatetimes = advsPlayEndDatetimes;
    }

    public String getAdvsPlayBeginTime() {
        return advsPlayBeginTime;
    }

    public void setAdvsPlayBeginTime(String advsPlayBeginTime) {
        this.advsPlayBeginTime = advsPlayBeginTime;
    }

    public String getAdvsPlayEndTime() {
        return advsPlayEndTime;
    }

    public void setAdvsPlayEndTime(String advsPlayEndTime) {
        this.advsPlayEndTime = advsPlayEndTime;
    }

    public int getAdvsStatus() {
        return advsStatus;
    }

    public void setAdvsStatus(int advsStatus) {
        this.advsStatus = advsStatus;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    @Override
    public String toString() {
        return "Advs_Video{" +
                "advsId=" + advsId +
                ", creatAdminUserId=" + creatAdminUserId +
                ", passAdminUserId=" + passAdminUserId +
                ", advsIndustry=" + advsIndustry +
                ", advsPlayScene=" + advsPlayScene +
                ", advsChargMode=" + advsChargMode +
                ", advsIsPointDevice=" + advsIsPointDevice +
                ", advsTitle='" + advsTitle + '\'' +
                ", advsVideoDownloadPath='" + advsVideoDownloadPath + '\'' +
                ", advsVideoLocaltionPath='" + advsVideoLocaltionPath + '\'' +
                ", advsVideoLengthOfTime='" + advsVideoLengthOfTime + '\'' +
                ", advsVideoSize=" + advsVideoSize +
                ", advsCreatTime='" + advsCreatTime + '\'' +
                ", advsPassTime='" + advsPassTime + '\'' +
                ", advsPlayBeginDatetimes='" + advsPlayBeginDatetimes + '\'' +
                ", advsPlayEndDatetimes='" + advsPlayEndDatetimes + '\'' +
                ", advsPlayBeginTime='" + advsPlayBeginTime + '\'' +
                ", advsPlayEndTime='" + advsPlayEndTime + '\'' +
                ", advsStatus=" + advsStatus +
                ", isLocal=" + isLocal +
                '}';
    }
}
