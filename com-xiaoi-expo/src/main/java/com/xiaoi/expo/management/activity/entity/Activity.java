package com.xiaoi.expo.management.activity.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaoi.expo.management.baseconfig.enterprise.entity.Enterprise;
import com.xiaoi.expo.management.baseconfig.exhibition.entity.Exhibition;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.entity.ExhibitionBooth;
import com.xiaoi.expo.management.baseconfig.exhibitionhall.entity.ExhibitionHall;
import com.xiaoi.expo.management.dictionary.entity.Dictionary;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Qu, De-Hui
 * @Description: 活动表
 * @date 2018/3/1216:43
 */
@Entity
@Table(name="expo_activity")
public class Activity implements Serializable{
    @Id
    @Column(name = "activity_id", length=36)
    @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid")
    private String activityId; // 主键

    @Column(name="name", length=200)
    private String name; // 活动名称

    @Column(name="location_type", length=1)
    private String locationType; // 0:展馆 1:会场
    
    @Column(name="status", length=1)
    private String status; // 0:有效 1:无效
    
    @Column(name="description", length=5000)
    private String description; //活动描述
    
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name = "pavilion_id")
    private Dictionary pavilionDic; //展馆
    
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name = "booth_id")
    private ExhibitionBooth booth; //展台
    
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition; //会场
    
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name = "hall_id")
    private ExhibitionHall exhibitionHall; //议会厅
    
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise; //举办方
    
    @Column(name = "activity_start_time")
    private Date activityStartTime; // 活动开始时间
    
    @Column(name = "activity_end_time")
    private Date activityEndTime; // 活动开始时间
    
    @Column(name = "create_time")
    private Date createTime; // 创建时间

    @Column(name = "update_time")
    private Date updateTime; // 修改时间
    
    @Column(name = "fmap_coorx")
    private String fmapCoorx;
    
    @Column(name = "fmap_coory")
    private String fmapCoory;

    @Transient
	private String activityTime;
    
	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public Dictionary getPavilionDic() {
		return pavilionDic;
	}

	public void setPavilionDic(Dictionary pavilionDic) {
		this.pavilionDic = pavilionDic;
	}

	public ExhibitionBooth getBooth() {
		return booth;
	}

	public void setBooth(ExhibitionBooth booth) {
		this.booth = booth;
	}

	public Exhibition getExhibition() {
		return exhibition;
	}

	public void setExhibition(Exhibition exhibition) {
		this.exhibition = exhibition;
	}

	public ExhibitionHall getExhibitionHall() {
		return exhibitionHall;
	}

	public void setExhibitionHall(ExhibitionHall exhibitionHall) {
		this.exhibitionHall = exhibitionHall;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
	public Date getActivityStartTime() {
		return activityStartTime;
	}

	public void setActivityStartTime(Date activityStartTime) {
		this.activityStartTime = activityStartTime;
	}
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
	public Date getActivityEndTime() {
		return activityEndTime;
	}

	public void setActivityEndTime(Date activityEndTime) {
		this.activityEndTime = activityEndTime;
	}

	public String getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(String activityTime) {
		this.activityTime = activityTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFmapCoorx() {
		return fmapCoorx;
	}

	public void setFmapCoorx(String fmapCoorx) {
		this.fmapCoorx = fmapCoorx;
	}

	public String getFmapCoory() {
		return fmapCoory;
	}

	public void setFmapCoory(String fmapCoory) {
		this.fmapCoory = fmapCoory;
	}

	
	
}
