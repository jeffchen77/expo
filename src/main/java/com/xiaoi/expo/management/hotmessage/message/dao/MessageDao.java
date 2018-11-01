package com.xiaoi.expo.management.hotmessage.message.dao;

import com.xiaoi.expo.management.hotmessage.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author guangjian.zeng
 * @Description: ${todo}
 * @date 2018/3/1911:24
 */

public interface MessageDao extends JpaRepository<Message, String>, JpaSpecificationExecutor<Message> {
}
