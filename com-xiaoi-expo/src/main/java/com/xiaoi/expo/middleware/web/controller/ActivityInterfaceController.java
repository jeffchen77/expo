package com.xiaoi.expo.middleware.web.controller;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.common.utils.DateFormatUtils;
import com.xiaoi.expo.constants.Constants;
import com.xiaoi.expo.management.activity.entity.Activity;
import com.xiaoi.expo.management.activity.service.ActivityService;
import com.xiaoi.expo.management.baseconfig.enterprise.entity.Enterprise;
import com.xiaoi.expo.management.baseconfig.enterprise.service.EnterpriseService;
import com.xiaoi.expo.management.baseconfig.guest.service.GuestService;
import com.xiaoi.expo.management.dictionary.entity.Dictionary;
import com.xiaoi.expo.management.dictionary.service.DictionaryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Qu, De-Hui
 * @Description: 活动信息接口
 * @date 2018/3/2810:15
 */
@RestController
@RequestMapping(value = "/interface/activity")
public class ActivityInterfaceController {

    private Logger logger =  LoggerFactory.getLogger(ActivityInterfaceController.class);
    
    private static final String DATE_TIME_FORMAT="yyyy/MM/dd HH:mm";

    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private DictionaryService dictionaryService;
   
    @RequestMapping(value = "listByPavCode", method = RequestMethod.GET)
    public MapResult listByPavCode(String pavCode){
        try {
        	MapResult resutl = MapResult.ok();
        	Dictionary dic = dictionaryService.findByValue(pavCode);
        	String date = DateFormatUtils.dateToString(new Date(), DateFormatUtils.DATE_FORMAT_YYYYMMDDHHMMSS);
        	List<Activity> activitys = activityService.findAllByPavilionIdAndDate(dic.getId(), date);
        	List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();
        	for(Activity act : activitys) {
        		Map<String, Object> resultMap = new HashMap<String, Object>();
        		//活动名称
        		resultMap.put("name", act.getName());
        		//活动时间
        		String time = DateFormatUtils.dateToString(act.getActivityStartTime(), "MM月dd")+ " " +DateFormatUtils.dateToString(act.getActivityStartTime(), "HH:mm") + "-" + DateFormatUtils.dateToString(act.getActivityEndTime(), "HH:mm");
        		resultMap.put("time", time);
        		//活动状态
        		String start=DateFormatUtils.dateToString(act.getActivityStartTime(), DATE_TIME_FORMAT);
            	String end=DateFormatUtils.dateToString(act.getActivityEndTime(), DATE_TIME_FORMAT);
            	int status=DateFormatUtils.comparedDate(start, end, DATE_TIME_FORMAT);
            	resultMap.put("status", status);
            	//企业ID
            	resultMap.put("comid", act.getEnterprise()==null?null:act.getEnterprise().getId());
            	// fmap coor
            	resultMap.put("coorx", act.getFmapCoorx());
            	resultMap.put("coory", act.getFmapCoory());
        		listResult.add(resultMap);
        	}
        	resutl.put("list", listResult);
        	return resutl;
        } catch (Exception e) {
            logger.error("获取活动信息接口出错");
            return MapResult.error("获取活动信息接口出错");
        }
    }
    
    @RequestMapping(value = "listByPavCodeAndEntId", method = RequestMethod.GET)
    public MapResult listByPavCodeAndEntId(String pavCode, String entId){
        try {
        	MapResult resutl = MapResult.ok();
        	Dictionary dic = dictionaryService.findByValue(pavCode);
        	Date date = DateFormatUtils.stringToDate(DateFormatUtils.dateToString(new Date(), DateFormatUtils.DATE_FORMAT_YYYYMMDDHHMMSS), DateFormatUtils.DATE_FORMAT_YYYYMMDDHHMMSS);
        	List<Activity> activitys = activityService.findAllByPavilionIdAndEntIdAndDate(dic.getId(), entId, date);
        	Map<String, Object> resultMap = new HashMap<String, Object>();
        	
        	if(activitys!=null && activitys.size() > 0 && activitys.get(0)!=null) {
        		Activity act = activitys.get(0);
       		
        		//活动名称
        		resultMap.put("name", act.getName());
        		//活动时间
        		String time = DateFormatUtils.dateToString(act.getActivityStartTime(), "MM月dd")+ " " +DateFormatUtils.dateToString(act.getActivityStartTime(), "HH:mm") + "-" + DateFormatUtils.dateToString(act.getActivityEndTime(), "HH:mm");        		
        		resultMap.put("time", time);
        		//活动状态
        		String start=DateFormatUtils.dateToString(act.getActivityStartTime(), DATE_TIME_FORMAT);
            	String end=DateFormatUtils.dateToString(act.getActivityEndTime(), DATE_TIME_FORMAT);
            	int status=DateFormatUtils.comparedDate(start, end, DATE_TIME_FORMAT);
            	resultMap.put("status", status);
            	//企业ID
            	resultMap.put("comid", act.getEnterprise()==null?null:act.getEnterprise().getId());
            	// fmap coor
            	resultMap.put("coorx", act.getFmapCoorx());
            	resultMap.put("coory", act.getFmapCoory());

        	}
        	resutl.put("activity", resultMap);
        	return resutl;
        } catch (Exception e) {
            logger.error("获取活动信息接口出错");
            return MapResult.error("获取活动信息接口出错");
        }
    }
}
