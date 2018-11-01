package com.xiaoi.expo.middleware.web.dao;

import com.xiaoi.expo.middleware.web.entity.HotMessageView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author bright.liang
 * @Description: 热点资讯dao
 * @date 2018/3/2714:30
 */
public interface HotMessageViewDao  extends JpaRepository<HotMessageView, String>, JpaSpecificationExecutor<HotMessageView> {
}
