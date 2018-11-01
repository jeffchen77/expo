package com.xiaoi.expo.management.hotmessage.message.service.impl;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.management.hotmessage.message.dao.MessageDao;
import com.xiaoi.expo.management.hotmessage.message.entity.Message;
import com.xiaoi.expo.management.hotmessage.message.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MessageServiceImpl implements MessageService{

    private Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    MessageDao messageDao;

    @Override
    public PageResult<Message> getGuestTalks(int pageNum, int pageSize, String searchName) {
        PageResult<Message> pageResult = new PageResult<Message>();
        Sort sort = new Sort(Sort.Direction.DESC, new String[]{"createTime"});
        PageRequest startPage = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<Message> pageUser = messageDao.findAll(new Specification<Message>() {
            @Override
            public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(!StringUtils.isEmpty(searchName)){
                    predicates.add(criteriaBuilder.like(root.get("title"), "%" + searchName + "%"));
                }

                predicates.add(criteriaBuilder.equal(root.get("status"),"0"));

                criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
                return criteriaQuery.getRestriction();
            }
        }, startPage);
        pageResult.setData(pageUser.getContent());
        pageResult.setCount(pageUser.getTotalElements());
        return pageResult;
    }

    @Override
    public MapResult save(Message message) {
        try{
            messageDao.save(message);
        }catch (Exception e){
            logger.error("保存图文短讯出错");
            e.printStackTrace();
            return MapResult.error("保存图文短讯出错");
        }
        return MapResult.ok();
    }

    @Override
    public Message findOne(String messageId) {
        return messageDao.getOne(messageId);
    }
}
