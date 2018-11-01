package com.xiaoi.expo.management.baseconfig.enterprise.entity;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.entity.ExhibitionBooth;
import com.xiaoi.expo.management.dictionary.entity.Dictionary;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Qu, De-Hui
 * @Description: 企业信息表
 * @date 2018/3/1216:43
 */
@Entity
@Table(name = "expo_enterprise")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Enterprise implements Serializable {
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@Column(name = "name")
	private String name;// 企业名称
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "type")
	private Dictionary typeDic; // 企业类别：字典表

	@Column(name = "logo")
	private String logo;// 企业logo存放地址

	@Column(name = "theme")
	private String theme; // 企业展出主题
	
	@Column(name = "descrption")
	private String descrption; // 企业展介绍
	
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name="expo_enterprise_showtype",
    joinColumns={@JoinColumn(name="enterprise_id", referencedColumnName="id")},inverseJoinColumns={@JoinColumn(name="dic_id", referencedColumnName="id")})
	private Set<Dictionary> showTypeDic;
	
	@Column(name = "show_highlights")
	private String showHighlights; // 展出亮点

	@Column(name = "status")
	private String status; // 企业信息状态：0正常，1 删除

	@Column(name = "data_resource")
	private String dataResource; // 数据来源：0 系统录入，1 同步数据

	@Column(name = "create_time")
	private Date createTime; // 创建时间

	@Column(name = "update_time")
	private Date updateDate; // 更新时间

	@Column(name = "rank")
	private Integer rank;
	
	@Transient
	private List<ExhibitionBooth> exhibtionList;
	
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

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getShowHighlights() {
		return showHighlights;
	}

	public void setShowHighlights(String showHighlights) {
		this.showHighlights = showHighlights;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDataResource() {
		return dataResource;
	}

	public void setDataResource(String dataResource) {
		this.dataResource = dataResource;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Dictionary getTypeDic() {
		return typeDic;
	}

	public void setTypeDic(Dictionary typeDic) {
		this.typeDic = typeDic;
	}

	public Set<Dictionary> getShowTypeDic() {
        return showTypeDic;
    }
    
    public void setShowTypeDic(Set<Dictionary> showTypeDic) {
        this.showTypeDic = showTypeDic;
    }

	public String getDescrption() {
		return descrption;
	}

	public void setDescrption(String descrption) {
		this.descrption = descrption;
	}

	public List<ExhibitionBooth> getExhibtionList() {
		return exhibtionList;
	}

	public void setExhibtionList(List<ExhibitionBooth> exhibtionList) {
		this.exhibtionList = exhibtionList;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
}
