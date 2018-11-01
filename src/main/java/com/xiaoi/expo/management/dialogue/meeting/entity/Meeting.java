package com.xiaoi.expo.management.dialogue.meeting.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaoi.expo.management.baseconfig.exhibition.entity.Exhibition;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.entity.ExhibitionBooth;
import com.xiaoi.expo.management.baseconfig.exhibitionhall.entity.ExhibitionHall;
import com.xiaoi.expo.management.baseconfig.guest.entity.Guest;
import com.xiaoi.expo.management.dictionary.entity.Dictionary;

/**
 * @author chen fei
 * @Description: 会议信息表
 * @date 2018/3/26 09:43
 */
@Entity
@Table(name="expo_meeting")
public class Meeting implements Serializable{

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "theme")
    private String theme; //会议主题

    @Column(name="total_people")
    private String totalPeople; // 参会人员规模
     
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "meeting_type")
	private Dictionary typeDic; // 会议类别：字典表
    
    @Column(name="location_type")
    private String locationType; // 0:展馆 1:会场
    
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
    
    
    @Column(name = "meeting_start_time")
    private Date meetingStartTime; //会议开始时间
    
    @Column(name = "meeting_end_time")
    private Date meetingEndTime; //会议结束时间
    
    @Column(name = "create_time")
    private Date createTime; // 创建时间

    @Column(name = "update_time")
    private Date updateTime; // 修改时间

    @Column(name = "description")
    private String description; //会议介绍    
   
    
    @ManyToMany
	@JoinTable(name = "expo_meeting_guest",joinColumns = {@JoinColumn(name = "meeting_id")},inverseJoinColumns = {@JoinColumn(name = "guest_id")})
    @OrderBy("orderNum ASC")
    private Set<Guest> guests;
        
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getTotalPeople() {
		return totalPeople;
	}

	public void setTotalPeople(String totalPeople) {
		this.totalPeople = totalPeople;
	}

	public Dictionary getTypeDic() {
		return typeDic;
	}

	public void setTypeDic(Dictionary typeDic) {
		this.typeDic = typeDic;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getMeetingStartTime() {
		return meetingStartTime;
	}

	public void setMeetingStartTime(Date meetingStartTime) {
		this.meetingStartTime = meetingStartTime;
	}

	public Date getMeetingEndTime() {
		return meetingEndTime;
	}

	public void setMeetingEndTime(Date meetingEndTime) {
		this.meetingEndTime = meetingEndTime;
	}

	public Set<Guest> getGuests() {
		return guests;
	}

	public void setGuests(Set<Guest> guests) {
		this.guests = guests;
	}
	

}
