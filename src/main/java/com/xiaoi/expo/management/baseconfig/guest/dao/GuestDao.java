package com.xiaoi.expo.management.baseconfig.guest.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.xiaoi.expo.management.baseconfig.guest.entity.Guest;

/**
 * @author chen fei
 * @Description: 嘉宾库dao层
 * @date 2018/3/20 09:20
 */
public interface GuestDao extends JpaRepository<Guest, String>, JpaSpecificationExecutor<Guest> {
	List<Guest> findByQrCode(String authCode);
}
