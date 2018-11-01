package com.xiaoi.expo.management.dictionary.service.impl;

import com.xiaoi.expo.management.dictionary.dao.DictionaryDao;
import com.xiaoi.expo.management.dictionary.entity.Dictionary;
import com.xiaoi.expo.management.dictionary.service.DictionaryService;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Qu, De-Hui
 * @Description:
 * @date 2018/3/21
 */
@Service
public class DictionaryServiceImpl implements DictionaryService{

    @Autowired
    private DictionaryDao dictionaryDao;

	@Override
	public List<Dictionary> getByParentId(String parentId) {
		return dictionaryDao.findAllByParentIdOrderByPriorityAsc(parentId);
	}

	@Override
	public Dictionary findByValue(String value) {
		return dictionaryDao.findByValue(value);
	}

	@Override
	public Set<Dictionary> findAllByIdIn(List<String> values) {
		return dictionaryDao.findAllByIdIn(values);
	}
	
	@Override
	public Dictionary findOne(String id) {
		return dictionaryDao.getOne(id);
	}
}
