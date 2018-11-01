package com.xiaoi.expo.management.baseconfig.exhibitionbooth.service.impl;
import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.management.activity.entity.Activity;
import com.xiaoi.expo.management.activity.service.ActivityService;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.dao.ExhibitionBoothDao;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.entity.ExhibitionBooth;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.service.ExhibitionBoothService;
import com.xiaoi.expo.management.dictionary.entity.Dictionary;
import com.xiaoi.expo.management.enterexhibition.service.EnterpriseExhibitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Qu，De-Hui
 * @Description: 展台库管理serivce
 * @date 2018/3/20
 */
@Service
public class ExhibitionBoothServiceImpl implements ExhibitionBoothService{
    private Logger logger = LoggerFactory.getLogger(ExhibitionBoothServiceImpl.class);

    @Autowired
    private ExhibitionBoothDao exhibitionBoothDao;
    
    @Autowired
    private EnterpriseExhibitionService enterpriseExhibitionService;
    
    @Autowired
    private ActivityService activityService;

	@Override
	public PageResult<ExhibitionBooth> getExhibitionBooth(int pageNum, int pageSize, String searchName) {
		PageResult<ExhibitionBooth> pageResult = new PageResult<ExhibitionBooth>();
        Sort sort = new Sort(Sort.Direction.DESC, new String[]{"updateTime"});
        PageRequest startPage = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<ExhibitionBooth> pageExhibitionBooth = exhibitionBoothDao.findAll(new Specification<ExhibitionBooth>() {
            @Override
            public Predicate toPredicate(Root<ExhibitionBooth> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(!StringUtils.isEmpty(searchName)){
                    predicates.add(criteriaBuilder.like(root.get("code"), "%" + searchName + "%"));
                    //predicates.add(criteriaBuilder.like(root.<Dictionary>get("position").<String>get("displayName"), "%" + searchName + "%"));
                    Join<ExhibitionBooth,Dictionary> positionJoin = root.join("position",JoinType.LEFT);
                	predicates.add(criteriaBuilder.like(positionJoin.get("displayName"), "%" + searchName + "%"));
                }
                if(predicates.size()>0) {
                	criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));
                }
                return criteriaQuery.getRestriction();
            }
        }, startPage);
        pageResult.setData(pageExhibitionBooth.getContent());
        pageResult.setCount(pageExhibitionBooth.getTotalElements());
        return pageResult;
	}

	@Override
	public MapResult save(ExhibitionBooth exhibitionBooth) {
		try{
			exhibitionBoothDao.save(exhibitionBooth);
        }catch (Exception e){
            logger.error("保存展台信息出错");
            e.printStackTrace();
            return MapResult.error("保存展台信息出错");
        }
        return MapResult.ok();
	}

	@Override
	public ExhibitionBooth findOne(String exhibitionBoothId) {
		return exhibitionBoothDao.getOne(exhibitionBoothId);
	}

	@Transactional
	@Override
	public MapResult delete(String exhibitionBoothId) {
		try{
			if(enterpriseExhibitionService.isExitByExhId(exhibitionBoothId)) {
				return MapResult.error("此展台信息已被企业库引用，不能删除");
			}
			List<Activity> acts = activityService.findAllByExhibitionId(exhibitionBoothId);
			if(acts!=null && acts.size()>0) {
				return MapResult.error("此展台信息已被活动引用，不能删除");
			}
			exhibitionBoothDao.deleteById(exhibitionBoothId);
			enterpriseExhibitionService.deleteByExhId(exhibitionBoothId);
        }catch(DataIntegrityViolationException e) {
        	return MapResult.error("此展台信息已被引用，不能删除");
        }catch (Exception e){
            logger.error("删除展台信息出错");
            e.printStackTrace();
            return MapResult.error("删除展台信息出错");
        }
        return MapResult.ok();
	}

	@Override
	public MapResult update(ExhibitionBooth exhibitionBooth) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExhibitionBooth> findExhibitionBoothWithoutUsed() {
		List<ExhibitionBooth> retList = new ArrayList<ExhibitionBooth>();
		List<ExhibitionBooth> getList = exhibitionBoothDao.findAll();
		if(getList!=null && getList.size() > 0) {
			for(int i=0; i<getList.size(); i++) {
				ExhibitionBooth exh = getList.get(i);
				if(!enterpriseExhibitionService.isExitByExhId(exh.getId())) {
					retList.add(exh);
				}
			}
		}
		return retList;
	}
	
	@Override
	public List<ExhibitionBooth> findExhibitionBooth(String pavilionId) {
		
		return  exhibitionBoothDao.findAll(new Specification<ExhibitionBooth>() {
            @Override
            public Predicate toPredicate(Root<ExhibitionBooth> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                
                predicates.add(criteriaBuilder.equal(root.<Dictionary>get("position").<String>get("id"),  pavilionId));
               
                criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
                
                return criteriaQuery.getRestriction();
            }
        });
		
		
	}
	
	@Override
	public List<ExhibitionBooth> findAll() {
		
		return exhibitionBoothDao.findAll();
	}

	@Override
	public int countById(String exhibitionBoothId) {
		return exhibitionBoothDao.countById(exhibitionBoothId);
	}

	@Override
	public ExhibitionBooth findByCode(String code) {
		return exhibitionBoothDao.findByCode(code);
	}

	
	
}
