package com.xiaoi.expo.management.hotmessage.guesttalk.dao;

import com.xiaoi.expo.management.hotmessage.guesttalk.entity.GuestTalk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author bright.liang
 * @Description: ${todo}
 * @date 2018/3/1615:18
 */
public interface GuestTalkDao  extends JpaRepository<GuestTalk, String>, JpaSpecificationExecutor<GuestTalk> {
}
