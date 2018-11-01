package com.xiaoi.expo.management.user.dao;

import com.xiaoi.expo.management.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author bright.liang
 * @Description: 用户管理dao层
 * @date 2018/3/1216:58
 */
public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
}
