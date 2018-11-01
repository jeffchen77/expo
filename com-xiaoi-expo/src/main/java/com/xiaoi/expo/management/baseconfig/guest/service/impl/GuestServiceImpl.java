package com.xiaoi.expo.management.baseconfig.guest.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.constants.Constants;
import com.xiaoi.expo.management.baseconfig.guest.dao.GuestDao;
import com.xiaoi.expo.management.baseconfig.guest.entity.Guest;
import com.xiaoi.expo.management.baseconfig.guest.service.GuestService;

/**
 * @author chen fei
 * @Description: 嘉宾库service实现层
 * @date 2018/3/20 09:20
 */
@Service
public class GuestServiceImpl implements GuestService{
    private Logger logger = LoggerFactory.getLogger(GuestServiceImpl.class);

    @Autowired
    private GuestDao guestDao;

    @Value("${ftp.domain}")
    private String domain;
  
    @Override
    public PageResult<Guest> getGuests(int pageNum, int pageSize, final String searchName) {
        PageResult<Guest> pageResult = new PageResult<Guest>();
        Sort sort = new Sort(Sort.Direction.ASC, new String[]{"orderNum"});
        PageRequest startPage = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<Guest> pageGuest = guestDao.findAll(new Specification<Guest>() {
            @Override
            public Predicate toPredicate(Root<Guest> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(!StringUtils.isEmpty(searchName)){
                	
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchName + "%"));
                    predicates.add(criteriaBuilder.like(root.get("position"), "%" + searchName + "%"));
                    predicates.add(criteriaBuilder.like(root.get("telPhone"), "%" + searchName + "%"));
                    predicates.add(criteriaBuilder.like(root.join("enterprise",JoinType.LEFT).<String>get("name"), "%" + searchName + "%"));
                    if("男".equals(searchName)) {
                    	predicates.add(criteriaBuilder.like(root.get("sex"), "0"));	
                    }else if("女".equals(searchName)) {
                    	predicates.add(criteriaBuilder.like(root.get("sex"), "1"));
                    }
                }
                if(predicates.size()>0) {
                criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));
                }else {
                	criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));	
                }
                return criteriaQuery.getRestriction();
            }
        }, startPage);
        pageResult.setData(pageGuest.getContent());
        pageResult.setCount(pageGuest.getTotalElements());
        return pageResult;
    }
    
    
    @Override
    public MapResult save(Guest guest) {

        try{
        	guestDao.save(guest);
        }catch (Exception e){
            logger.error("保存嘉宾信息出错");
            e.printStackTrace();
            return MapResult.error("保存嘉宾信息出错");
        }

        return MapResult.ok();
    }


    @Override
    public Guest findOne(String guestId) {
        return guestDao.getOne(guestId);
    }
    
    @Override
    public MapResult delete(String guestId) {

        try{
        	guestDao.deleteById(guestId);
        }catch (Exception e){
            logger.error("删除嘉宾信息出错");
            e.printStackTrace();
            return MapResult.error("删除嘉宾信息出错,请先删除会议列表中关联的嘉宾信息");
        }

        return MapResult.ok();
    }
    
    @Override
    public List<Guest> findAll(){
    	return guestDao.findAll();
    }
    
    @Override
	public PageResult<Map<String, Object>> findByPage(int pageNum, int pageSize) {
		PageResult<Map<String, Object>> pageResult = new PageResult<Map<String, Object>>();
		Sort sort = new Sort(Sort.Direction.ASC, new String[]{"orderNum"});
		PageRequest startPage = PageRequest.of(pageNum - 1, pageSize, sort);
		Page<Guest> pageGuests = guestDao.findAll(startPage);
		if(pageGuests.getContent() != null && pageGuests.getContent().size() > 0){
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for(Guest guest: pageGuests.getContent()){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", guest.getName());
				map.put("photo", !StringUtils.isEmpty(guest.getPhotoAddr())?domain+guest.getPhotoAddr():null);
				map.put("position", guest.getPosition());
				mapList.add(map);
			}
			pageResult.setData(mapList);
		}
		pageResult.setCount(pageGuests.getTotalElements());
		int totalPage = (int)Math.ceil((float)pageGuests.getTotalElements()/(float) pageSize);
		pageResult.setTotalPage(totalPage);
		pageResult.setCurrentPage(pageNum);
		pageResult.setCode(Constants.SUCESS_CODE);
		return pageResult;
	}
    
    @Override
    public PageResult<Guest> authFindByPagging(int pageNum, int pageSize, final String searchName) {
        PageResult<Guest> pageResult = new PageResult<Guest>();
        Sort sort = new Sort(Sort.Direction.ASC, new String[]{"signStatus"});
        PageRequest startPage = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<Guest> pageGuest = guestDao.findAll(new Specification<Guest>() {
            @Override
            public Predicate toPredicate(Root<Guest> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(!StringUtils.isEmpty(searchName)){
                	
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchName + "%"));
                    predicates.add(criteriaBuilder.like(root.get("position"), "%" + searchName + "%"));
                    predicates.add(criteriaBuilder.like(root.join("enterprise",JoinType.LEFT).<String>get("name"), "%" + searchName + "%"));
                    if("已认证".equals(searchName)) {
                    	predicates.add(criteriaBuilder.like(root.get("signStatus"), "0"));	
                    }else if("未认证".equals(searchName)) {
                    	predicates.add(criteriaBuilder.like(root.get("signStatus"), "1"));
                    }
                }
                if(predicates.size()>0) {
                criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));
                }else {
                	criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));	
                }
                return criteriaQuery.getRestriction();
            }
        }, startPage);
        pageResult.setData(pageGuest.getContent());
        pageResult.setCount(pageGuest.getTotalElements());
        return pageResult;
    }


	@Override
	public MapResult auth(String authCode) throws Exception{
		List<Guest> guestList = guestDao.findByQrCode(authCode);
		if(guestList!=null && guestList.size()>0) {
			Guest guest = guestList.get(0);
			guest.setSignStatus("0");
			guest.setSignTime(new Date());
			guestDao.save(guest);
			MapResult mapResult = MapResult.ok();
			mapResult.put("authed", 1);
			String name = guest.getName();
			name = !StringUtils.isEmpty(name)? name.substring(0, 1) : "";
			String sex = guest.getSex();
			if("0".equals(sex)) {
				//男，先生
				name+="先生";
			}else{
				//女，女士
				name+="女士";
			}
			mapResult.put("name", name);
			return mapResult;
		}else {
			MapResult mapResult = MapResult.ok();
			mapResult.put("authed", 0);
			return mapResult;
		}
	}
    
}
