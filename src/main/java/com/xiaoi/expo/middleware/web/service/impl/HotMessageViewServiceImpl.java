package com.xiaoi.expo.middleware.web.service.impl;

import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.constants.Constants;
import com.xiaoi.expo.middleware.web.dao.HotMessageViewDao;
import com.xiaoi.expo.middleware.web.entity.HotMessageView;
import com.xiaoi.expo.middleware.web.service.HotMessageViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bright.liang
 * @Description: 热点资讯视图service
 * @date 2018/3/2714:33
 */
@Service
public class HotMessageViewServiceImpl implements HotMessageViewService{
    @Autowired
    private HotMessageViewDao hotMessageViewDao;


    @Override
    public List<HotMessageView> findHotNews(Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.ASC, new String[]{"rank"});
        PageRequest startPage = PageRequest.of(0, pageSize, sort);
        Page<HotMessageView> pageHotmessages = hotMessageViewDao.findAll(startPage);
        if(pageHotmessages != null){
            return pageHotmessages.getContent();
        }
        return null;
    }

    @Override
    public List<HotMessageView> findHotMessages(Integer pageNumber, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, new String[]{"cerateDate"});
        PageRequest startPage = PageRequest.of(pageNumber, pageSize, sort);
        Page<HotMessageView> pageHotmessages = hotMessageViewDao.findAll(new Specification<HotMessageView>() {
            public Predicate toPredicate(Root<HotMessageView> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                predicates.add(criteriaBuilder.equal(root.get("type"), "0"));
                predicates.add(criteriaBuilder.isNotNull(root.get("knowledge")));
                predicates.add(criteriaBuilder.notEqual(root.get("knowledge"), ""));
                criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
                return criteriaQuery.getRestriction();
            }
        }, startPage);
        if(pageHotmessages != null){
            return pageHotmessages.getContent();
        }
        return null;
    }

    @Override
    public List<HotMessageView> findHotMessagesByImg(Integer pageNumber, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, new String[]{"cerateDate"});
        PageRequest startPage = PageRequest.of(pageNumber, pageSize, sort);
        Page<HotMessageView> pageHotmessages = hotMessageViewDao.findAll(new Specification<HotMessageView>() {
            public Predicate toPredicate(Root<HotMessageView> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                predicates.add(criteriaBuilder.equal(root.get("type"), "0"));
                predicates.add(criteriaBuilder.isNotNull(root.get("image")));
                criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
                return criteriaQuery.getRestriction();
            }
        }, startPage);
        if(pageHotmessages != null){
            return pageHotmessages.getContent();
        }
        return null;
    }

    @Override
    public PageResult findHotMessageslist(Integer pageNumber, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, new String[]{"cerateDate"});
        PageRequest startPage = PageRequest.of(pageNumber, pageSize, sort);
        Page<HotMessageView> pageHotmessages = hotMessageViewDao.findAll(new Specification<HotMessageView>() {
            public Predicate toPredicate(Root<HotMessageView> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                predicates.add(criteriaBuilder.equal(root.get("type"), "0"));
                predicates.add(criteriaBuilder.isNotNull(root.get("knowledge")));
                criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
                return criteriaQuery.getRestriction();
            }
        }, startPage);
        PageResult pageResult = new PageResult();
        if(pageHotmessages != null){
            pageResult.setCode(Constants.SUCESS_CODE);
            pageResult.setData(pageHotmessages.getContent());
            int totalPage = (int)Math.ceil((float)pageHotmessages.getTotalElements()/(float) pageSize);
            pageResult.setTotalPage(totalPage);
        }else{
            pageResult.setCode(Constants.ERROR_CODE_SYSTEM);
            pageResult.setMsg("查询热点资讯出错");
        }
        return pageResult;
    }
}
