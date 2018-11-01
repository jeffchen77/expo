package com.xiaoi.expo.management.baseconfig.enterprise.dto;

import java.io.Serializable;
import java.util.List;

public class EnterpriseDTO implements Serializable{
	private String id;
	private String enterpriseName;
	private String enterpriseType;
	private String enterPriseLog;
	private String enterpriseTheme;
	private String enterpriseDesc;
	//private String themeType;
	private List<String> hightArr;
	private List<String> themeTypeList;
	private List<String> exhibitionIds;
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public String getEnterpriseType() {
		return enterpriseType;
	}
	public void setEnterpriseType(String enterpriseType) {
		this.enterpriseType = enterpriseType;
	}
	public String getEnterPriseLog() {
		return enterPriseLog;
	}
	public void setEnterPriseLog(String enterPriseLog) {
		this.enterPriseLog = enterPriseLog;
	}
	public String getEnterpriseTheme() {
		return enterpriseTheme;
	}
	public void setEnterpriseTheme(String enterpriseTheme) {
		this.enterpriseTheme = enterpriseTheme;
	}
/*	public String getThemeType() {
		return themeType;
	}
	public void setThemeType(String themeType) {
		this.themeType = themeType;
	}*/
	public List<String> getHightArr() {
		return hightArr;
	}
	public void setHightArr(List<String> hightArr) {
		this.hightArr = hightArr;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getThemeTypeList() {
		return themeTypeList;
	}
	public void setThemeTypeList(List<String> themeTypeList) {
		this.themeTypeList = themeTypeList;
	}
	public String getEnterpriseDesc() {
		return enterpriseDesc;
	}
	public void setEnterpriseDesc(String enterpriseDesc) {
		this.enterpriseDesc = enterpriseDesc;
	}
	public List<String> getExhibitionIds() {
		return exhibitionIds;
	}
	public void setExhibitionIds(List<String> exhibitionIds) {
		this.exhibitionIds = exhibitionIds;
	}
	@Override
	public String toString() {
		System.out.println("enterpriseDesc="+enterpriseDesc+"	exhibitionIds="+exhibitionIds+" themeTypeList="+themeTypeList+"	id="+id+"	enterpriseName="+enterpriseName+"	enterpriseType="+enterpriseType+"	enterPriseLog="
				+enterPriseLog+"	enterpriseTheme="+enterpriseTheme+"	hightArr="+hightArr);
		return super.toString();
	}
	
}
