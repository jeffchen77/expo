package com.xiaoi.expo.management.baseconfig.enterprise.service;

import java.util.List;
import java.util.Map;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.management.baseconfig.enterprise.dto.EnterpriseDTO;
import com.xiaoi.expo.management.baseconfig.enterprise.entity.Enterprise;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.entity.ExhibitionBooth;

/**
 * @author Qu, De-Hui
 * @Description: 企业管理service层
 * @date 2018/3/20
 */
public interface EnterpriseService {
  
    /**
     * @Description: 分页查询企业库信息
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @param searchKey 查询条件key
     * @param searchValue 查询条件value
     * @return PageResult<Enterprise>
     * @author Qu, De-Hui
     * @date 2018/3/20
     */
    PageResult<Enterprise> getEnterprise(int pageNum, int pageSize, String searchName);

    /**
     * @Description: 保存企业信息
     * @param dto 企业信息
     * @return MapResult
     * @author Qu, De-Hui
     * @date 2018/3/20
     */
    MapResult save(EnterpriseDTO dto);

    /**
     * @Description: 检测是否有对应的展台
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @return PageResult<Enterprise>
     * @author bright.liang
     * @date 2018/3/20
     */
    ExhibitionBooth checkExhibitionBooth(String name);
    
    /**
     * @Description: 根据企业id查询企业信息
     * @param enterpriseId 企业id
     * @return Enterprise
     * @author Qu, De-Hui
     * @date 2018/3/20
     */
    Enterprise findOne(String enterpriseId);
    
    /**
     * @Description: 根据企业id删除企业信息
     * @param enterpriseId 企业id
     * @return MapResult
     * @author Qu, De-Hui
     * @date 2018/3/21
     */
    MapResult delete(String enterpriseId);
    
    /**
     * @Description: 查询所有企业信息
     * @return List
     * @author Qu, De-Hui
     * @date 2018/3/21
     */
    List<Enterprise> findAll();
    
    /**
     * @Description: 保存企业信息
     * @param dto 企业信息
     * @return MapResult
     * @author Qu, De-Hui
     * @date 2018/3/20
     */
    MapResult update(EnterpriseDTO dto);


    /**
     * @Description: 分页查询企业库信息(大屏接口)
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @return PageResult<Enterprise>
     * @author bright.liang
     * @date 2018/3/20
     */
    PageResult<Map<String, Object>> findByPage(int pageNum, int pageSize);
    
    /**
     * @Description: 根据企业名称查询企业信息
     * @param enterpriseId 企业name
     * @return List<Enterprise>
     * @author Qu, De-Hui
     * @date 2018/3/20
     */
    List<Enterprise> findByNameLike(String name);
    
}
