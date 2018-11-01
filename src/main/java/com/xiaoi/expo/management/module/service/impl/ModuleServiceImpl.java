package com.xiaoi.expo.management.module.service.impl;

import com.xiaoi.expo.constants.Constants;
import com.xiaoi.expo.management.module.dao.ModuleDao;
import com.xiaoi.expo.management.module.entity.Module;
import com.xiaoi.expo.management.module.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bright.liang
 * @Description: ${todo}
 * @date 2018/3/1314:43
 */
@Service
public class ModuleServiceImpl implements ModuleService{

    @Autowired
    private ModuleDao moduleDao;

    @Override
    public List<Module> getModules(String role) {
        List<Module> result = new ArrayList<Module>();
        List<Module> modules = moduleDao.findAll();
        if(modules!= null && modules.size() > 0){
            for(Module module : modules){
                if(Constants.USER_ROLE_NORMAL.equals(role) && module.getModuleName().equals("用户管理")){
                    continue;
                }
                if(Constants.USER_ROLE_VISTER.equals(role)){
                    if(module.getModuleName().equals("嘉宾认证统计")){
                        if(StringUtils.isEmpty(module.getParentId())){
                            List<Module> subModules = new ArrayList<Module>();
                            for(Module module1 : modules){
                                if(module.getId().equals(module1.getParentId())){
                                    subModules.add(module1);
                                }
                            }
                            module.setSubModules(subModules);
                            result.add(module);
                        }else{
                            continue;
                        }
                    }
                }else{
                    if(StringUtils.isEmpty(module.getParentId())){
                        List<Module> subModules = new ArrayList<Module>();
                        for(Module module1 : modules){
                            if(module.getId().equals(module1.getParentId())){
                                subModules.add(module1);
                            }
                        }
                        module.setSubModules(subModules);
                        result.add(module);
                    }
                }

            }
        }
        return result;
    }
}
