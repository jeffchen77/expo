package com.xiaoi.expo.management.dialogue.meeting.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.xiaoi.expo.management.dialogue.meeting.entity.MeetingGuest;

/**
 * @author chen fei
 * @Description: 会议dao层
 * @date 2018/3/28 09:20
 */
public interface MeetingGuestDao extends JpaRepository<MeetingGuest, String>, JpaSpecificationExecutor<MeetingGuest> {
	
}
