package com.xiaoi.expo.management.module.dao;

import com.xiaoi.expo.management.module.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author bright.liang
 * @Description: ${todo}
 * @date 2018/3/1314:40
 */
public interface ModuleDao extends JpaRepository<Module, String>, JpaSpecificationExecutor<Module> {
}
