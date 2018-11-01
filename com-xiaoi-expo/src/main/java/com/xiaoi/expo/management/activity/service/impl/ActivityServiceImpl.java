package com.xiaoi.expo.management.activity.service.impl;
import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.common.utils.EncryptptUtils;
import com.xiaoi.expo.management.activity.dao.ActivityDao;
import com.xiaoi.expo.management.activity.entity.Activity;
import com.xiaoi.expo.management.activity.service.ActivityService;
import com.xiaoi.expo.management.baseconfig.enterprise.entity.Enterprise;
import com.xiaoi.expo.management.baseconfig.exhibition.entity.Exhibition;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.entity.ExhibitionBooth;
import com.xiaoi.expo.management.baseconfig.exhibitionhall.entity.ExhibitionHall;
import com.xiaoi.expo.management.dictionary.entity.Dictionary;
import com.xiaoi.expo.management.user.dao.UserDao;
import com.xiaoi.expo.management.user.entity.User;
import com.xiaoi.expo.management.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Qu, De-Hui
 * @Description: ${todo}
 * @date 2018/3/1217:00
 */
@Service
public class ActivityServiceImpl implements ActivityService{
    private Logger logger = LoggerFactory.getLogger(ActivityServiceImpl.class);

    @Autowired
    private ActivityDao activityDao;

    @Override
    public PageResult<Activity> getActivity(int pageNum, int pageSize, final String searchName) {
        PageResult<Activity> pageResult = new PageResult<Activity>();
        Sort sort = new Sort(Sort.Direction.DESC, new String[]{"updateTime"});
        PageRequest startPage = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<Activity> pageUser = activityDao.findAll(new Specification<Activity>() {
            @Override
            public Predicate toPredicate(Root<Activity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(!StringUtils.isEmpty(searchName)){                	
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchName + "%"));//根据活动名称筛选
                    //predicates.add(criteriaBuilder.like(root.<Dictionary>get("pavilionDic").<String>get("displayName"), "%" + searchName + "%"));//根据展馆名称筛选
                    //predicates.add(criteriaBuilder.like(root.<ExhibitionBooth>get("booth").<String>get("code"), "%" + searchName + "%"));//根据展台编号筛选
                    //predicates.add(criteriaBuilder.like(root.<Exhibition>get("exhibition").<String>get("name"), "%" + searchName + "%"));//根会场名称筛选
                    //predicates.add(criteriaBuilder.like(root.<ExhibitionHall>get("exhibitionHall").<String>get("name"), "%" + searchName + "%"));//根会议厅名称筛选
                    //predicates.add(criteriaBuilder.like(root.<Enterprise>get("enterprise").<String>get("name"), "%" + searchName + "%"));//根会企业名称筛选
                    
                    Join<Activity,Dictionary> pavilionDicJoin = root.join("pavilionDic",JoinType.LEFT);
                	predicates.add(criteriaBuilder.like(pavilionDicJoin.get("displayName"), "%" + searchName + "%"));
                	
                	Join<Activity,ExhibitionBooth> exhibitionBoothJoin = root.join("booth",JoinType.LEFT);
                	predicates.add(criteriaBuilder.like(exhibitionBoothJoin.get("code"), "%" + searchName + "%"));
                	
                	Join<Activity,Exhibition> exhibitionJoin = root.join("exhibition",JoinType.LEFT);
                	predicates.add(criteriaBuilder.like(exhibitionJoin.get("name"), "%" + searchName + "%"));
                	
                	Join<Activity,ExhibitionHall> exhibitionHallJoin = root.join("exhibitionHall",JoinType.LEFT);
                	predicates.add(criteriaBuilder.like(exhibitionHallJoin.get("name"), "%" + searchName + "%"));
                	
                	Join<Activity,Enterprise> enterpriseJoin = root.join("enterprise",JoinType.LEFT);
                	predicates.add(criteriaBuilder.like(enterpriseJoin.get("name"), "%" + searchName + "%"));
                }
                if(predicates.size()>0) {
                    criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));
                }
                return criteriaQuery.getRestriction();
            }
        }, startPage);
        pageResult.setData(pageUser.getContent());
        pageResult.setCount(pageUser.getTotalElements());
        return pageResult;
    }

    @Override
    public MapResult save(Activity activity) {
        try{
        	activityDao.save(activity);
        }catch (Exception e){
            logger.error("保存活动信息出错");
            e.printStackTrace();
            return MapResult.error("保存活动信息出错");
        }

        return MapResult.ok();
    }

    @Override
    public Activity findOne(String activityId) {
        return activityDao.getOne(activityId);
    }

	@Override
	public MapResult delete(String activityId) {
		try{
			activityDao.deleteById(activityId);
        }catch (Exception e){
            logger.error("删除活动信息出错");
            e.printStackTrace();
            return MapResult.error("删除活动信息出错");
        }

        return MapResult.ok();
	}

	@Override
	public List<Activity> findAllByExhibitionId(String exhId) {
		return activityDao.findAllByBooth_Id(exhId);
	}
	
	@Override
	public List<Activity> findAllByPavilionIdAndDate(String pavId, String date) {
		return activityDao.findAllActivityWithEnterprise(pavId, date);
	}

	@Override
	public List<Activity> findAllByPavilionIdAndEntIdAndDate(String pavId, String entId, Date curDate) {
		return activityDao.findAllByPavilionDic_IdAndEnterprise_IdAndActivityEndTimeGreaterThanOrderByActivityEndTime(pavId, entId, curDate);
	}
    
}
