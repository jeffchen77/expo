package com.xiaoi.expo.management.baseconfig.exhibitionbooth.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.xiaoi.expo.management.baseconfig.exhibitionbooth.entity.ExhibitionBooth;

/**
 * @author Qu, De-Hui
 * @Description: 展台库dao层
 * @date 2018/3/20 09:20
 */
public interface ExhibitionBoothDao extends JpaRepository<ExhibitionBooth, String>, JpaSpecificationExecutor<ExhibitionBooth> {
	int countById(String exhibitionBoothId);
	ExhibitionBooth findByCode(String code);
}
