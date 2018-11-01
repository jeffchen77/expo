package com.xiaoi.expo.management.user.service;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.management.user.entity.User;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * @author bright.liang
 * @Description: 用户管理service层
 * @date 2018/3/1217:00
 */
public interface UserService {
    /**
     * @Description: 用户登录
     * @param userName 登录用户名
     * @param password 登录用户密码
     * @return ${return_type}
     * @throws
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    User userLogin(String userName, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException;


    /**
     * @Description: 分页查询用户信息
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @param searchKey 查询条件key
     * @param searchValue 查询条件value
     * @return PageResult<User>
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    PageResult<User> getUsers(int pageNum, int pageSize, String searchName);

    /**
     * @Description: 保存用户信息
     * @param user 用户信息
     * @return MapResult
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    MapResult save(User user);

    /**
     * @Description: 根据用户名查询用户信息
     * @param userName 用户名
     * @return MapResult
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    User findByUserName(String userName);

    /**
     * @Description: 根据用户id查询用户信息
     * @param userId 用户id
     * @return User
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    User findOne(String userId);
}
