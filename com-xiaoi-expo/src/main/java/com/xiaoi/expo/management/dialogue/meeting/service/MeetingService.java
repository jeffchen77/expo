package com.xiaoi.expo.management.dialogue.meeting.service;

import java.util.List;
import java.util.Map;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.management.dialogue.meeting.entity.Meeting;
import com.xiaoi.expo.management.dialogue.meeting.entity.MeetingGuest;

/**
 * @author chen fei
 * @Description: 会议service层
 * @date 2018/3/28 09:20
 */
public interface MeetingService {
    
    /**
     * @Description: 分页查询会议信息
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @param searchKey 查询条件key
     * @param searchValue 查询条件value
     * @return PageResult<Meeting>
     * @author chen fei
     * @date 2018/3/28 09:24
     */
    PageResult<Meeting> getMeetings(int pageNum, int pageSize, String searchName);

    /**
     * @Description: 保存会议信息
     * @param meeting 会议信息
     * @return MapResult
     * @author chen fei
     * @date 2018/3/28 09:24
     */
    MapResult save(Meeting meeting);

     /**
     * @Description: 根据会议id查询会议信息
     * @param meetingId 会议id
     * @return Meeting
     * @author chen fei
     * @date 2018/3/28 09:24
     */
    Meeting findOne(String meetingId);
    
    /**
     * @Description: 删除会议信息
     * @param meetingId 会议id
     * @return MapResult
     * @author chen fei
     * @date 2018/3/28 09:24
     */
    MapResult delete(String meetingId);
    
    /**
     * @Description: 所有会议信息
     * @return List<Meeting>
     * @author chen fei
     * @date 2018/3/28 09:24
     */
    List<Meeting> findAll();
    /**
     * @Description: 分页查询会议信息
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @param searchKey 查询条件key
     * @param meetingId 会议Id
     * @param searchValue 查询条件value
     * @return PageResult<Guest>
     * @author chen fei
     * @date 2018/3/28 09:24
     */
    PageResult<MeetingGuest> getMeetingGuest(int pageNum, int pageSize,String meetingId, String searchName);
    
    /**
     * @Description: 分页查询会议信息(大屏接口)
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @return PageResult<Map<String, Object>>
     * @author bright.liang
     * @date 2018/3/20
     */
    PageResult<Map<String, Object>> findByPage(int pageNum, int pageSize, String date) throws Exception;

    /**
     * @Description: 查询会场会议日期(大屏接口)
     * @param hallId 分会场id
     * @return PageResult<Map<String, Object>>
     * @author bright.liang
     * @date 2018/3/20
     */
    PageResult<Map<String, Object>> findMeetingDates( String hallId) throws Exception;

    /**
     * @Description: 分页查询会场会议议程(大屏接口)
     * @param hallId 分会场id
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @param date 日期
     * @return PageResult<Map<String, Object>>
     * @author bright.liang
     * @date 2018/3/20
     */
    PageResult<Map<String, Object>> findByPageHall(int pageNum, int pageSize, String date, String hallId) throws Exception;
    
    /**
     * @Description: 查询会议日期(大屏接口)
     * @return PageResult<Map<String, Object>>
     * @author Qu, De-Hui
     * @date 2018/3/20
     */
    PageResult<Map<String, Object>> getAllMeetingDates() throws Exception;
    
    /**
     * @Description: 查询会议详情(大屏接口)
     * @return PageResult<Map<String, Object>>
     * @author Qu, De-Hui
     * @date 2018/3/20
     */
    MapResult meetingDetail(String meetingId) throws Exception;


    /**
     * @Description: 分页查询会场会议议程(大屏接口)
     * @param hallId 分会场id
     * @param date 日期
     * @return PageResult<Map<String, Object>>
     * @author bright.liang
     * @date 2018/3/20
     */
    PageResult<Map<String, Object>> hallMeetings(String date, String hallId) throws Exception;
    /**
     * @Description: 分页查询会场会议议程(大屏接口)
     * @param types 会议类型
     * @param floor 楼层
     * @return List<Map<String, Object>>
     * @author bright.liang
     * @date 2018/4/15
     */
    List<Map<String, Object>> findByType(String types, int floor);

    /**
     * @Description: 根据会议厅名称查询最近会议
     * @param hallName 会议厅名称
     * @return Map<String, Object>
     * @author bright.liang
     * @date 2018/4/15
     */
    Map<String, Object> findByHallName(String hallName);


}
