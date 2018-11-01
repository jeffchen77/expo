package com.xiaoi.expo.management.enterexhibition.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoi.expo.management.enterexhibition.entity.EnterpriseExhibition;

/**
 * @author Qu, De-Hui
 * @Description: 展台库dao层
 * @date 2018/3/20 09:20
 */
public interface EnterpriseExhibitionDao extends JpaRepository<EnterpriseExhibition, String>, JpaSpecificationExecutor<EnterpriseExhibition> {
	EnterpriseExhibition findByExhibitionId(String exhId);
	void deleteByExhibitionId(String exhId);
	@Transactional
	@Modifying
	@Query(value = "delete from expo_enterprise_exhibition where enterprise_id=?1", nativeQuery = true)
	void deleteByEnterpriseId(String enterpriseId);
	List<EnterpriseExhibition> findAllByEnterpriseId(String enterpriseId);
}
