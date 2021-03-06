package com.xiaoi.expo.management.baseconfig.exhibition.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author chen fei
 * @Description: 展会信息表
 * @date 2018/3/23 09:43
 */
@Entity
@Table(name="expo_exhibition")
public class Exhibition implements Serializable{

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "name")
    private String name; //会场名称

    @Column(name="addr")
    private String addr; // 会场地址

    @Column(name = "picture")
    private String picture; //会场图片
    
    @Column(name = "create_time")
    private Date createTime; // 创建时间

    @Column(name = "update_time")
    private Date updateTime; // 修改时间

    @Column(name = "status")
    private String status; //状态：0正常， 1停用
    
    @Column(name = "description")
    private String description; //会场介绍

    @Column(name = "rank")
    private Integer rank;
    
        
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

   
    public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
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

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
