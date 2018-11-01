package com.xiaoi.expo.management.baseconfig.exhibition.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.xiaoi.expo.management.baseconfig.exhibition.entity.Exhibition;

/**
 * @author chen fei
 * @Description: 会场库dao层
 * @date 2018/3/23 09:20
 */
public interface ExhibitionDao extends JpaRepository<Exhibition, String>, JpaSpecificationExecutor<Exhibition> {
}
