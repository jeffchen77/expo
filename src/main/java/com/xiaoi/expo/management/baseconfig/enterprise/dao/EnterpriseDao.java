package com.xiaoi.expo.management.baseconfig.enterprise.dao;

import com.xiaoi.expo.management.baseconfig.enterprise.entity.Enterprise;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Qu, De-Hui
 * @Description: 企业库管理dao层
 * @date 2018/3/20
 */
public interface EnterpriseDao extends JpaRepository<Enterprise, String>, JpaSpecificationExecutor<Enterprise> {
	@Transactional
	@Modifying
	@Query(value = "delete from expo_enterprise where id=?1", nativeQuery = true)
	void deleteEnterprise(String enterpriseId);
	List<Enterprise> findByNameLike(String name);
}
