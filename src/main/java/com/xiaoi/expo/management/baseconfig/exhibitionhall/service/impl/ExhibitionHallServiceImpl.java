package com.xiaoi.expo.management.baseconfig.exhibitionhall.service.impl;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.management.baseconfig.exhibition.entity.Exhibition;
import com.xiaoi.expo.management.baseconfig.exhibitionhall.dao.ExhibitionHallDao;
import com.xiaoi.expo.management.baseconfig.exhibitionhall.entity.ExhibitionHall;
import com.xiaoi.expo.management.baseconfig.exhibitionhall.service.ExhibitionHallService;

/**
 * @author chen fei
 * @Description: 议会厅service实现层
 * @date 2018/3/27 09:20
 */
@Service
public class ExhibitionHallServiceImpl implements ExhibitionHallService{
    private Logger logger = LoggerFactory.getLogger(ExhibitionHallServiceImpl.class);

    @Autowired
    private ExhibitionHallDao exhibitionHallDao;

  
    @Override
    public PageResult<ExhibitionHall> getExhibitionHalls(int pageNum, int pageSize, final String searchName) {
        PageResult<ExhibitionHall> pageResult = new PageResult<ExhibitionHall>();
        Sort sort = new Sort(Sort.Direction.ASC, new String[]{"name"});
        PageRequest startPage = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<ExhibitionHall> pageExhibitionHall = exhibitionHallDao.findAll(new Specification<ExhibitionHall>() {
            @Override
            public Predicate toPredicate(Root<ExhibitionHall> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(!StringUtils.isEmpty(searchName)){
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchName + "%"));
                    predicates.add(criteriaBuilder.like(root.join("exhibition").<String>get("name"), "%" + searchName + "%"));
                    
                }
                if(predicates.size()>0) {
                criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));
                }else {
                	criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));	
                }
                return criteriaQuery.getRestriction();
            }
        }, startPage);
        pageResult.setData(pageExhibitionHall.getContent());
        pageResult.setCount(pageExhibitionHall.getTotalElements());
        return pageResult;
    }

    @Override
    public MapResult save(ExhibitionHall exhibitionHall) {

        try{
        	exhibitionHallDao.save(exhibitionHall);
        }catch (Exception e){
            logger.error("保存议会厅信息出错");
            e.printStackTrace();
            return MapResult.error("保存议会厅信息出错");
        }

        return MapResult.ok();
    }


    @Override
    public ExhibitionHall findOne(String exhibitionHallId) {
        return exhibitionHallDao.getOne(exhibitionHallId);
    }
    
    @Override
    public MapResult delete(String exhibitionHallId) {

        try{
        	exhibitionHallDao.deleteById(exhibitionHallId);
        }catch (Exception e){
            logger.error("删除议会厅信息出错");
            e.printStackTrace();
            return MapResult.error("删除议会厅信息出错,请现删除关联信息表");
        }

        return MapResult.ok();
    }
    
    @Override
    public List<ExhibitionHall> findAll(){
    	return exhibitionHallDao.findAll();
    }
    
    @Override
	public List<ExhibitionHall> findExhibitionHall(String exhibitionId) {
		
		return  exhibitionHallDao.findAll(new Specification<ExhibitionHall>() {
            @Override
            public Predicate toPredicate(Root<ExhibitionHall> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                
                predicates.add(criteriaBuilder.like(root.<Exhibition>get("exhibition").<String>get("id"), "%" + exhibitionId + "%"));
               
                criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
                
                return criteriaQuery.getRestriction();
            }
        });
		
		
	}
}
