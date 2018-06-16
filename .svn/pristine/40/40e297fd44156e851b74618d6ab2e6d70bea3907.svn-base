package newwater.com.newwater.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.Date;



@Table(name = "SysDeviceWaterQualityAO")
public class SysDeviceNoticeAO implements Serializable {
	/**
	 * author:zhouaqiang
	 */

	private static final long serialVersionUID = -7105952477354843044L;

	/**
	 * 主键
	 */
	@Column(name = "deviceNoticeId", isId = true, autoGen = true)
	private Integer deviceNoticeId;

	/**
	 * 设备ID
	 */
	@Column(name = "deviceId")
	private Integer deviceId;

	/**
	 * 故障类别（1：断网；2：滤芯不足；3：滤芯用完；4：水质异常；5：纸杯不足；6：纸杯用完；7：耗水量异常；8：漏电；9：漏水；10：原水缺水；11：视频地址错误）
	 */
	@Column(name = "deviceNoticeType")
	private Integer deviceNoticeType;

	/**
	 * 预警级别 (0：异常警告；1：严重故障)
	 */
	@Column(name = "deviceNoticeLeve")
	private Integer deviceNoticeLeve;

	/**
	 * 预警主题
	 */
	@Column(name = "deviceNoticeSubject")
	private String deviceNoticeSubject;

	/**
	 * 预警详情
	 */
	@Column(name = "deviceNoticeContent")
	private String deviceNoticeContent;

	/**
	 * 预警产生时间
	 */
	@Column(name = "deviceNoticeTime")
	private String deviceNoticeTime ;
	/*private java.util.Date deviceNoticeTime;*/

	/** 后台用户ID */
	@Column(name = "adminUserId")
	private Integer adminUserId;

	/** 预警状态 */
	/**
	 * 0：30分钟内未处理 1：30分钟后处理 2电话通知 3已知晓 4：处理中 5维修中 6已返厂 7已处理
	 */
	@Column(name = "deviceNoticeStatus")
	private Integer deviceNoticeStatus;

	public SysDeviceNoticeAO() {
		super();
	}

	public SysDeviceNoticeAO(Integer deviceId, Integer deviceNoticeType, Integer deviceNoticeLeve, String deviceNoticeSubject, String deviceNoticeContent, String deviceNoticeTime) {
		this.deviceId = deviceId;
		this.deviceNoticeType = deviceNoticeType;
		this.deviceNoticeLeve = deviceNoticeLeve;
		this.deviceNoticeSubject = deviceNoticeSubject;
		this.deviceNoticeContent = deviceNoticeContent;
		this.deviceNoticeTime = deviceNoticeTime;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getDeviceNoticeId() {
		return deviceNoticeId;
	}

	public void setDeviceNoticeId(Integer deviceNoticeId) {
		this.deviceNoticeId = deviceNoticeId;
	}

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getDeviceNoticeType() {
		return deviceNoticeType;
	}

	public void setDeviceNoticeType(Integer deviceNoticeType) {
		this.deviceNoticeType = deviceNoticeType;
	}

	public Integer getDeviceNoticeLeve() {
		return deviceNoticeLeve;
	}

	public void setDeviceNoticeLeve(Integer deviceNoticeLeve) {
		this.deviceNoticeLeve = deviceNoticeLeve;
	}

	public String getDeviceNoticeSubject() {
		return deviceNoticeSubject;
	}

	public void setDeviceNoticeSubject(String deviceNoticeSubject) {
		this.deviceNoticeSubject = deviceNoticeSubject;
	}

	public String getDeviceNoticeContent() {
		return deviceNoticeContent;
	}

	public void setDeviceNoticeContent(String deviceNoticeContent) {
		this.deviceNoticeContent = deviceNoticeContent;
	}

	public String getDeviceNoticeTime() {
		return deviceNoticeTime;
	}

	public void setDeviceNoticeTime(String deviceNoticeTime) {
		this.deviceNoticeTime = deviceNoticeTime;
	}

	public Integer getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(Integer adminUserId) {
		this.adminUserId = adminUserId;
	}

	public Integer getDeviceNoticeStatus() {
		return deviceNoticeStatus;
	}

	public void setDeviceNoticeStatus(Integer deviceNoticeStatus) {
		this.deviceNoticeStatus = deviceNoticeStatus;
	}
}
