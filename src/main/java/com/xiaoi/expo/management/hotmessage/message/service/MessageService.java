package com.xiaoi.expo.management.hotmessage.message.service;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.management.hotmessage.message.entity.Message;

/**
 * @author guangjian.zeng
 * @Description: 图文信息service接口
 * @date 2018/3/1916:05
 */
public interface MessageService {

    /**
     * @Description: 分页查询大佬说信息
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @param searchName 查询条件value
     * @return PageResult<Message>
     * @author guangjian.zeng
     * @date 2018/3/19 11:30
     */
    PageResult<Message> getGuestTalks(int pageNum, int pageSize, String searchName);

    /**
     * @Description: 保存用户信息
     * @param message 用户信息
     * @return MapResult
     * @author guangjian.zeng
     * @date 2018/3/20 14:04
     */
    MapResult save(Message message);

    /**
     * @Description: 根据用户id查询图文信息
     * @param messageId 图文短讯id
     * @return Message
     * @author guangjian.zeng
     * @date 2018/3/20 14:04
     */
    Message findOne(String messageId);
}
