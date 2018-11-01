package com.xiaoi.expo.management.enterexhibition.service;

import java.util.List;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.entity.ExhibitionBooth;
import com.xiaoi.expo.management.enterexhibition.entity.EnterpriseExhibition;

/**
 * @author Qu, De-Hui
 * @Description: 展台管理service层
 * @date 2018/3/20
 */
public interface EnterpriseExhibitionService {
	Boolean isExitByExhId(String exhId);
	void deleteByExhId(String exhId);
	void deleteByEnterpriseId(String exhId);
	void save(EnterpriseExhibition enterpriseExhibition);
	List<EnterpriseExhibition> findAllByEnterpriseId(String enterpriseId);
	EnterpriseExhibition findByExhibitionId(String exhId);
}
