package com.xiaoi.expo.management.dictionary.dao;

import com.xiaoi.expo.management.dictionary.entity.Dictionary;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Qu, De-Hui
 * @Description: ${todo}
 * @date 2018/3/21
 */
public interface DictionaryDao  extends JpaRepository<Dictionary, String>, JpaSpecificationExecutor<Dictionary> {
	
	Dictionary findByValue(String value);
	
	List<Dictionary> findAllByParentIdOrderByPriorityAsc(String parentId);
	
	Set<Dictionary> findAllByIdIn(List<String> values);
}
