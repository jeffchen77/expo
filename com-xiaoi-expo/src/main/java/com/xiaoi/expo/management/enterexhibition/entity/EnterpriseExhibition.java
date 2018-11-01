package com.xiaoi.expo.management.enterexhibition.entity;

import org.hibernate.annotations.GenericGenerator;

import com.xiaoi.expo.management.baseconfig.exhibitionbooth.entity.ExhibitionBooth;
import com.xiaoi.expo.management.dictionary.entity.Dictionary;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Qu, De-Hui
 * @Description: 企业展台关联表
 * @date 2018/3/1216:43
 */
@Entity
@Table(name = "expo_enterprise_exhibition")
public class EnterpriseExhibition implements Serializable {
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@Column(name = "enterprise_id")
	private String enterpriseId;// 企业ID
	
	@Column(name = "exhibition_id")
	private String exhibitionId;// 展台ID

	@ManyToOne(cascade=CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name="exhibition_id", referencedColumnName="id", insertable=false, updatable=false)
	private ExhibitionBooth exhibitionBooth;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public ExhibitionBooth getExhibitionBooth() {
		return exhibitionBooth;
	}

	public void setExhibitionBooth(ExhibitionBooth exhibitionBooth) {
		this.exhibitionBooth = exhibitionBooth;
	}

	public String getExhibitionId() {
		return exhibitionId;
	}

	public void setExhibitionId(String exhibitionId) {
		this.exhibitionId = exhibitionId;
	}
}
