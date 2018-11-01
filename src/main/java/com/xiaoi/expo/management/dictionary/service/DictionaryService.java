package com.xiaoi.expo.management.dictionary.service;

import java.util.List;
import java.util.Set;

import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.management.dictionary.entity.Dictionary;

/**
 * @author Qu, De-Hui
 * @Description: 数据字典接口
 * @date 2018/3/1616:05
 */
public interface DictionaryService {
    /**
     * @Description: 根据parentID查询
     * @return List<Dictionary>
     * @author Qu, De-Hui
     * @date 2018/3/13 18:04
     */
    List<Dictionary> getByParentId(String parentId);
    
    /**
     * @Description: 根据parentID查询
     * @return Dictionary
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    Dictionary findByValue(String value);
    
    /**
     * @Description: 根据parentID查询
     * @return Dictionary
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    Set<Dictionary> findAllByIdIn(List<String> values);
    
    /**
     * @Description: 根据ID查询
     * @return Dictionary
     * @author Qu, De-Hui
     * @date 2018/3/13 18:04
     */
    Dictionary findOne(String id);
}
