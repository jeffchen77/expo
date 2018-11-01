package com.xiaoi.expo.management.baseconfig.exhibitionhall.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.xiaoi.expo.management.baseconfig.exhibitionhall.entity.ExhibitionHall;

/**
 * @author chen fei
 * @Description: 议会厅dao层
 * @date 2018/3/27 09:20
 */
public interface ExhibitionHallDao extends JpaRepository<ExhibitionHall, String>, JpaSpecificationExecutor<ExhibitionHall> {
}
