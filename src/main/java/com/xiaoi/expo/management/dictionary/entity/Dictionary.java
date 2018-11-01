package com.xiaoi.expo.management.dictionary.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @author Qu, De-Hui
 * @Description: 字典表
 * @date 2018/3/20
 */
@Entity
@Table(name="expo_dictionary")
public class Dictionary implements Serializable{
	@Id
    @Column(length=36, name = "id")
    @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id; // 主键

    @Column(length=200, name="displayname")
    private String displayName; // 字典描述

    @Column(length=200, name="value")
    private String value;// 字典值

    @Column(name="priority")
    private int priority;  // 排序

    @Column(length=50, name="parentid")
    private String parentId; // 父节点id

    @Column(length=100, name="remark")
    private String remark; //备注

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
