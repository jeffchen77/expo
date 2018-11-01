package com.xiaoi.expo.management.baseconfig.exhibitionbooth.entity;

import org.hibernate.annotations.GenericGenerator;

import com.xiaoi.expo.management.dictionary.entity.Dictionary;
import com.xiaoi.expo.management.enterexhibition.entity.EnterpriseExhibition;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author Qu, De-Hui
 * @Description: 展台信息表
 * @date 2018/3/1216:43
 */
@Entity
@Table(name="expo_exhibition_booth")
public class ExhibitionBooth implements Serializable{

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "code")
    private String code; //展台编号
  
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "position")
	private Dictionary position; // 企业类别：字典表
    
    @Column(name = "picture_addr")
    private String pictureAddr; //展台图片地址

    @Column(name = "create_time")
    private Date createTime; // 创建时间

    @Column(name = "update_time")
    private Date updateTime; // 修改时间

    @Column(name = "status")
    private String status; //展台信息状态：0正常，1删除
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Dictionary getPosition() {
		return position;
	}

	public void setPosition(Dictionary position) {
		this.position = position;
	}

	public String getPictureAddr() {
		return pictureAddr;
	}

	public void setPictureAddr(String pictureAddr) {
		this.pictureAddr = pictureAddr;
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

}
