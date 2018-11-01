package com.xiaoi.expo.management.hotmessage.guesttalk.service;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.management.baseconfig.guest.entity.Guest;
import com.xiaoi.expo.management.hotmessage.guesttalk.entity.GuestTalk;

/**
 * @author bright.liang
 * @Description: 大佬说service接口
 * @date 2018/3/1616:05
 */
public interface GuestTalkService {
    /**
     * @Description: 分页查询大佬说信息
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @param searchName 查询条件value
     * @return PageResult<GuestTalk>
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    PageResult<GuestTalk> getGuestTalks(int pageNum, int pageSize, String searchName);

    /**
     * @Description: 保存大佬说信息
     * @param guestTalk 大佬说信息
     * @return MapResult
     * @author bright.liang
     * @date 2018/3/22 17:04
     */
    MapResult save(GuestTalk guestTalk);

    /**
     * @Description: 根据id查询大佬说信息
     * @param id 大佬说信息id
     * @return MapResult
     * @author bright.liang
     * @date 2018/3/23 14:04
     */
    GuestTalk findOne(String id);


    /**
     * @Description: 根据id删除大佬说信息
     * @param id 大佬说信息id
     * @return void
     * @author bright.liang
     * @date 2018/3/23 14:04
     */
    void delete(String id);
}
