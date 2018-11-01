package com.xiaoi.expo.middleware.web.controller;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.constants.Constants;
import com.xiaoi.expo.management.baseconfig.enterprise.entity.Enterprise;
import com.xiaoi.expo.management.baseconfig.enterprise.service.EnterpriseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author bright.liang
 * @Description: 展商信息接口
 * @date 2018/3/2810:15
 */
@RestController
@RequestMapping(value = "/interface/enterprise")
public class EntrpriseInterfaceController {

    private Logger logger =  LoggerFactory.getLogger(EntrpriseInterfaceController.class);

    @Autowired
    private EnterpriseService enterpriseService;

    @Value("${ftp.domain}")
    private String domain;

    @Value("${enterprise.separator}")
    private String separator;

    @RequestMapping(value = "findByPage", method = RequestMethod.GET)
    public PageResult findEnterprise(Integer currentPage, Integer pageSize){
        if(currentPage == null ||currentPage ==0){
            currentPage = 1;
        }

        if(pageSize == null || pageSize == 0){
            pageSize = 4; // 默认每页显示4条
        }

        try{
            PageResult pageResult = enterpriseService.findByPage(currentPage, pageSize);
            return  pageResult;
        }catch (Exception e){
            logger.error("查询参展商信息出错，参数为：currentPage=" + currentPage + "pageSize=" + pageSize);
            e.printStackTrace();
            PageResult pageResult = new PageResult();
            pageResult.setCode(Constants.ERROR_CODE_SYSTEM);
            pageResult.setMsg("查询参展商信息出错");
            return pageResult;
        }
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public MapResult detail(String id){
        logger.info("查询展商详情， id为 [" + id + "]");
        if(StringUtils.isEmpty(id)){
            return MapResult.error(Constants.ERROR_CODE_VALIDATION, "展商id不能为空");
        }
        try{
            Enterprise enterprise = enterpriseService.findOne(id);
            if(enterprise == null){
                return MapResult.error(Constants.ERROR_CODE_VALIDATION, "展商信息不存在或已被删除");
            }
            Map<String, Object> enterpriseMap = new HashMap<String, Object>();
            enterpriseMap.put("id", enterprise.getId());
            enterpriseMap.put("description", enterprise.getDescrption());
            enterpriseMap.put("showHighlights", !StringUtils.isEmpty(enterprise.getShowHighlights()) ? enterprise.getShowHighlights().split(separator): null);
            enterpriseMap.put("name", enterprise.getName());
            enterpriseMap.put("image", !StringUtils.isEmpty(enterprise.getLogo())? domain + enterprise.getLogo(): null);
            MapResult mapResult = MapResult.ok();
            mapResult.put("detail", enterpriseMap);
            return mapResult;
        }catch (Exception e){
            logger.error("查询展商详情出错");
            e.printStackTrace();
            return MapResult.error(Constants.ERROR_CODE_SYSTEM, "查询展商详情出错");
        }

    }
    
    @RequestMapping(value = "detailByName", method = RequestMethod.GET)
    public MapResult detailByName(String name){
        logger.info("查询展商详情， name为 [" + name + "]");
        if(StringUtils.isEmpty(name)){
            return MapResult.error(Constants.ERROR_CODE_VALIDATION, "展商名称不能为空");
        }
        try{
            List<Enterprise> enterpriseList = enterpriseService.findByNameLike("%"+name+"%");
            if(enterpriseList == null || enterpriseList.size()<=0){
                return MapResult.error(Constants.ERROR_CODE_VALIDATION, "展商信息不存在或已被删除");
            }
            Enterprise enterprise = enterpriseList.get(0);
            Map<String, Object> enterpriseMap = new HashMap<String, Object>();
            enterpriseMap.put("id", enterprise.getId());
            enterpriseMap.put("description", enterprise.getDescrption());
            enterpriseMap.put("showHighlights", !StringUtils.isEmpty(enterprise.getShowHighlights()) ? enterprise.getShowHighlights().split(separator): null);
            enterpriseMap.put("name", enterprise.getName());
            enterpriseMap.put("image", !StringUtils.isEmpty(enterprise.getLogo())? domain + enterprise.getLogo(): null);
            MapResult mapResult = MapResult.ok();
            mapResult.put("detail", enterpriseMap);
            return mapResult;
        }catch (Exception e){
            logger.error("查询展商详情出错");
            e.printStackTrace();
            return MapResult.error(Constants.ERROR_CODE_SYSTEM, "查询展商详情出错");
        }

    }
}
