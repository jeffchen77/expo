package com.xiaoi.expo.middleware.web.controller;

import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.constants.Constants;
import com.xiaoi.expo.management.baseconfig.exhibition.service.ExhibitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bright.liang
 * @Description: ${todo}
 * @date 2018/3/2815:35
 */
@RestController
@RequestMapping(value = "/interface/exhibition/")
public class ExhibitionInterfaceController {

    private Logger logger =  LoggerFactory.getLogger(ExhibitionInterfaceController.class);

    @Autowired
    ExhibitionService exhibitionService;

    @RequestMapping(value = "findByPage", method = RequestMethod.GET)
    public PageResult findByPage(Integer currentPage, Integer pageSize){
        if(currentPage == null ||currentPage ==0){
            currentPage = 1;
        }

        if(pageSize == null || pageSize == 0){
            pageSize = 4; // 默认每页显示4条
        }
        try{
            return exhibitionService.findByPage(currentPage, pageSize);
        }catch (Exception e){
            logger.error("查询会场列表错误传入参数：currentPage：[" + currentPage + "]pageSize:[" + pageSize +"]");
            e.printStackTrace();
            PageResult pageResult = new PageResult();
            pageResult.setCode(Constants.ERROR_CODE_SYSTEM);
            pageResult.setMsg("查询参展商信息出错");
            return pageResult;
        }
    }

}
