package com.xiaoi.expo.management.module.service;

import com.xiaoi.expo.management.module.entity.Module;

import java.util.List;

/**
 * @author bright.liang
 * @Description: ${todo}
 * @date 2018/3/1314:42
 */
public interface ModuleService {

    /**
    * @Description: 获取菜单列表
    * @return List<Module>
    * @throws
    * @author bright.liang
    * @date 2018/3/13 14:48
    */
    List<Module> getModules(String role);
}
