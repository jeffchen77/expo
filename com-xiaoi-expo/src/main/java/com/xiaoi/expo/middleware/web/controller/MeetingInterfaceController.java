package com.xiaoi.expo.middleware.web.controller;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.common.utils.DateFormatUtils;
import com.xiaoi.expo.constants.Constants;
import com.xiaoi.expo.management.dialogue.meeting.service.MeetingService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bright.liang
 * @Description: ${todo}
 * @date 2018/3/2815:35
 */
@RestController
@RequestMapping(value = "/interface/meeting/")
public class MeetingInterfaceController {

    private Logger logger =  LoggerFactory.getLogger(MeetingInterfaceController.class);

    @Autowired
    MeetingService meetingService;

    @RequestMapping(value = "findByPage", method = RequestMethod.GET)
    public PageResult findByPage(Integer currentPage, Integer pageSize, String date){
    	
    	if(date == null) {
    		date = DateFormatUtils.getDateFormatYyyymmdd();
    	}else {
        	if(!isValidDate(date)) {
       		 PageResult pageResult = new PageResult();
                pageResult.setCode(Constants.ERROR_CODE_SYSTEM);
                pageResult.setMsg("日期格式错误，正确格式为:yyyy-MM-dd");
                return pageResult;
        	}
    	}
    	
        if(currentPage == null ||currentPage ==0){
            currentPage = 1;
        }

        if(pageSize == null || pageSize == 0){
            pageSize = 4; // 默认每页显示4条
        }
        try{
            return meetingService.findByPage(currentPage, pageSize, date);
        }catch (Exception e){
            logger.error("查询会议列表错误传入参数：currentPage：[" + currentPage + "]pageSize:[" + pageSize +"]");
            e.printStackTrace();
            PageResult pageResult = new PageResult();
            pageResult.setCode(Constants.ERROR_CODE_SYSTEM);
            pageResult.setMsg("查询会议信息出错");
            return pageResult;
        }
    }
    
    private boolean isValidDate(String date) {
    	boolean valid = true;
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	try {
    		format.setLenient(false);
    		format.parse(date);
    	}catch(ParseException e) {
    		logger.error("查询会议列表错误日期参数错误:[date]="+date);
    		e.printStackTrace();
    		valid=false;
    	}
    	return valid;
    }


    @RequestMapping(value = "findByMeeting", method = RequestMethod.GET)
    public PageResult findByMeeting(Integer currentPage, Integer pageSize, String date, String hallId){
        if(date == null) {
            date = DateFormatUtils.getDateFormatYyyymmdd();
        }else {
            if(!isValidDate(date)) {
                PageResult pageResult = new PageResult();
                pageResult.setCode(Constants.ERROR_CODE_SYSTEM);
                pageResult.setMsg("日期格式错误，正确格式为:yyyy-MM-dd");
                return pageResult;
            }
        }

        if(StringUtils.isEmpty(hallId)){
            PageResult pageResult = new PageResult();
            pageResult.setCode(Constants.ERROR_CODE_SYSTEM);
            pageResult.setMsg("请输入会场id");
            return pageResult;
        }

        if(currentPage == null ||currentPage ==0){
            currentPage = 1;
        }

        if(pageSize == null || pageSize == 0){
            pageSize = 4; // 默认每页显示4条
        }
        try {
            return meetingService.findByPageHall(currentPage, pageSize, date, hallId);
        } catch (Exception e) {
            logger.error("根据会场id查询会议议程出错 ");
            e.printStackTrace();
        }
        PageResult pageResult = new PageResult();
        pageResult.setCode(Constants.ERROR_CODE_SYSTEM);
        pageResult.setMsg("根据会场id查询会议议程出错");
        return pageResult;
    }

    @RequestMapping(value = "getMeetingDates", method = RequestMethod.GET)
    public PageResult getMeetingDates(String hallId){
        try {
            return meetingService.findMeetingDates(hallId);
        } catch (Exception e) {
            logger.error("根据会场查询会议日期出错");
            e.printStackTrace();
        }
        return null;
    }
    
    @RequestMapping(value = "getAllMeetingDates", method = RequestMethod.GET)
    public PageResult getAllMeetingDates(){
        try {
            return meetingService.getAllMeetingDates();
        } catch (Exception e) {
            logger.error("查询会议日期出错");
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "meetingDetail", method = RequestMethod.GET)
    public MapResult meetingDetail(String meetingId){
    	try {
    		return meetingService.meetingDetail(meetingId);
    	}catch(Exception e) {
    		e.printStackTrace();
    		return MapResult.error("查询会议详情接口出错");
    	}
    }

    @RequestMapping(value = "hallMeetings", method = RequestMethod.GET)
    public PageResult hallMeetings(String date, String hallId){
        if(date == null) {
            date = DateFormatUtils.getDateFormatYyyymmdd();
        }else {
            if(!isValidDate(date)) {
                PageResult pageResult = new PageResult();
                pageResult.setCode(Constants.ERROR_CODE_SYSTEM);
                pageResult.setMsg("日期格式错误，正确格式为:yyyy-MM-dd");
                return pageResult;
            }
        }

        if(StringUtils.isEmpty(hallId)){
            PageResult pageResult = new PageResult();
            pageResult.setCode(Constants.ERROR_CODE_SYSTEM);
            pageResult.setMsg("请输入会议厅id");
            return pageResult;
        }

        try {
            return meetingService.hallMeetings(date, hallId);
        } catch (Exception e) {
            logger.error("根据会议厅id查询会议议程出错 ");
            e.printStackTrace();
        }
        PageResult pageResult = new PageResult();
        pageResult.setCode(Constants.ERROR_CODE_SYSTEM);
        pageResult.setMsg("根据会议厅id查询会议议程出错");
        return pageResult;
    }

    @RequestMapping(value = "findByType", method = RequestMethod.GET)
    public List<Map<String, Object>> findByType(String types, Integer floor){
        return  meetingService.findByType(types, floor);
    }

    @RequestMapping(value = "findByHallName", method = RequestMethod.GET)
    public Map<String, Object> findByName(String hallName){
        String hall = "";
        try {
            hall = URLDecoder.decode(hallName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return meetingService.findByHallName(hall);
    }

}
