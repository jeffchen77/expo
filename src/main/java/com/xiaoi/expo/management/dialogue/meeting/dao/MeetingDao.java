package com.xiaoi.expo.management.dialogue.meeting.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.xiaoi.expo.management.dialogue.meeting.entity.Meeting;
import org.springframework.data.jpa.repository.Query;

/**
 * @author chen fei
 * @Description: 会议dao层
 * @date 2018/3/28 09:20
 */
public interface MeetingDao extends JpaRepository<Meeting, String>, JpaSpecificationExecutor<Meeting> {
	Page<Meeting> findByMeetingStartTimeBetween(Date d1, Date d2, Pageable page);

	@Query(value = "SELECT DISTINCT DATE_FORMAT(meeting_start_time, '%Y-%m-%d') from expo_meeting where exhibition_id= ?1 ORDER BY meeting_start_time", nativeQuery = true)
	List<Object> findMeetingDates(String hallId);
	
	@Query(value = "SELECT DISTINCT DATE_FORMAT(meeting_start_time, '%Y-%m-%d') from expo_meeting ORDER BY meeting_start_time", nativeQuery = true)
	List<Object> getAllMeetingDates();

	@Query(value = "SELECT m.id as id,  m.theme as theme, m.meeting_start_time as start_time, m.meeting_end_time as end_time, h.flow_index as floor, h.pos_x as x, h.pos_y as y , m.hall_id as hall_id, h.picture as picture, h.name as hallName FROM (SELECT * FROM expo_meeting where meeting_end_time > NOW() and meeting_type in(?1) ORDER BY meeting_end_time asc ) m LEFT JOIN expo_exhibition_hall h on h.id = m.hall_id where h.flow_index = ?2 GROUP BY m.hall_id", nativeQuery = true)
    List<Map<String, Object>> getLastMeetings(List<String> type, int flowIndex);

	@Query(value = "SELECT m.id as id,  m.theme as theme, m.meeting_start_time as start_time, m.meeting_end_time as end_time, h.flow_index as floor, h.pos_x as x, h.pos_y as y , m.hall_id as hall_id, h.picture as picture, h.name as hallName FROM (SELECT * FROM expo_meeting where meeting_end_time > NOW()  ORDER BY meeting_end_time asc ) m LEFT JOIN expo_exhibition_hall h on h.id = m.hall_id where h.name = ?1 OR m.theme = ?1 GROUP BY m.hall_id", nativeQuery = true)
	List<Map<String, Object>> getLastMeetingsByHallName(String hallName);
}
