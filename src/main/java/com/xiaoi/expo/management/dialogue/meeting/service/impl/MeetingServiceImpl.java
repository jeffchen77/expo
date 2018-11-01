package com.xiaoi.expo.management.dialogue.meeting.service.impl;
import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.common.utils.DateFormatUtils;
import com.xiaoi.expo.constants.Constants;
import com.xiaoi.expo.management.baseconfig.guest.entity.Guest;
import com.xiaoi.expo.management.dialogue.meeting.dao.MeetingDao;
import com.xiaoi.expo.management.dialogue.meeting.dao.MeetingGuestDao;
import com.xiaoi.expo.management.dialogue.meeting.entity.Meeting;
import com.xiaoi.expo.management.dialogue.meeting.entity.MeetingGuest;
import com.xiaoi.expo.management.dialogue.meeting.service.MeetingService;
import com.xiaoi.expo.management.dictionary.entity.Dictionary;

/**
 * @author chen fei
 * @Description: 会议service实现层
 * @date 2018/3/28 09:20
 */
@Service
public class MeetingServiceImpl implements MeetingService{
    private Logger logger = LoggerFactory.getLogger(MeetingServiceImpl.class);

    @Autowired
    private MeetingDao meetingDao;
    
    @Autowired
    private MeetingGuestDao meetingGuestDao;
    
    private static final String DATE_TIME_FORMAT="yyyy/MM/dd HH:mm";
    
    @Value("${ftp.domain}")
    private String domain;
      
    @Override
    public PageResult<Meeting> getMeetings(int pageNum, int pageSize, final String searchName) {
        PageResult<Meeting> pageResult = new PageResult<Meeting>();
        Sort sort = new Sort(Sort.Direction.ASC, new String[]{"theme"});
        PageRequest startPage = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<Meeting> pageMeeting = meetingDao.findAll(new Specification<Meeting>() {
            @Override
            public Predicate toPredicate(Root<Meeting> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                Predicate pre=null;
                if(!StringUtils.isEmpty(searchName)){
                	if(searchName.trim().equalsIgnoreCase("未开始")) {
                		pre=criteriaBuilder.greaterThan(root.<Date>get("meetingStartTime"),new Date());
                    }else if(searchName.trim().equalsIgnoreCase("进行中")){
                    	pre=criteriaBuilder.greaterThan(root.<Date>get("meetingEndTime"),new Date());
                    	pre=criteriaBuilder.and(pre,criteriaBuilder.lessThan(root.<Date>get("meetingStartTime"),new Date()));
                    }else if(searchName.trim().equalsIgnoreCase("已结束")){
                    	pre=criteriaBuilder.lessThan(root.<Date>get("meetingEndTime"),new Date());
                    }else {
                    predicates.add(criteriaBuilder.like(root.get("theme"), "%" + searchName + "%"));
                    predicates.add(criteriaBuilder.like(root.get("totalPeople"), "%" + searchName + "%"));
                    predicates.add(criteriaBuilder.like(root.<Dictionary>get("typeDic").<String>get("displayName"), "%" + searchName + "%"));
                    }
                }
                if(predicates.size()>0) {                	
                    criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));
                   }else {
                	   if(pre!=null) {
                		   criteriaQuery.where(pre);   
                	   }else {
                		   criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
                	   }
                   	
                   }
                return criteriaQuery.getRestriction();
            }
        }, startPage);
        pageResult.setData(pageMeeting.getContent());
        pageResult.setCount(pageMeeting.getTotalElements());
        return pageResult;
    }

    @Override
    public MapResult save(Meeting meeting) {

        try{
        	meetingDao.save(meeting);
        }catch (Exception e){
            logger.error("保存会议信息出错");
            e.printStackTrace();
            return MapResult.error("保存会议信息出错");
        }

        return MapResult.ok();
    }


    @Override
    public Meeting findOne(String meetingId) {
        return meetingDao.getOne(meetingId);
    }
    
    @Override
    public MapResult delete(String meetingId) {

        try{
        	meetingDao.deleteById(meetingId);
        }catch (Exception e){
            logger.error("删除会议信息出错");
            e.printStackTrace();
            return MapResult.error("删除会议信息出错");
        }

        return MapResult.ok();
    }
    
    @Override
    public List<Meeting> findAll(){
    	return meetingDao.findAll();
    }
    
    @Override
    public PageResult<MeetingGuest> getMeetingGuest(int pageNum, int pageSize,final String meetingId, final String searchName) {
    	PageResult<MeetingGuest> pageResult = new PageResult<MeetingGuest>();
        Sort sort = new Sort(Sort.Direction.ASC, new String[]{"guest.name"});
        PageRequest startPage = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<MeetingGuest> pageMeeting = meetingGuestDao.findAll(new Specification<MeetingGuest>() {
            @Override
            public Predicate toPredicate(Root<MeetingGuest> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();   
                Predicate pre=criteriaBuilder.and(criteriaBuilder.equal(root.<Meeting>get("meeting").<String>get("id"), meetingId));
                
                if(!StringUtils.isEmpty(searchName)){
                	if(searchName.trim().equalsIgnoreCase("已认证")) {
                    	pre=criteriaBuilder.and(pre,criteriaBuilder.equal(root.join("guest").<String>get("signStatus"), "0"));
                    }else if(searchName.trim().equalsIgnoreCase("未认证")){
                    	pre=criteriaBuilder.and(pre,criteriaBuilder.equal(root.join("guest").<String>get("signStatus"), "1"));
                    }else {
                    	predicates.add(criteriaBuilder.like(root.join("guest",JoinType.LEFT).<String>get("name"), "%" + searchName + "%"));
                    predicates.add(criteriaBuilder.like(root.join("guest",JoinType.LEFT).join("enterprise",JoinType.LEFT).<String>get("name"), "%" + searchName.trim() + "%"));
                    predicates.add(criteriaBuilder.like(root.join("guest",JoinType.LEFT).<String>get("position"), "%" + searchName.trim() + "%"));
                    }
                    
                }
                
                if(predicates.size()>0) {
                criteriaQuery.where(criteriaBuilder.and(pre,criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]))));
                }else {
                	criteriaQuery.where(pre);	
                }
                return criteriaQuery.getRestriction();
            }
        }, startPage);
        pageResult.setData(pageMeeting.getContent());
        pageResult.setCount(pageMeeting.getTotalElements());
        return pageResult;
    }
    
    @Override
	public PageResult<Map<String, Object>> findByPage(int pageNum, int pageSize, String date) throws Exception {
		PageResult<Map<String, Object>> pageResult = new PageResult<Map<String, Object>>();
		//根据会议开始时间升序排序
		Sort sort = new Sort(Sort.Direction.ASC, new String[]{"meetingStartTime","meetingEndTime"});
		Date begin = DateFormatUtils.stringToDate(date + " 00:00:00", DateFormatUtils.DATE_FORMAT_YYYYMMDDHHMMSS);
		Date end = DateFormatUtils.stringToDate(date + " 23:59:59", DateFormatUtils.DATE_FORMAT_YYYYMMDDHHMMSS);
		Pageable startPage = PageRequest.of(pageNum - 1, pageSize, sort);
		Page<Meeting> pageMeeting = meetingDao.findByMeetingStartTimeBetween(begin, end, startPage);
		
		if(pageMeeting.getContent() != null && pageMeeting.getContent().size() > 0){
			List<Meeting> meetings = pageMeeting.getContent();
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			
			for(int i=0; i<meetings.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();				
				//会议类别
				String preType = meetings.get(i).getTypeDic()!=null?meetings.get(i).getTypeDic().getDisplayName():"";
				map.put("meetingType", preType);
				List<Map<String, Object>> innerList = new ArrayList<Map<String, Object>>();
				innerList.add(getMeetingMap(meetings.get(i)));
				for(int j=i+1; j<meetings.size(); j++) {
					Meeting innerMeeting = meetings.get(j);
					if(innerMeeting.getTypeDic()==null || preType.equalsIgnoreCase(innerMeeting.getTypeDic().getDisplayName())) {
						innerList.add(getMeetingMap(innerMeeting));
						i = j;
					}else {
						i = j - 1;
						break;
					}
				}
				map.put("meetings", innerList);
				mapList.add(map);
			}
			pageResult.setData(mapList);			
		}
		pageResult.setCount(pageMeeting.getTotalElements());
		int totalPage = (int)Math.ceil((float)pageMeeting.getTotalElements()/(float) pageSize);
		pageResult.setTotalPage(totalPage);
		pageResult.setCurrentPage(pageNum);
		pageResult.setCode(Constants.SUCESS_CODE);
		return pageResult;
	}

	private Map<String, Object> getMeetingMap(Meeting meeting){
    	Map<String, Object> map = new HashMap<String, Object>();
    	//主键ID
		map.put("id", meeting.getId());
		//会议主题
		map.put("name", meeting.getTheme());
		//会议地址，分为展馆和会场, 0代表展馆，1代表会场
		String address = "未知";
		if(!StringUtils.isEmpty(meeting.getLocationType())) {
			if("0".equals(meeting.getLocationType())) {
				//地址为展馆
				address = (meeting.getPavilionDic() !=null ? meeting.getPavilionDic().getDisplayName() : "") + " " + (meeting.getBooth() !=null ? meeting.getBooth().getCode() : "");								
			}else if("1".equals(meeting.getLocationType())){
			//地址为会场
			address = (meeting.getExhibition() !=null ? meeting.getExhibition().getName() : "") + " " + (meeting.getExhibitionHall() !=null ? meeting.getExhibitionHall().getName() : "");	
			}
		}
		map.put("address", address);
		
		//会议时间
		String meetingTime = "未知";
		if(meeting.getMeetingStartTime()!=null && meeting.getMeetingEndTime()!=null) {
			meetingTime = DateFormatUtils.dateToString(meeting.getMeetingStartTime(), "HH:mm") + "-" + DateFormatUtils.dateToString(meeting.getMeetingEndTime(), "HH:mm");
			String start=DateFormatUtils.dateToString(meeting.getMeetingStartTime(), DATE_TIME_FORMAT);
        	String end=DateFormatUtils.dateToString(meeting.getMeetingEndTime(), DATE_TIME_FORMAT);
        	int status=DateFormatUtils.comparedDate(start, end, DATE_TIME_FORMAT);
        	map.put("status", status);
		}else {			
        	map.put("status", -1);
		}
		map.put("meetingTime", meetingTime);
		
		//会议嘉宾
		Set<Guest> guests = meeting.getGuests();
		String guest = "";
		int time = 1;//只拿前面3个嘉宾
		if(guests != null && !guests.isEmpty()) {
			for(Guest g : guests) {
				if(time > 3) {
					break;
				}	
				guest += (g.getName() + "、");
				time++;
			}
		}
		if(!StringUtils.isEmpty(guest)) {
			map.put("guest", guest.substring(0, guest.length()-"、".length())+"等");
		}else {
			map.put("guest", "未知");
		}
    	return map;
    }


	@Override
	public PageResult<Map<String, Object>> findMeetingDates(String hallId) throws Exception {
		List<Object> dates = meetingDao.findMeetingDates(hallId);
		PageResult<Map<String, Object>> pageResult = new PageResult<Map<String, Object>>();
		List<Map<String, Object>> dateList = new ArrayList<Map<String, Object>>();
		for(Object obj : dates){
			if(obj != null){
				Map<String, Object> map = new HashMap<String, Object>();
				Date d1 = DateFormatUtils.stringToDate(String.valueOf(obj), DateFormatUtils.DATE_FORMAT_YYYYMMDD);
				map.put("dateString", DateFormatUtils.dateToString(d1, "MM月dd日"));
				map.put("date", DateFormatUtils.dateToString(d1, DateFormatUtils.DATE_FORMAT_YYYYMMDD));
				dateList.add(map);
			}
		}
		pageResult.setData(dateList);
		pageResult.setCode(Constants.SUCESS_CODE);
		return pageResult;
	}

	@Override
	public PageResult<Map<String, Object>> findByPageHall(int pageNum, int pageSize, String date, String hallId) throws Exception {
		PageResult<Map<String, Object>> pageResult = new PageResult<Map<String, Object>>();
		//根据会议开始时间升序排序
		Sort sort = new Sort(Sort.Direction.ASC, new String[]{"meetingStartTime","meetingEndTime"});
		Date begin = DateFormatUtils.stringToDate(date + " 00:00:00", DateFormatUtils.DATE_FORMAT_YYYYMMDDHHMMSS);
		Date end = DateFormatUtils.stringToDate(date + " 23:59:59", DateFormatUtils.DATE_FORMAT_YYYYMMDDHHMMSS);
		Pageable startPage = PageRequest.of(pageNum - 1, pageSize, sort);
		Page<Meeting> pageMeeting = meetingDao.findAll(new Specification<Meeting>() {
			@Override
			public Predicate toPredicate(Root<Meeting> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(!StringUtils.isEmpty(hallId)){
					predicates.add(criteriaBuilder.equal(root.get("exhibitionHall").get("id"), hallId));
				}

				predicates.add(criteriaBuilder.between(root.get("meetingStartTime"), begin, end));
				criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
				return criteriaQuery.getRestriction();
			}
		}, startPage);

		List<Map<String, Object>> meetingList = new ArrayList<Map<String, Object>>();
		if(pageMeeting.getContent() != null && pageMeeting.getContent().size() > 0){
			for(Meeting meeting : pageMeeting.getContent()){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", meeting.getId());
				map.put("theme", meeting.getTheme());
				StringBuffer sb = new StringBuffer();
				if(meeting.getMeetingStartTime() != null){
					sb.append(DateFormatUtils.dateToString(meeting.getMeetingStartTime(), DateFormatUtils.DATE_FORMAT_HHMM));
				}
				if(meeting.getMeetingEndTime() != null){
					sb.append(" - ");
					sb.append(DateFormatUtils.dateToString(meeting.getMeetingEndTime(), DateFormatUtils.DATE_FORMAT_HHMM));
				}

				map.put("time", sb.toString());
				map.put("isDoing", DateFormatUtils.comparedDate(meeting.getMeetingStartTime(), meeting.getMeetingEndTime()));
				meetingList.add(map);
			}
		}

		if(meetingList.size() > 0){
			pageResult.setData(meetingList);
			pageResult.setCode(Constants.SUCESS_CODE);
			pageResult.setCount(pageMeeting.getTotalElements());
		}
		return pageResult;
	}
	
	@Override
	public PageResult<Map<String, Object>> getAllMeetingDates() throws Exception {
		List<Object> dates = meetingDao.getAllMeetingDates();
		PageResult<Map<String, Object>> pageResult = new PageResult<Map<String, Object>>();
		List<Map<String, Object>> dateList = new ArrayList<Map<String, Object>>();
		for(Object obj : dates){
			if(obj != null){
				Map<String, Object> map = new HashMap<String, Object>();
				Date d1 = DateFormatUtils.stringToDate(String.valueOf(obj), DateFormatUtils.DATE_FORMAT_YYYYMMDD);
				map.put("dateString", DateFormatUtils.dateToString(d1, "MM月dd日"));
				map.put("date", DateFormatUtils.dateToString(d1, DateFormatUtils.DATE_FORMAT_YYYYMMDD));
				dateList.add(map);
			}
			pageResult.setCount(dates.size());
		}
		pageResult.setData(dateList);
		pageResult.setCode(Constants.SUCESS_CODE);
		return pageResult;
	}

	@Override
	public MapResult meetingDetail(String meetingId) throws Exception{
		Meeting meeting = meetingDao.getOne(meetingId);
		if(meeting!=null) {
			MapResult mapResult = MapResult.ok();
			mapResult.put("meetingDesc", meeting.getDescription());
			List<Map<String, Object>> guests = new ArrayList<Map<String, Object>>();
			Set<Guest> person = meeting.getGuests();
			if(person!=null && !person.isEmpty()) {
				for(Guest onePerson : person) {
					Map<String, Object> per = new HashMap<String ,Object>();
					per.put("name", !StringUtils.isEmpty(onePerson.getName())?onePerson.getName():"");
					per.put("position", !StringUtils.isEmpty(onePerson.getPosition())?onePerson.getPosition():"");
					per.put("photo", !StringUtils.isEmpty(onePerson.getPhotoAddr())?domain+onePerson.getPhotoAddr():"");
					guests.add(per);
				}
			}
			mapResult.put("guests", guests);
			return mapResult;
		}else {
			return MapResult.error("查询会议详情出错");
		}
	}

	@Override
	public PageResult<Map<String, Object>> hallMeetings(String date, String hallId) throws Exception {
		PageResult<Map<String, Object>> pageResult = new PageResult<Map<String, Object>>();
		//根据会议开始时间升序排序
		Sort sort = new Sort(Sort.Direction.ASC, new String[]{"meetingStartTime","meetingEndTime"});
		Date begin = DateFormatUtils.stringToDate(date + " 00:00:00", DateFormatUtils.DATE_FORMAT_YYYYMMDDHHMMSS);
		Date end = DateFormatUtils.stringToDate(date + " 23:59:59", DateFormatUtils.DATE_FORMAT_YYYYMMDDHHMMSS);
		List<Meeting> pageMeeting = meetingDao.findAll(new Specification<Meeting>() {
			@Override
			public Predicate toPredicate(Root<Meeting> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(!StringUtils.isEmpty(hallId)){
					predicates.add(criteriaBuilder.equal(root.get("exhibitionHall").get("id"), hallId));
				}

				predicates.add(criteriaBuilder.between(root.get("meetingStartTime"), begin, end));
				criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
				return criteriaQuery.getRestriction();
			}
		}, sort);

		List<Map<String, Object>> meetingList = new ArrayList<Map<String, Object>>();
		if(pageMeeting != null && pageMeeting.size() > 0){
			for(Meeting meeting : pageMeeting){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", meeting.getId());
				map.put("theme", meeting.getTheme());
				map.put("picture", StringUtils.isEmpty(meeting.getExhibitionHall().getPicture())? domain + meeting.getExhibitionHall().getPicture(): null);
				StringBuffer sb = new StringBuffer();
				if(meeting.getMeetingStartTime() != null){
					sb.append(DateFormatUtils.dateToString(meeting.getMeetingStartTime(), DateFormatUtils.DATE_FORMAT_HHMM));
				}
				if(meeting.getMeetingEndTime() != null){
					sb.append(" - ");
					sb.append(DateFormatUtils.dateToString(meeting.getMeetingEndTime(), DateFormatUtils.DATE_FORMAT_HHMM));
				}

				map.put("time", sb.toString());
				map.put("isDoing", DateFormatUtils.comparedDate(meeting.getMeetingStartTime(), meeting.getMeetingEndTime()));
				meetingList.add(map);
			}
		}

		if(meetingList.size() > 0){
			pageResult.setData(meetingList);
			pageResult.setCode(Constants.SUCESS_CODE);
			pageResult.setCount(pageMeeting.size());
		}
		return pageResult;
	}

	@Override
	public List<Map<String, Object>> findByType(String types, int floor) {
		String[] typeAttr = types.split(",");
		List<Map<String, Object>> meetings = meetingDao.getLastMeetings(Arrays.asList(typeAttr), floor);
		List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
		if(meetings != null && meetings.size() > 0){
			for(Map<String, Object> obj: meetings){
				if(obj.get("id") != null){
					Map<String, Object> temp = new HashMap<String, Object>();
					temp.put("id", obj.get("id"));
					temp.put("theme", obj.get("theme"));
					temp.put("floor", obj.get("floor"));
					temp.put("x", obj.get("x"));
					temp.put("y", obj.get("y"));
					temp.put("hallId", obj.get("hall_id"));
					temp.put("hallName", obj.get("hallName"));
					if(!StringUtils.isEmpty(obj.get("picture"))){
						temp.put("picture", domain + obj.get("picture").toString());
					}
					Meeting meeting = meetingDao.getOne(obj.get("id").toString());

					Set<Guest> guests = meeting.getGuests();
					Iterator<Guest> it = guests.iterator();
					if(guests != null){
						List<String> guestImgs = new ArrayList<String>();
						int i=0;
						while(it.hasNext()){
							Guest guest = it.next();
							if(!StringUtils.isEmpty(guest.getPhotoAddr())){
								guestImgs.add(domain + guest.getPhotoAddr());
								i ++;
							}

							if(i >= 2){
								break;
							}
						}
						temp.put("guestImgs", guestImgs);

						StringBuffer sb = new StringBuffer();
						if(obj.get("start_time") != null){
							String MMdd = DateFormatUtils.dateToString((Date) obj.get("start_time"), "MM月dd");
							sb.append(MMdd + " ");
							sb.append(DateFormatUtils.dateToString((Date)obj.get("start_time"), DateFormatUtils.DATE_FORMAT_HHMM));
						}
						if(obj.get("end_time") != null){
							sb.append(" - ");
							sb.append(DateFormatUtils.dateToString((Date)obj.get("end_time"), DateFormatUtils.DATE_FORMAT_HHMM));
						}

						temp.put("time", sb.toString());
						if(DateFormatUtils.comparedDate((Date) obj.get("start_time"), (Date) obj.get("end_time")) == 0){
							temp.put("isDoing", "即将开始");
						}else if(DateFormatUtils.comparedDate((Date) obj.get("start_time"), (Date) obj.get("end_time")) == 1){
							temp.put("isDoing", "进行中");
						}else{
							temp.put("isDoing", "已结束");
						}
					}
					resultMap.add(temp);
				}
			}
		}

		return resultMap;
	}

	@Override
	public Map<String, Object> findByHallName(String hallName) {
		List<Map<String, Object>> meetings = meetingDao.getLastMeetingsByHallName(hallName);
		Map<String, Object> meetingMap = new HashMap<String, Object>();
		if (meetings != null && meetings.size() > 0) {
			Map<String, Object> obj = meetings.get(0);
			meetingMap.put("id", obj.get("id"));
			meetingMap.put("theme", obj.get("theme"));
			meetingMap.put("floor", obj.get("floor"));
			meetingMap.put("x", obj.get("x"));
			meetingMap.put("y", obj.get("y"));
			meetingMap.put("hallId", obj.get("hall_id"));
			meetingMap.put("hallName", obj.get("hallName"));
			if(!StringUtils.isEmpty(obj.get("picture"))){
				meetingMap.put("picture", domain + obj.get("picture").toString());
			}

			String id = obj.get("id").toString();
			Meeting meeting = meetingDao.getOne(id);
			Set<Guest> guests = meeting.getGuests();
			Iterator<Guest> it = guests.iterator();
			if (guests != null) {
				List<String> guestImgs = new ArrayList<String>();
				int i = 0;
				while (it.hasNext()) {
					Guest guest = it.next();
					if (!StringUtils.isEmpty(guest.getPhotoAddr())) {
						guestImgs.add(domain + guest.getPhotoAddr());
						i++;
					}

					if (i >= 2) {
						break;
					}
				}
				meetingMap.put("guestImgs", guestImgs);

				StringBuffer sb = new StringBuffer();
				if (obj.get("start_time") != null) {
					String MMdd = DateFormatUtils.dateToString((Date) obj.get("start_time"), "MM月dd");
					sb.append(MMdd + " ");
					sb.append(DateFormatUtils.dateToString((Date) obj.get("start_time"), DateFormatUtils.DATE_FORMAT_HHMM));
				}
				if (obj.get("end_time") != null) {
					sb.append(" - ");
					sb.append(DateFormatUtils.dateToString((Date) obj.get("end_time"), DateFormatUtils.DATE_FORMAT_HHMM));
				}

				meetingMap.put("time", sb.toString());
				if(DateFormatUtils.comparedDate((Date) obj.get("start_time"), (Date) obj.get("end_time")) == 0){
					meetingMap.put("isDoing", "即将开始");
				}else if(DateFormatUtils.comparedDate((Date) obj.get("start_time"), (Date) obj.get("end_time")) == 1){
					meetingMap.put("isDoing", "进行中");
				}else{
					meetingMap.put("isDoing", "已结束");
				}

			}

		}
		return meetingMap;
	}

}
