package com.xiaoi.expo.management.baseconfig.exhibitionbooth.service;

import java.util.List;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.entity.ExhibitionBooth;

/**
 * @author Qu, De-Hui
 * @Description: 展台管理service层
 * @date 2018/3/20
 */
public interface ExhibitionBoothService {
  
    /**
     * @Description: 分页查询展台库信息
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @param searchKey 查询条件key
     * @param searchValue 查询条件value
     * @return PageResult<ExhibitionBooth>
     * @author Qu, De-Hui
     * @date 2018/3/20
     */
    PageResult<ExhibitionBooth> getExhibitionBooth(int pageNum, int pageSize, String searchName);

    /**
     * @Description: 保存展台信息
     * @param exhibitionBooth 展台信息
     * @return MapResult
     * @author Qu, De-Hui
     * @date 2018/3/20
     */
    MapResult save(ExhibitionBooth exhibitionBooth);

    /**
     * @Description: 根据展台id查询展台信息
     * @param exhibitionBoothId 展台id
     * @return ExhibitionBooth
     * @author Qu, De-Hui
     * @date 2018/3/20
     */
    ExhibitionBooth findOne(String exhibitionBoothId);
    
    /**
     * @Description: 根据展台id删除展台信息
     * @param exhibitionBoothId 展台id
     * @return MapResult
     * @author Qu, De-Hui
     * @date 2018/3/21
     */
    MapResult delete(String exhibitionBoothId);
    
    /**
     * @Description: 更新展台信息
     * @param exhibitionBooth 展台信息
     * @return MapResult
     * @author Qu, De-Hui
     * @date 2018/3/20
     */
    MapResult update(ExhibitionBooth exhibitionBooth);
    
    /**
     * @Description: 更新展台信息
     * @param exhibitionBooth 展台信息
     * @return MapResult
     * @author Qu, De-Hui
     * @date 2018/3/20
     */
    List<ExhibitionBooth> findExhibitionBoothWithoutUsed();
    
    /**
     * @Description: 展台信息
     * @param pavilionId 展馆Id
     * @return List<ExhibitionBooth>
     * @author chen fei
     * @date 2018/3/28
     */
    List<ExhibitionBooth> findExhibitionBooth(String pavilionId);
    
    /**
     * @Description: 所有展台信息
     * @return List<ExhibitionBooth>
     * @author chen fei
     * @date 2018/3/28
     */
    List<ExhibitionBooth> findAll();
    
    /**
     * @Description: 所有展台信息
     * @return List<ExhibitionBooth>
     * @author chen fei
     * @date 2018/3/28
     */
    int countById(String exhibitionBoothId);
    
    /**
     * @Description: 根据展台名字查询
     * @return List<ExhibitionBooth>
     * @author chen fei
     * @date 2018/3/28
     */
    ExhibitionBooth findByCode(String code);
    
}
