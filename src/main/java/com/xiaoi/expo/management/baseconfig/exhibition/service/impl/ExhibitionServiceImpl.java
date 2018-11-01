package com.xiaoi.expo.management.baseconfig.exhibition.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.xiaoi.expo.constants.Constants;
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
import com.xiaoi.expo.management.baseconfig.exhibition.dao.ExhibitionDao;
import com.xiaoi.expo.management.baseconfig.exhibition.service.ExhibitionService;
import com.xiaoi.expo.management.baseconfig.exhibition.entity.Exhibition;

/**
 * @author chen fei
 * @Description: 会场库service实现层
 * @date 2018/3/23 09:20
 */
@Service
public class ExhibitionServiceImpl implements ExhibitionService{
    private Logger logger = LoggerFactory.getLogger(ExhibitionServiceImpl.class);

    @Autowired
    private ExhibitionDao exhibitionDao;

    @Value("${ftp.domain}")
    private String domain;

  
    @Override
    public PageResult<Exhibition> getExhibitions(int pageNum, int pageSize, final String searchName) {
        PageResult<Exhibition> pageResult = new PageResult<Exhibition>();
        Sort sort = new Sort(Sort.Direction.ASC, new String[]{"name"});
        PageRequest startPage = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<Exhibition> pageExhibition = exhibitionDao.findAll(new Specification<Exhibition>() {
            @Override
            public Predicate toPredicate(Root<Exhibition> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(!StringUtils.isEmpty(searchName)){
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchName + "%"));
                    predicates.add(criteriaBuilder.like(root.get("addr"), "%" + searchName + "%"));
                    
                }
                if(predicates.size()>0) {
                criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));
                }else {
                	criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));	
                }
                return criteriaQuery.getRestriction();
            }
        }, startPage);
        pageResult.setData(pageExhibition.getContent());
        pageResult.setCount(pageExhibition.getTotalElements());
        return pageResult;
    }

    @Override
    public MapResult save(Exhibition exhibition) {

        try{
        	exhibitionDao.save(exhibition);
        }catch (Exception e){
            logger.error("保存会场信息出错");
            e.printStackTrace();
            return MapResult.error("保存会场信息出错");
        }

        return MapResult.ok();
    }


    @Override
    public Exhibition findOne(String exhibitionId) {
        return exhibitionDao.getOne(exhibitionId);
    }
    
    @Override
    public MapResult delete(String exhibitionId) {

        try{
        	exhibitionDao.deleteById(exhibitionId);
        }catch (Exception e){
            logger.error("删除会场信息出错");
            return MapResult.error("删除会场信息出错,请先删除关联的议会厅");
        }

        return MapResult.ok();
    }
    
    @Override
    public List<Exhibition> findAll(){
    	return exhibitionDao.findAll();
    }

    @Override
    public PageResult<Map<String, Object>> findByPage(int pageNum, int pageSize) {
        PageResult<Map<String, Object>> pageResult = new PageResult<Map<String, Object>>();
        Sort sort = new Sort(Sort.Direction.ASC, new String[]{"rank"});
        PageRequest startPage = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<Exhibition> pageExhibition = exhibitionDao.findAll(startPage);
        if(pageExhibition.getContent() != null && pageExhibition.getContent().size() > 0){
            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
            for(Exhibition exhibition: pageExhibition.getContent()){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", exhibition.getId());
                map.put("name", exhibition.getName());
                map.put("image", !StringUtils.isEmpty(exhibition.getPicture()) ? domain +exhibition.getPicture(): null);
                mapList.add(map);
            }
            pageResult.setData(mapList);
        }

        int totalPage = (int)Math.ceil((float)pageExhibition.getTotalElements()/(float) pageSize);
        pageResult.setTotalPage(totalPage);
        pageResult.setCurrentPage(pageNum);
        pageResult.setCode(Constants.SUCESS_CODE);
        return pageResult;
    }
}
