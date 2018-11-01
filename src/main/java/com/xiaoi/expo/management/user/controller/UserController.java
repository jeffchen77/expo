package com.xiaoi.expo.management.user.controller;

import com.xiaoi.expo.common.interceptor.Auth;
import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.common.utils.EncryptptUtils;
import com.xiaoi.expo.constants.Constants;
import com.xiaoi.expo.management.module.entity.Module;
import com.xiaoi.expo.management.module.service.ModuleService;
import com.xiaoi.expo.management.user.entity.User;
import com.xiaoi.expo.management.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author bright.liang
 * @Description: ${todo}
 * @date 2018/3/1217:15
 */
@Controller
@RequestMapping(value = "/management/users")
public class UserController {

    private Logger logger =  LoggerFactory.getLogger(UserController.class);

    private static  final String INDEX = "main";

    private static final String USER_LIST = "users/list";

    private static final String LOGIN = "login";

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;

    /**
     * @Description: 用户登录
     * @param userName 用户名
     * @param password 密码
     * @return MapResult
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public MapResult userLogin(String userName, String password, HttpServletRequest request){
        MapResult mapResult = null;
        if(StringUtils.isEmpty(userName)){
            mapResult = MapResult.error(Constants.ERROR_CODE_VALIDATION, "用户名不能为空");
            return mapResult;
        }

        if(StringUtils.isEmpty(password)){
            mapResult = MapResult.error(Constants.ERROR_CODE_VALIDATION, "密码不能为空");
            return mapResult;
        }

        User user = null;
        try {
            user = userService.userLogin(userName, password);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("登录异常， 用户名[" + userName + "], 密码[" +password + "]");
            mapResult = MapResult.error(Constants.ERROR_CODE_VALIDATION, "系统异常，请稍后再试");
            return mapResult;
        }
        if(user == null){
            mapResult = MapResult.error(Constants.ERROR_CODE_VALIDATION, "用户名或密码错误");
            return mapResult;
        }

        if(Constants.USER_STATUS_STOP.equals(user.getStatus())){
            mapResult = MapResult.error(Constants.ERROR_CODE_VALIDATION, "该用户已被锁定，请联系管理员...");
            return mapResult;
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        String indexPage = "/management/users/list";
        String indexName = "用户列表";
        if(Constants.USER_ROLE_NORMAL.equals(user.getRole())){
            indexPage = "/management/activity/list";
            indexName = "活动列表";
        }else if(Constants.USER_ROLE_VISTER.equals(user.getRole())){
            indexPage = "/management/config/guest/auth";
            indexName = "统计列表";
        }
        session.setAttribute("indexPage", indexPage);
        session.setAttribute("indexName", indexName);
        return MapResult.ok(Constants.SUCESS_CODE, "登录成功");
    }

    /**
     * @Description: 跳转到登录页面
     * @return String
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    @RequestMapping(value = "tologin", method = RequestMethod.GET)
    public String toLogin(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session != null && session.getAttribute("user") != null){
            return INDEX;
        }
        return LOGIN;
    }

    /**
     * @Description: 跳转到首页
     * @return String
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    @RequestMapping(value = "main", method = RequestMethod.GET)
    @Auth
    public String index(HttpServletRequest request){
        return INDEX;
    }

    /**
     * @Description: 获取菜单
     * @return MapResult
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    @RequestMapping(value = "menu", method = RequestMethod.GET)
    @ResponseBody
    public MapResult getMenu(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<Module> moduleList = moduleService.getModules(user.getRole());
        MapResult mapResult = MapResult.ok();
        mapResult.put("menu", moduleList);
        return mapResult;
    }

    /**
     * @Description: 加载用户列表页面
     * @return String
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String userList(){
        return USER_LIST;
    }

    /**
     * @Description: 分页查询用户信息
     * @param page 页码
     * @param  limit 每页显示条数
     * @param searchName 搜索关键词
     * @return PageResult<User>
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    @RequestMapping(value = "findByPagging", method = RequestMethod.GET)
    @ResponseBody
    public PageResult<User> findByPagging(Integer page, Integer limit, String searchName){
        if(page == null) {
            page = 0;
            limit = 10;
        }
        return userService.getUsers(page, limit, searchName);
    }

    /**
     * @Description: 保存用户信息
     * @param user 用户信息
     * @return MapResult
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public MapResult save(User user){
        User oldUser = null;
        if(!StringUtils.isEmpty(user.getId())){
            oldUser = userService.findOne(user.getId());
            if(oldUser == null){
                return MapResult.error("用户不存在或已被删除");
            }
            oldUser.setStatus(user.getStatus());
            oldUser.setUserName(user.getUserName());
            oldUser.setRealUserName(user.getRealUserName());
            oldUser.setRole(user.getRole());
            oldUser.setSex(user.getSex());
            oldUser.setUpdateDate(new Date());
            oldUser.setUserPhone(user.getUserPhone());
        }else{
            oldUser = user;
            try {
                oldUser.setPassword(EncryptptUtils.md5Encrypt("123456"));
            } catch (Exception e) {
                logger.error("新增用户初始化密码出错");
                e.printStackTrace();
                return MapResult.error("新增用户初始化密码出错");
            }
            oldUser.setCreateTime(new Date());
        }
        return userService.save(oldUser);
    }

    /**
     * @Description: 检查用户名是否已存在
     * @param userName 用户名
     * @return MapResult
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    @RequestMapping(value = "checkUserName", method = RequestMethod.GET)
    @ResponseBody
    public MapResult checkUserName(String userName, String id){
        User user = userService.findByUserName(userName);
        MapResult mapResult = MapResult.ok();

        if(StringUtils.isEmpty(id)){
            if(user == null){
                mapResult.put("flag", true);
            }else{
                mapResult.put("flag", false);
            }
        }else{
            if(user.getId().equals(id)){
                mapResult.put("flag", true);
            }else{
                mapResult.put("flag", false);
            }
        }
        return mapResult;
    }

    /**
     * @Description: 更新用户状态
     * @param userId 用户id
     * @param status 状态
     * @return MapResult
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    @RequestMapping(value = "updateStatus", method = RequestMethod.POST)
    @ResponseBody
    public MapResult updateStatus(String userId, String status){
        User user = userService.findOne(userId);
        if(user == null){
            return MapResult.error("用户不存在或已被删除");
        }
        user.setStatus(status);
        user.setUpdateDate(new Date());
        userService.save(user);
        return MapResult.ok();
    }

    /**
     * @Description: 重置用户密码
     * @param id 用户id
     * @return MapResult
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    @RequestMapping(value = "resetPwd", method = RequestMethod.POST)
    @ResponseBody
    public MapResult resetPwd(String id){
        User user = userService.findOne(id);
        if(user == null){
            return MapResult.error("用户不存在或已被删除");
        }

        try {
            user.setPassword(EncryptptUtils.md5Encrypt("123456"));
        } catch (Exception e) {
            logger.error("重置密码失败");
            e.printStackTrace();
            return MapResult.error("重置密码失败");

        }

        user.setUpdateDate(new Date());
        userService.save(user);
        return MapResult.ok("重置密码成功");
    }

    /**
     * @Description: 修改密码
     * @param oldPsw 原密码
     * @param newPsw 新密码
     * @param newPsw2 新密码确认
     * @return MapResult
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    @RequestMapping(value = "changePwd", method = RequestMethod.POST)
    @ResponseBody
    public MapResult changePwd(String oldPsw, String newPsw, String newPsw2, HttpServletRequest request){
        if(!newPsw.equals(newPsw2)){
            return MapResult.error("两次输入密码不一致");
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String encryptPwd = null;
        String encryptNew = null;
        try {
            encryptPwd = EncryptptUtils.md5Encrypt(oldPsw);
            encryptNew = EncryptptUtils.md5Encrypt(newPsw2);
        } catch (Exception e) {
            logger.error("修改密码失败");
            e.printStackTrace();
            return MapResult.error("修改密码失败");
        }

        if(!StringUtils.isEmpty(encryptPwd) && encryptPwd.equals(user.getPassword())){
            user.setPassword(encryptNew);
            userService.save(user);
        }else{
            return MapResult.error("原密码错误");
        }
        return MapResult.ok("修改密码成功");
    }

    /**
     * @Description: 退出登录
     * @return MapResult
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public MapResult logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return MapResult.ok();
    }
}
