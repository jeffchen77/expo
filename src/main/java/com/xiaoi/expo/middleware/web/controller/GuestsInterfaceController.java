package com.xiaoi.expo.middleware.web.controller;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.constants.Constants;
import com.xiaoi.expo.management.baseconfig.enterprise.entity.Enterprise;
import com.xiaoi.expo.management.baseconfig.enterprise.service.EnterpriseService;
import com.xiaoi.expo.management.baseconfig.guest.service.GuestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * @author bright.liang
 * @Description: 展商信息接口
 * @date 2018/3/2810:15
 */
@RestController
@RequestMapping(value = "/interface/guests")
public class GuestsInterfaceController {

    private Logger logger =  LoggerFactory.getLogger(GuestsInterfaceController.class);

    @Autowired
    private GuestService guestService;

    @Value("${ftp.domain}")
    private String domain;

    @RequestMapping(value = "findByPage", method = RequestMethod.GET)
    public PageResult findGuests(Integer currentPage, Integer pageSize){
        if(currentPage == null ||currentPage ==0){
            currentPage = 1;
        }

        if(pageSize == null || pageSize == 0){
            pageSize = 6; // 默认每页显示6条
        }

        try{
            PageResult pageResult = guestService.findByPage(currentPage, pageSize);
            return  pageResult;
        }catch (Exception e){
            logger.error("查询嘉宾列表信息出错，参数为：currentPage=" + currentPage + "pageSize=" + pageSize);
            e.printStackTrace();
            PageResult pageResult = new PageResult();
            pageResult.setCode(Constants.ERROR_CODE_SYSTEM);
            pageResult.setMsg("查询嘉宾列表信息出错");
            return pageResult;
        }
    }
   
    @RequestMapping(value = "auth", method = RequestMethod.GET)
    public MapResult auth(String authCode){
        try {
            return guestService.auth(authCode);
        } catch (Exception e) {
            logger.error("嘉宾认证接口出错");
            e.printStackTrace();
            return MapResult.error("嘉宾认证接口出错");
        }
    }
}
