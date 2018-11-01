package com.xiaoi.expo.management.baseconfig.exhibitionhall.service;

import java.util.List;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.management.baseconfig.exhibitionhall.entity.ExhibitionHall;

/**
 * @author chen fei
 * @Description: 议会厅厅service层
 * @date 2018/3/27 09:20
 */
public interface ExhibitionHallService {
    
    /**
     * @Description: 分页查询议会厅信息
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @param searchKey 查询条件key
     * @param searchValue 查询条件value
     * @return PageResult<ExhibitionHall>
     * @author chen fei
     * @date 2018/3/27 09:24
     */
    PageResult<ExhibitionHall> getExhibitionHalls(int pageNum, int pageSize, String searchName);

    /**
     * @Description: 保存议会厅信息
     * @param exhibitionHall 议会厅信息
     * @return MapResult
     * @author chen fei
     * @date 2018/3/27 09:24
     */
    MapResult save(ExhibitionHall exhibitionHall);

     /**
     * @Description: 根据议会厅id查询议会厅信息
     * @param exhibitionHallId 议会厅id
     * @return ExhibitionHall
     * @author chen fei
     * @date 2018/3/27 09:24
     */
    ExhibitionHall findOne(String exhibitionHallId);
    
    /**
     * @Description: 删除议会厅信息
     * @param exhibitionHallId 议会厅id
     * @return MapResult
     * @author chen fei
     * @date 2018/3/27 09:24
     */
    MapResult delete(String exhibitionHallId);
    
    /**
     * @Description: 所有议会厅信息
     * @return List<ExhibitionHall>
     * @author chen fei
     * @date 2018/3/27 09:24
     */
    List<ExhibitionHall> findAll();
    
    /**
     * @Description: 所有议会厅信息
     * @param exhibitionId 会场id
     * @return List<ExhibitionHall>
     * @author chen fei
     * @date 2018/3/28 09:24
     */
    List<ExhibitionHall> findExhibitionHall(String exhibitionId);
}
