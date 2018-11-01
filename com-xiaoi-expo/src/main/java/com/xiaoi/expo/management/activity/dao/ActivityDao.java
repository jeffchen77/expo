package com.xiaoi.expo.management.activity.dao;

import com.xiaoi.expo.management.activity.entity.Activity;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Qu, De-Hui
 * @Description: 活动管理dao层
 * @date 2018/3/1216:58
 */
public interface ActivityDao extends JpaRepository<Activity, String>, JpaSpecificationExecutor<Activity> {
	/**
     * @Description: 根据展台id查询活动信息
     * @param activityId 活动id
     * @return Activity
     * @author Qu, De-Hui
     * @date 2018/3/13 18:04
     */
	List<Activity> findAllByBooth_Id(String exhId);
	@Query(value = "select act1.* from expo_activity act1 where act1.pavilion_id=?1 and act1.enterprise_id is not null and act1.activity_end_time > ?2 and exists (select 1 from expo_activity act2 where act2.pavilion_id=act1.pavilion_id and act2.enterprise_id=act1.enterprise_id and act2.activity_end_time > ?2 having min(act2.activity_end_time)=act1.activity_end_time);", nativeQuery = true)
	List<Activity> findAllActivityWithEnterprise(String pavId, String date);
	List<Activity> findAllByPavilionDic_IdAndEnterprise_IdAndActivityEndTimeGreaterThanOrderByActivityEndTime(String pavId, String entId, Date date);
}
