package com.xiaoi.expo.management.baseconfig.exhibition.service;

import java.util.List;
import java.util.Map;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.management.baseconfig.exhibition.entity.Exhibition;

/**
 * @author chen fei
 * @Description: 会场库service层
 * @date 2018/3/23 09:20
 */
public interface ExhibitionService {
    
    /**
     * @Description: 分页查询会场信息
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @param searchKey 查询条件key
     * @param searchValue 查询条件value
     * @return PageResult<Exhibition>
     * @author chen fei
     * @date 2018/3/23 09:24
     */
    PageResult<Exhibition> getExhibitions(int pageNum, int pageSize, String searchName);

    /**
     * @Description: 保存会场信息
     * @param exhibition 会场信息
     * @return MapResult
     * @author chen fei
     * @date 2018/3/23 09:24
     */
    MapResult save(Exhibition exhibition);

     /**
     * @Description: 根据会场id查询会场信息
     * @param exhibitionId 会场id
     * @return Exhibition
     * @author chen fei
     * @date 2018/3/23 09:24
     */
    Exhibition findOne(String exhibitionId);
    
    /**
     * @Description: 删除会场信息
     * @param exhibitionId 会场id
     * @return MapResult
     * @author chen fei
     * @date 2018/3/23 09:24
     */
    MapResult delete(String exhibitionId);
    
    /**
     * @Description: 所有会场信息
     * @return List<Exhibition>
     * @author chen fei
     * @date 2018/3/23 09:24
     */
    List<Exhibition> findAll();

    /**
     * @Description: 分页查询会场信息(大屏接口)
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @return PageResult<Map<String, Object>>
     * @author bright.liang
     * @date 2018/3/20
     */
    PageResult<Map<String, Object>> findByPage(int pageNum, int pageSize);


}
