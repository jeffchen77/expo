package com.xiaoi.expo.middleware.web.service;

import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.middleware.web.entity.HotMessageView;

import java.util.List;

/**
 * @author bright.liang
 * @Description: ${todo}
 * @date 2018/3/2714:33
 */
public interface HotMessageViewService {

    /**
     * @Description: 分页查询热点资讯/大佬说信息
     * @param pageSize 每页显示条数
     * @return List<HotMessageView>
     * @author bright.liang
     * @date 2018/3/28 18:04
     */
    List<HotMessageView> findHotNews(Integer pageSize);

    /**
     * @Description: 分页查询热点资讯（知识链接不为空）
     * @param pageSize 每页显示条数
     * @return List<HotMessageView>
     * @author bright.liang
     * @date 2018/4/02 18:04
     */
    List<HotMessageView> findHotMessages(Integer pageNumber, Integer pageSize);

    /**
     * @Description: 分页查询热点资讯（图片不为空）
     * @param pageSize 每页显示条数
     * @return List<HotMessageView>
     * @author bright.liang
     * @date 2018/4/02 18:04
     */
    List<HotMessageView> findHotMessagesByImg(Integer pageNumber, Integer pageSize);

    PageResult findHotMessageslist(Integer pageNumber, Integer pageSize);
}
