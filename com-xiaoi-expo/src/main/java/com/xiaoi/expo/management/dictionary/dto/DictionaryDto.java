package com.xiaoi.expo.management.dictionary.dto;

import java.util.List;

import com.xiaoi.expo.management.dictionary.entity.Dictionary;

/**
 * @author chen fei
 * @Description: 字典表
 * @date 2018/3/28
 */
public class DictionaryDto extends Dictionary{
	
    
    private List<Dictionary> dicList; // 字典列表

	public List<Dictionary> getDicList() {
		return dicList;
	}

	public void setDicList(List<Dictionary> dicList) {
		this.dicList = dicList;
	}
    
        

}
