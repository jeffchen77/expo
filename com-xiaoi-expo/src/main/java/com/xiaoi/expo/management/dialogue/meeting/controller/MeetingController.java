package com.xiaoi.expo.management.dialogue.meeting.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.common.utils.DateFormatUtils;
import com.xiaoi.expo.management.baseconfig.exhibition.entity.Exhibition;
import com.xiaoi.expo.management.baseconfig.exhibition.service.ExhibitionService;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.entity.ExhibitionBooth;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.service.ExhibitionBoothService;
import com.xiaoi.expo.management.baseconfig.exhibitionhall.entity.ExhibitionHall;
import com.xiaoi.expo.management.baseconfig.exhibitionhall.service.ExhibitionHallService;
import com.xiaoi.expo.management.baseconfig.guest.entity.Guest;
import com.xiaoi.expo.management.baseconfig.guest.service.GuestService;
import com.xiaoi.expo.management.dialogue.meeting.dto.MeetingDto;
import com.xiaoi.expo.management.dialogue.meeting.entity.Meeting;
import com.xiaoi.expo.management.dialogue.meeting.entity.MeetingGuest;
import com.xiaoi.expo.management.dialogue.meeting.service.MeetingService;
import com.xiaoi.expo.management.dictionary.dto.DictionaryDto;
import com.xiaoi.expo.management.dictionary.entity.Dictionary;
import com.xiaoi.expo.management.dictionary.service.DictionaryService;

/**
 * @author chen fei
 * @Description: 会议controller层
 * @date 2018/3/28 09:20
 */
@Controller
@RequestMapping(value = "/management/dialogue/meeting")
public class MeetingController {

    private Logger logger =  LoggerFactory.getLogger(MeetingController.class);
    private static final String LIST = "dialogue/meeting/list"; // 列表页面;
    private static final String ADD = "dialogue/meeting/add"; // 新增页面
    private static final String VIEW = "dialogue/meeting/view"; // 查看页面
    private static final String EDIT = "dialogue/meeting/edit"; // 编辑页面
    private static final String DATE_TIME_FORMAT="yyyy/MM/dd HH:mm";

    @Autowired
    private MeetingService meetingService;
    
    @Autowired
    private DictionaryService dictionaryService;
    
    @Autowired
    private ExhibitionService exhibitionService;
    
    @Autowired
    private ExhibitionHallService exhibitionHallService;
    
    @Autowired
    private ExhibitionBoothService exhibitionBoothService;
    
    @Autowired
    private GuestService guestService;

    
    /**
     * @Description: 加载会议列表页面
     * @return String
     * @author chen fei
     * @date 2018/3/28 09:24
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String meetingList(Model model){
        return LIST;
    }
    
    /**
     * @Description: 跳转会议新增页面
     * @return String
     * @author chen fei
     * @date 2018/3/28 09:24
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model){
    	Meeting meeting =new Meeting();
    	meeting.setTypeDic(new Dictionary());
    	meeting.setExhibition(new Exhibition());
    	meeting.setExhibitionHall(new ExhibitionHall());
    	
    	List<DictionaryDto> mtypDics=new ArrayList<DictionaryDto>();
    	List<Dictionary> dicls=dictionaryService.getByParentId("MTYP");
    	if(dicls!=null) {
    	for(Dictionary dic:dicls) {
    		DictionaryDto dicDto=new DictionaryDto();
    		BeanUtils.copyProperties(dic, dicDto);    		
    		List<Dictionary> subdicls=dictionaryService.getByParentId(dic.getValue());
    		dicDto.setDicList(subdicls);
    		mtypDics.add(dicDto);
    		}
    	}
    	MeetingDto meetingDto=new MeetingDto();
    	BeanUtils.copyProperties(meeting, meetingDto); 
    	model.addAttribute("meeting",meetingDto);
    	model.addAttribute("diclist", mtypDics);
    	
        return ADD;
    }
    
    /**
     * @Description: 跳转会议查看页面
     * @param meetingId 会议id
     * @return String
     * @author chen fei
     * @date 2018/3/28 09:24
     */
    @RequestMapping(value = "view", method = RequestMethod.GET)
    public String view(String meetingId, Model model){
    	logger.debug("meetingId:"+meetingId);
    	Meeting meeting =meetingService.findOne(meetingId);
    	MeetingDto meetingDto=new MeetingDto();
    	BeanUtils.copyProperties(meeting, meetingDto);
    	if(meeting.getMeetingStartTime()==null ||meeting.getMeetingEndTime()==null) {
    		meetingDto.setRang("-");
    	}else {
        	String start=DateFormatUtils.dateToString(meeting.getMeetingStartTime(), DATE_TIME_FORMAT);
        	String end=DateFormatUtils.dateToString(meeting.getMeetingEndTime(), DATE_TIME_FORMAT);
        	meetingDto.setRang(start+" - "+end);
    	}	
    	model.addAttribute("meeting",meetingDto);    
        return VIEW;
    }
    
    /**
     * @Description: 跳转会议编辑页面
     * @param meetingId 会议id
     * @return String
     * @author chen fei
     * @date 2018/3/28 09:24
     */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit(String meetingId, Model model){
    	logger.debug("meetingId:"+meetingId);
    	Meeting meeting =meetingService.findOne(meetingId);
    	MeetingDto meetingDto=new MeetingDto();
    	BeanUtils.copyProperties(meeting, meetingDto);
    	if(meeting.getMeetingStartTime()==null ||meeting.getMeetingEndTime()==null) {
    		meetingDto.setRang("-");
    	}else {
        	String start=DateFormatUtils.dateToString(meeting.getMeetingStartTime(), DATE_TIME_FORMAT);
        	String end=DateFormatUtils.dateToString(meeting.getMeetingEndTime(), DATE_TIME_FORMAT);
        	meetingDto.setRang(start+" - "+end);
    	}
    	
    	    	
    	List<DictionaryDto> mtypDics=new ArrayList<DictionaryDto>();
    	List<Dictionary> dicls=dictionaryService.getByParentId("MTYP");
    	if(dicls!=null) {
    	for(Dictionary dic:dicls) {
    		DictionaryDto dicDto=new DictionaryDto();
    		BeanUtils.copyProperties(dic, dicDto);    		
    		List<Dictionary> subdicls=dictionaryService.getByParentId(dic.getValue());
    		dicDto.setDicList(subdicls);
    		mtypDics.add(dicDto);
    		}
    	}
    	model.addAttribute("meeting",meetingDto);
    	if(mtypDics.size()>0) {
    	model.addAttribute("diclist", mtypDics);
    	}
    	if("0".equals(meeting.getLocationType())) {
    		List<Dictionary> pavList=dictionaryService.getByParentId("PAV");
    		if(pavList!=null) {
    		model.addAttribute("pavlist",pavList);
    		}
    	if(meetingDto.getPavilionDic()!=null) {
    		List<ExhibitionBooth> boothList=exhibitionBoothService.findExhibitionBooth(meetingDto.getPavilionDic().getId());
    		if(boothList!=null) {
    		model.addAttribute("boothlist",boothList);
    		}
    	}
    	}else if("1".equals(meeting.getLocationType())) {
    		List<Exhibition> exhList=exhibitionService.findAll();
    		if(exhList!=null) {
	    	model.addAttribute("exhlist",exhList);
	    	if(meetingDto.getExhibition()!=null) {
	    		List<ExhibitionHall> hallList=exhibitionHallService.findExhibitionHall(meetingDto.getExhibition().getId());
	    		if(hallList!=null) {
	    			model.addAttribute("halllist",hallList);
	    		}
	    	}
    		}
    	}
    	model.addAttribute("guestlist",guestService.findAll());
        return EDIT;
    }
    
    /**
     * @Description: 分页查询会议信息
     * @param page 页码
     * @param  limit 每页显示条数
     * @param searchName 搜索关键词
     * @return PageResult<Meeting>
     * @author chen fei
     * @date 2018/3/28 09:24
     */
    @RequestMapping(value = "findByPagging", method = RequestMethod.GET)
    @ResponseBody
    public PageResult<MeetingDto> findByPagging(Integer page, Integer limit, String searchName){
        if(page == null) {
            page = 0;
            limit = 10;
        }
        logger.debug("searchName:"+ searchName);
        PageResult<Meeting> pageResult= meetingService.getMeetings(page, limit, searchName);
        List<MeetingDto> list=new ArrayList<MeetingDto>();
        if(pageResult.getData()!=null) {
        for(Meeting meeting:pageResult.getData()) {
        	MeetingDto meetingDto=new MeetingDto();
        	BeanUtils.copyProperties(meeting, meetingDto);
        	if(meeting.getMeetingStartTime()==null ||meeting.getMeetingEndTime()==null) {
        		meetingDto.setRang("-");
        		meetingDto.setStatus("-");
        	}else {
	        	String start=DateFormatUtils.dateToString(meeting.getMeetingStartTime(), DATE_TIME_FORMAT);
	        	String end=DateFormatUtils.dateToString(meeting.getMeetingEndTime(), DATE_TIME_FORMAT);
	        	meetingDto.setRang(start+" - "+end);
	        	int status=DateFormatUtils.comparedDate(start, end, DATE_TIME_FORMAT);
	        	if(status==0) {
	        		meetingDto.setStatus("未开始");
	        	}else if(status==1) {
	        		meetingDto.setStatus("进行中");
	        	}else if(status==2) {
	        		meetingDto.setStatus("已结束");
	        	}else {
	        		meetingDto.setStatus("-");
	        	}
        	}
        	
        	list.add(meetingDto);
        }
        }
        PageResult<MeetingDto> pageRes = new PageResult<MeetingDto>();
        pageRes.setData(list);
        pageRes.setCount(pageResult.getCount());
        return pageRes;
    }

    /**
     * @Description: 保存会议信息
     * @param meeting 会议信息
     * @return MapResult
     * @author chen fei
     * @date 2018/3/28 09:24
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public MapResult save(MeetingDto meetingDto){
        Meeting oldMeeting = null;
        if(!StringUtils.isEmpty(meetingDto.getId())){
            oldMeeting = meetingService.findOne(meetingDto.getId());
            if(oldMeeting == null){
                return MapResult.error("会议不存在或已被删除");
            }            
            oldMeeting.setTheme(meetingDto.getTheme());
            oldMeeting.setTypeDic(meetingDto.getTypeDic());
            oldMeeting.setTotalPeople(meetingDto.getTotalPeople());
            if(!StringUtils.isEmpty(meetingDto.getRang())) {
            	String start=meetingDto.getRang().split("-")[0];
        		String end=meetingDto.getRang().split("-")[1];
        		try {
            oldMeeting.setMeetingStartTime(DateFormatUtils.stringToDate(start, DATE_TIME_FORMAT));
            oldMeeting.setMeetingEndTime(DateFormatUtils.stringToDate(end, DATE_TIME_FORMAT));
        		}catch(Exception ex) {
        			return MapResult.error("会议时间格式有误");
        		}
            }
            oldMeeting.setLocationType(meetingDto.getLocationType());
            oldMeeting.setExhibition(meetingDto.getExhibition());
            oldMeeting.setExhibitionHall(meetingDto.getExhibitionHall());
            oldMeeting.setPavilionDic(meetingDto.getPavilionDic());
            oldMeeting.setBooth(meetingDto.getBooth());   
            oldMeeting.setDescription(meetingDto.getDescription());
            Set<Guest> guests=new HashSet<Guest>();
            if(meetingDto.getGuestArr()!=null) {
            for(String guestId:meetingDto.getGuestArr()) {
            	if(!StringUtils.isEmpty(guestId)) {
            	Guest guest=guestService.findOne(guestId);            	
            	guests.add(guest);
            	}
            }
            }
            oldMeeting.setGuests(guests);
            
            oldMeeting.setUpdateTime(new Date());      
            
        }else{ 
        	oldMeeting=new Meeting();
            oldMeeting.setTheme(meetingDto.getTheme());
            oldMeeting.setTypeDic(meetingDto.getTypeDic());
            oldMeeting.setTotalPeople(meetingDto.getTotalPeople());
            if(!StringUtils.isEmpty(meetingDto.getRang())) {
            	String start=meetingDto.getRang().split("-")[0];
        		String end=meetingDto.getRang().split("-")[1];
        		try {
            oldMeeting.setMeetingStartTime(DateFormatUtils.stringToDate(start, DATE_TIME_FORMAT));
            oldMeeting.setMeetingEndTime(DateFormatUtils.stringToDate(end, DATE_TIME_FORMAT));
        		}catch(Exception ex) {
        			return MapResult.error("会议时间格式有误");
        		}
            }
            oldMeeting.setLocationType(meetingDto.getLocationType());
            oldMeeting.setExhibition(meetingDto.getExhibition());
            oldMeeting.setExhibitionHall(meetingDto.getExhibitionHall());
            oldMeeting.setPavilionDic(meetingDto.getPavilionDic());
            oldMeeting.setBooth(meetingDto.getBooth());  
            oldMeeting.setDescription(meetingDto.getDescription());
            Set<Guest> guests=new HashSet<Guest>();
            if(meetingDto.getGuestArr()!=null) {
            for(String guestId:meetingDto.getGuestArr()) {
            	if(!StringUtils.isEmpty(guestId)) {
            	Guest guest=guestService.findOne(guestId);            	
            	guests.add(guest);
            	}
            }
            }
            oldMeeting.setGuests(guests);        	
        	
        	oldMeeting.setCreateTime(new Date());
        	oldMeeting.setUpdateTime(new Date());
        	
        }
        return meetingService.save(oldMeeting);
    }

  

    /**
     * @Description: 删除会议
     * @param meetingId 会议id
     * @return MapResult
     * @author chen fei
     * @date 2018/3/28 09:24
     */
    @RequestMapping(value = "deleteMeeting", method = RequestMethod.POST)
    @ResponseBody
    public MapResult deleteMeeting(String meetingId){
        Meeting meeting = meetingService.findOne(meetingId);
        if(meeting == null){
            return MapResult.error("会议不存在或已被删除");
        }
        return meetingService.delete(meetingId);        
    }
    
    /**
	 * @Description: 返回所有的会议信息，不分页
	 * @return List<Meeting>
	 * @author chen,fei
	 * @date 2018/3/28
	 */
	@RequestMapping(value = "findAll", method = RequestMethod.GET)
	@ResponseBody
	public List<Meeting> findAll() {
		return meetingService.findAll();
	}
	
	 /**
     * @Description: 分页查询会议下嘉宾信息
     * @param page 页码
     * @param  limit 每页显示条数
     * @param searchName 搜索关键词
     * @return PageResult<Meeting>
     * @author chen fei
     * @date 2018/3/28 09:24
     */
    @RequestMapping(value = "findGuestByPagging", method = RequestMethod.GET)
    @ResponseBody
    public PageResult<MeetingGuest> findGuestByPagging(Integer page, Integer limit,String meetingId, String searchName){
        if(page == null) {
            page = 0;
            limit = 10;
        }
        logger.debug("searchName:"+ searchName);
        PageResult<MeetingGuest> pageResult= meetingService.getMeetingGuest(page, limit,meetingId, searchName);
       
        return pageResult;
    }
	   
}
