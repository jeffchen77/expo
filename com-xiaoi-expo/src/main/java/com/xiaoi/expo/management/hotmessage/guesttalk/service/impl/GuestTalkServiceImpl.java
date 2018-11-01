package com.xiaoi.expo.management.hotmessage.guesttalk.service.impl;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.management.hotmessage.guesttalk.dao.GuestTalkDao;
import com.xiaoi.expo.management.hotmessage.guesttalk.entity.GuestTalk;
import com.xiaoi.expo.management.hotmessage.guesttalk.service.GuestTalkService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author bright.liang
 * @Description:
 * @date 2018/3/1616:06
 */
@Service
public class GuestTalkServiceImpl implements GuestTalkService{

    @Autowired
    private GuestTalkDao guestTalkDao;

    @Override
    public PageResult<GuestTalk> getGuestTalks(int pageNum, int pageSize, String searchName) {
        PageResult<GuestTalk> pageResult = new PageResult<GuestTalk>();
        Sort sort = new Sort(Sort.Direction.DESC, new String[]{"createTime"});
        PageRequest startPage = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<GuestTalk> pageGuestTalk = guestTalkDao.findAll(new Specification<GuestTalk>() {
            @Override
            public Predicate toPredicate(Root<GuestTalk> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(!StringUtils.isEmpty(searchName)){
                    predicates.add(criteriaBuilder.like(root.get("title"), "%" + searchName + "%"));
                }
                criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
                return criteriaQuery.getRestriction();
            }
        }, startPage);
        pageResult.setData(pageGuestTalk.getContent());
        pageResult.setCount(pageGuestTalk.getTotalElements());
        return pageResult;
    }

    @Override
    public MapResult save(GuestTalk guestTalk) {
        GuestTalk oldGuestTalk = null;
        if(!StringUtils.isEmpty(guestTalk.getId())){
            oldGuestTalk = guestTalkDao.getOne(guestTalk.getId());
            if(oldGuestTalk != null){
                oldGuestTalk.setContent(guestTalk.getContent());
                oldGuestTalk.setTitle(guestTalk.getTitle());
                oldGuestTalk.setGuest(guestTalk.getGuest());
                oldGuestTalk.setKnowledge(guestTalk.getKnowledge());
                oldGuestTalk.setPicture(guestTalk.getPicture());
                oldGuestTalk.setUpdateTime(new Date());
            }else {
                return MapResult.error("修改失败！信息不存在或已被删除");
            }
        }else{
            oldGuestTalk = guestTalk;
            oldGuestTalk.setCreateTime(new Date());
        }
        guestTalkDao.save(oldGuestTalk);
        return MapResult.ok();
    }

    @Override
    public GuestTalk findOne(String id) {
        return guestTalkDao.getOne(id);
    }

    @Override
    public void delete(String id) {
        guestTalkDao.deleteById(id);
    }
}
