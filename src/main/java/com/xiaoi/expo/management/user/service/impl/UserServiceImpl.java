package com.xiaoi.expo.management.user.service.impl;
import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.common.utils.EncryptptUtils;
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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author bright.liang
 * @Description: ${todo}
 * @date 2018/3/1217:00
 */
@Service
public class UserServiceImpl implements UserService{
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    public User userLogin(final String userName, final String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        final String encryptPwd = EncryptptUtils.md5Encrypt(password);


        List<User> users = userDao.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                predicates.add(criteriaBuilder.equal(root.get("userName"), userName));
                predicates.add(criteriaBuilder.equal(root.get("password"), encryptPwd));
                criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
                return criteriaQuery.getRestriction();
            }
        });

        if(users == null || users.size() == 0){
            return null;
        }
        return users.get(0);
    }

    @Override
    public PageResult<User> getUsers(int pageNum, int pageSize, final String searchName) {
        PageResult<User> pageResult = new PageResult<User>();
        Sort sort = new Sort(Sort.Direction.ASC, new String[]{"userName"});
        PageRequest startPage = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<User> pageUser = userDao.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(!StringUtils.isEmpty(searchName)){
                    predicates.add(criteriaBuilder.like(root.get("realUserName"), "%" + searchName + "%"));
                }
                criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
                return criteriaQuery.getRestriction();
            }
        }, startPage);
        pageResult.setData(pageUser.getContent());
        pageResult.setCount(pageUser.getTotalElements());
        return pageResult;
    }

    @Override
    public MapResult save(User user) {

        try{
            userDao.save(user);
        }catch (Exception e){
            logger.error("保存用户信息出错");
            e.printStackTrace();
            return MapResult.error("保存用户信息出错");
        }

        return MapResult.ok();
    }

    @Override
    public User findByUserName(final String userName) {
        List<User> users = userDao.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                predicates.add(criteriaBuilder.equal(root.get("userName"), userName));
                criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
                return criteriaQuery.getRestriction();
            }
        });

        if(users == null || users.size() == 0){
            return null;
        }
        return users.get(0);
    }

    @Override
    public User findOne(String userId) {
        return userDao.getOne(userId);
    }
}
