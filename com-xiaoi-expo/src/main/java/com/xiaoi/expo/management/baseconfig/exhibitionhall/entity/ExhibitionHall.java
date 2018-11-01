package com.xiaoi.expo.management.baseconfig.exhibitionhall.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.xiaoi.expo.management.baseconfig.exhibition.entity.Exhibition;

/**
 * @author chen fei
 * @Description: 议会厅信息表
 * @date 2018/3/27 09:43
 */
@Entity
@Table(name="expo_exhibition_hall")
public class ExhibitionHall implements Serializable{

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "name")
    private String name; //议会厅名称

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition; // 议会厅地址

    @Column(name = "picture")
    private String picture; //议会厅图片
    
    @Column(name = "create_time")
    private Date createTime; // 创建时间

    @Column(name = "update_time")
    private Date updateTime; // 修改时间

    @Column(name = "status")
    private String status; //状态：0正常， 1停用
    
    @Column(name = "description")
    private String description; //议会厅介绍
    
        
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   

	public Exhibition getExhibition() {
		return exhibition;
	}

	public void setExhibition(Exhibition exhibition) {
		this.exhibition = exhibition;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
