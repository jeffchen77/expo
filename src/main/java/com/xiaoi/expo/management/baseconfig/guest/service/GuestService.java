package com.xiaoi.expo.management.baseconfig.guest.service;

import java.util.List;
import java.util.Map;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.management.baseconfig.guest.entity.Guest;

/**
 * @author chen fei
 * @Description: 嘉宾库service层
 * @date 2018/3/20 09:20
 */
public interface GuestService {
    
    /**
     * @Description: 分页查询嘉宾信息
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @param searchKey 查询条件key
     * @param searchValue 查询条件value
     * @return PageResult<Guest>
     * @author chen fei
     * @date 2018/3/20 09:24
     */
    PageResult<Guest> getGuests(int pageNum, int pageSize, String searchName);
    
    /**
     * @Description: 分页查询嘉宾认证信息
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @param searchKey 查询条件key
     * @param searchValue 查询条件value
     * @return PageResult<Guest>
     * @author chen fei
     * @date 2018/3/20 09:24
     */
    PageResult<Guest> authFindByPagging(int pageNum, int pageSize, String searchName);

    /**
     * @Description: 保存嘉宾信息
     * @param guest 嘉宾信息
     * @return MapResult
     * @author chen fei
     * @date 2018/3/20 09:24
     */
    MapResult save(Guest guest);

     /**
     * @Description: 根据嘉宾id查询嘉宾信息
     * @param guestId 嘉宾id
     * @return Guest
     * @author chen fei
     * @date 2018/3/20 09:24
     */
    Guest findOne(String guestId);
    
    /**
     * @Description: 删除嘉宾信息
     * @param guestId 嘉宾id
     * @return MapResult
     * @author chen fei
     * @date 2018/3/20 09:24
     */
    MapResult delete(String guestId);
    
    /**
     * @Description: 所有嘉宾信息
     * @return List<Guest>
     * @author chen fei
     * @date 2018/3/20 09:24
     */
    List<Guest> findAll();
    
    /**
     * @Description: 分页查询嘉宾信息(大屏接口)
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @return PageResult<Map<String, Object>>
     * @author bright.liang
     * @date 2018/3/20
     */
    PageResult<Map<String, Object>> findByPage(int pageNum, int pageSize);
    
    /**
     * @Description: 嘉宾认证大屏接口
     * @param guestId 嘉宾id
     * @return MapResult
     * @author chen fei
     * @date 2018/3/20 09:24
     */
    MapResult auth(String authCode) throws Exception;
}
