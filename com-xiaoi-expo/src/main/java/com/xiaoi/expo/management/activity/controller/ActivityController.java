package com.xiaoi.expo.management.activity.controller;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.common.utils.DateFormatUtils;
import com.xiaoi.expo.management.activity.entity.Activity;
import com.xiaoi.expo.management.activity.service.ActivityService;
import com.xiaoi.expo.management.baseconfig.enterprise.dto.EnterpriseDTO;
import com.xiaoi.expo.management.baseconfig.enterprise.entity.Enterprise;
import com.xiaoi.expo.management.baseconfig.enterprise.service.EnterpriseService;
import com.xiaoi.expo.management.baseconfig.exhibition.entity.Exhibition;
import com.xiaoi.expo.management.baseconfig.exhibition.service.ExhibitionService;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.entity.ExhibitionBooth;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.service.ExhibitionBoothService;
import com.xiaoi.expo.management.baseconfig.exhibitionhall.entity.ExhibitionHall;
import com.xiaoi.expo.management.baseconfig.exhibitionhall.service.ExhibitionHallService;
import com.xiaoi.expo.management.baseconfig.guest.entity.Guest;
import com.xiaoi.expo.management.dialogue.meeting.dto.MeetingDto;
import com.xiaoi.expo.management.dialogue.meeting.entity.Meeting;
import com.xiaoi.expo.management.dictionary.dto.DictionaryDto;
import com.xiaoi.expo.management.dictionary.entity.Dictionary;
import com.xiaoi.expo.management.dictionary.service.DictionaryService;
import com.xiaoi.expo.management.enterexhibition.entity.EnterpriseExhibition;
import com.xiaoi.expo.management.enterexhibition.service.EnterpriseExhibitionService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Qu, De-Hui
 * @Description: ${todo}
 * @date 2018/3/1217:15
 */
@Controller
@RequestMapping(value = "/management/activity")
public class ActivityController {

    private Logger logger =  LoggerFactory.getLogger(ActivityController.class);

	private static final String ACTIVITY_LIST = "activity/list";
	
	private static final String ACTIVITY_ADD = "activity/add";
	
	private static final String ACTIVITY_VIEW = "activity/view";
	
	private static final String ACTIVITY_EDIT = "activity/edit";
	
	 private static final String DATE_TIME_FORMAT="yyyy/MM/dd HH:mm";

    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private EnterpriseService enterpriseService;
    
    @Autowired
    private DictionaryService dictionaryService;
    
    @Autowired
    private ExhibitionService exhibitionService;
    
    @Autowired
    private ExhibitionHallService exhibitionHallService;
    
    @Autowired
    private ExhibitionBoothService exhibitionBoothService;
    
    @Autowired
    private EnterpriseExhibitionService enterpriseExhibitionService;
    
    /**
     * @Description: 跳转到列表页面
     * @return String
     * @author Qu, De-Hui
     * @date 2018/3/13 18:04
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(){
        return ACTIVITY_LIST;
    }
    
    /**
	 * @Description: 分页查询活动信息
	 * @param page
	 *            页码
	 * @param limit
	 *            每页显示条数
	 * @param searchName
	 *            搜索关键词
	 * @return PageResult<Activity>
	 * @author Qu, De-Hui
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "findByPagging", method = RequestMethod.GET)
	@ResponseBody
	public PageResult<Activity> findByPagging(Integer page, Integer limit, String searchName) {
		if (page == null) {
			page = 0;
			limit = 10;
		}
		return activityService.getActivity(page, limit, searchName);
	}
	
	/**
	 * @Description: 加载活动新增页面
	 * @return String
	 * @author Qu, De-Hui
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add(Model model){  	
    	model.addAttribute("list",enterpriseService.findAll());
        return ACTIVITY_ADD;
    }
	
	/**
	 * @Description: 删除活动信息
	 * @return MapResult
	 * @author Qu, De-Hui
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public MapResult delete(String activityId) {
		return activityService.delete(activityId);
	}
	
	/**
     * @Description: 保存活动信息
     * @param activity 活动信息
     * @return MapResult
     * @author Qu, De-Hui
     * @date 2018/3/21
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public MapResult save(Activity activity){
    	Activity oldActivity = null;
        if(!StringUtils.isEmpty(activity.getActivityId())){
        	oldActivity = activityService.findOne(activity.getActivityId());
            if(oldActivity == null){
                return MapResult.error("活动不存在或已被删除");
            }
            String time = activity.getActivityTime();
            if(!StringUtils.isEmpty(time)) {
            	String[] tArr = time.split("-");
            	if(tArr!=null && tArr.length == 2) {
            		try {
						oldActivity.setActivityStartTime(DateFormatUtils.stringToDate(tArr[0], "yyyy/MM/dd HH:mm"));
						oldActivity.setActivityEndTime(DateFormatUtils.stringToDate(tArr[1], "yyyy/MM/dd HH:mm"));
					} catch (Exception e) {
						e.printStackTrace();
					}
            	}
            }
            oldActivity.setBooth(activity.getBooth());
            oldActivity.setEnterprise(activity.getEnterprise());
            oldActivity.setExhibition(activity.getExhibition());
            oldActivity.setExhibitionHall(activity.getExhibitionHall());
            oldActivity.setLocationType(activity.getLocationType());
            oldActivity.setName(activity.getName());
            oldActivity.setDescription(activity.getDescription());
            oldActivity.setPavilionDic(activity.getPavilionDic());
            oldActivity.setUpdateTime(new Date());            
        }else{
        	oldActivity = activity;
            
        	String time = activity.getActivityTime();
            if(!StringUtils.isEmpty(time)) {
            	String[] tArr = time.split("-");
            	if(tArr!=null && tArr.length == 2) {
            		try {
						oldActivity.setActivityStartTime(DateFormatUtils.stringToDate(tArr[0], "yyyy/MM/dd HH:mm"));
						oldActivity.setActivityEndTime(DateFormatUtils.stringToDate(tArr[1], "yyyy/MM/dd HH:mm"));
					} catch (Exception e) {
						e.printStackTrace();
					}
            	}
            }
        	oldActivity.setStatus("0");
        	oldActivity.setCreateTime(new Date());
        	oldActivity.setUpdateTime(new Date());
        }
        return activityService.save(oldActivity);
    }
    
    /**
	 * @Description: 加载活动详情界面
	 * @return String
	 * @author Qu, De-Hui
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(Model model, String id) {
		logger.debug("activityId:"+id);
		
		Activity activity =activityService.findOne(id);
    	
    	if(activity.getActivityStartTime() == null || activity.getActivityEndTime() == null) {
    		activity.setActivityTime("");
    	}else {
        	String start=DateFormatUtils.dateToString(activity.getActivityStartTime(), DATE_TIME_FORMAT);
        	String end=DateFormatUtils.dateToString(activity.getActivityEndTime(), DATE_TIME_FORMAT);
        	activity.setActivityTime(start+" - "+end);
    	}

    	if("0".equals(activity.getLocationType())) {
	    	model.addAttribute("pavlist",dictionaryService.getByParentId("PAV"));
	    	model.addAttribute("boothlist",exhibitionBoothService.findAll());
    	}else if("1".equals(activity.getLocationType())) {
	    	model.addAttribute("exhlist",exhibitionService.findAll());
	    	model.addAttribute("halllist",exhibitionHallService.findAll());
    	}
    	//企业下拉框
    	model.addAttribute("entList",enterpriseService.findAll());
    	
    	model.addAttribute("activity",activity);
		return ACTIVITY_VIEW;
	}
	
	/**
	 * @Description: 修改活动详情界面
	 * @return String
	 * @author Qu, De-Hui
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(Model model, String id) {
		logger.debug("activityId:"+id);
		
		Activity activity =activityService.findOne(id);
    	
    	if(activity.getActivityStartTime() == null || activity.getActivityEndTime() == null) {
    		activity.setActivityTime("");
    	}else {
        	String start=DateFormatUtils.dateToString(activity.getActivityStartTime(), DATE_TIME_FORMAT);
        	String end=DateFormatUtils.dateToString(activity.getActivityEndTime(), DATE_TIME_FORMAT);
        	activity.setActivityTime(start+" - "+end);
    	}

    	/*if("0".equals(activity.getLocationType())) {
	    	model.addAttribute("pavlist",dictionaryService.getByParentId("PAV"));
	    	model.addAttribute("boothlist",exhibitionBoothService.findAll());
    	}else if("1".equals(activity.getLocationType())) {
	    	model.addAttribute("exhlist",exhibitionService.findAll());
	    	model.addAttribute("halllist",exhibitionHallService.findAll());
    	}*/
    	
    	if("0".equals(activity.getLocationType())) {
    		List<Dictionary> pavList=dictionaryService.getByParentId("PAV");
    		if(pavList!=null) {
    		model.addAttribute("pavlist",pavList);
    		}
    	if(activity.getPavilionDic()!=null) {
    		List<ExhibitionBooth> boothList=exhibitionBoothService.findExhibitionBooth(activity.getPavilionDic().getId());
    		if(boothList!=null) {
    		model.addAttribute("boothlist",boothList);
    		}
    	}}else if("1".equals(activity.getLocationType())) {
    		List<Exhibition> exhList=exhibitionService.findAll();
    		if(exhList!=null) {
	    	model.addAttribute("exhlist",exhList);
	    	if(activity.getExhibition()!=null) {
	    		List<ExhibitionHall> hallList=exhibitionHallService.findExhibitionHall(activity.getExhibition().getId());
	    		if(hallList!=null) {
	    			model.addAttribute("halllist",hallList);
	    		}
	    	}
    		}
    	}
    	

    	//企业下拉框
    	if(activity.getBooth()!=null) {
    		List<Enterprise> ent = new ArrayList<Enterprise>();
    		EnterpriseExhibition exh = enterpriseExhibitionService.findByExhibitionId(activity.getBooth().getId());
    		if(exh==null) {
    			model.addAttribute("entList",ent);
    		}else {
    			ent.add(enterpriseService.findOne(exh.getEnterpriseId()));
    			model.addAttribute("entList",ent);
    		}    		
    	}else {
        	model.addAttribute("entList",enterpriseService.findAll());
    	}
    	model.addAttribute("activity",activity);
    	
    	return ACTIVITY_EDIT;
	}
	
	/**
	 * @Description: 根据展台Id去删选举办企业
	 * @return String
	 * @author Qu, De-Hui
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "getEntByExhId", method = RequestMethod.GET)
	@ResponseBody
	public List<Enterprise> getEntByExhId(String exhId) {
		List<Enterprise> ent = null;
		if(StringUtils.isEmpty(exhId)) {
			//返回所有的企业
			ent = enterpriseService.findAll();
		}else {
			//根据展台ID去删选企业
			ent = new ArrayList<Enterprise>();
			EnterpriseExhibition exh = enterpriseExhibitionService.findByExhibitionId(exhId);
			if(exh != null) {
				ent.add(enterpriseService.findOne(exh.getEnterpriseId()));
			}
		}
    	
    	return ent;
	}
}
