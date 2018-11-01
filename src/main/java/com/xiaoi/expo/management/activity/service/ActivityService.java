package com.xiaoi.expo.management.activity.service;

import java.util.Date;
import java.util.List;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.management.activity.entity.Activity;

/**
 * @author Qu, De-Hui
 * @Description: 活动管理service层
 * @date 2018/3/1217:00
 */
public interface ActivityService {
     /**
     * @Description: 分页查询用户信息
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @param searchKey 查询条件key
     * @param searchValue 查询条件value
     * @return PageResult<Activity>
     * @author Qu,De-Hui
     * @date 2018/3/13 18:04
     */
    PageResult<Activity> getActivity(int pageNum, int pageSize, String searchName);

    /**
     * @Description: 保存活动信息
     * @param activity 活动信息
     * @return MapResult
     * @author Qu, De-Hui
     * @date 2018/3/13 18:04
     */
    MapResult save(Activity activity);

    /**
     * @Description: 根据活动id查询用户信息
     * @param activityId 活动id
     * @return Activity
     * @author Qu, De-Hui
     * @date 2018/3/13 18:04
     */
    Activity findOne(String activityId);
    
    /**
     * @Description: 根据活动id查询用户信息
     * @param activityId 活动id
     * @return Activity
     * @author Qu, De-Hui
     * @date 2018/3/13 18:04
     */
    MapResult delete(String activityId);
    
    /**
     * @Description: 根据展台信息去查询活动
     * @param activityId 活动id
     * @return Activity
     * @author Qu, De-Hui
     * @date 2018/3/13 18:04
     */
    List<Activity> findAllByExhibitionId(String exhId);
    
    /**
     * @Description: 根据展馆信息去查询活动
     * @param activityId 活动id
     * @return Activity
     * @author Qu, De-Hui
     * @date 2018/3/13 18:04
     */
    List<Activity> findAllByPavilionIdAndDate(String pavId, String curDate);
    
    /**
     * @Description: 根据展馆信息和企业ID去查询活动
     * @param activityId 活动id
     * @return Activity
     * @author Qu, De-Hui
     * @date 2018/3/13 18:04
     */
    List<Activity> findAllByPavilionIdAndEntIdAndDate(String pavId, String EntId, Date curDate);
}
