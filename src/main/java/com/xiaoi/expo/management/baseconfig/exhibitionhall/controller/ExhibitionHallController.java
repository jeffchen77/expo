package com.xiaoi.expo.management.baseconfig.exhibitionhall.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.common.utils.FtpUtils;
import com.xiaoi.expo.management.baseconfig.exhibition.entity.Exhibition;
import com.xiaoi.expo.management.baseconfig.exhibition.service.ExhibitionService;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.entity.ExhibitionBooth;
import com.xiaoi.expo.management.baseconfig.exhibitionhall.entity.ExhibitionHall;
import com.xiaoi.expo.management.baseconfig.exhibitionhall.service.ExhibitionHallService;

/**
 * @author chen fei
 * @Description: 议会厅controller层
 * @date 2018/3/27 09:20
 */
@Controller
@RequestMapping(value = "/management/config/exhibitionhall")
public class ExhibitionHallController {

    private Logger logger =  LoggerFactory.getLogger(ExhibitionHallController.class);
    private static final String LIST = "config/exhibitionhall/list"; // 列表页面;
    private static final String ADD = "config/exhibitionhall/add"; // 新增页面
    private static final String VIEW = "config/exhibitionhall/view"; // 查看页面
    private static final String EDIT = "config/exhibitionhall/add"; // 编辑页面

    @Autowired
    private ExhibitionHallService exhibitionHallService;
    
    @Autowired
    private ExhibitionService exhibitionService;
    
    @Autowired
    private FtpUtils ftpUtils;
    
    /**
     * @Description: 加载议会厅列表页面
     * @return String
     * @author chen fei
     * @date 2018/3/27 09:24
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String exhibitionHallList(Model model){
    	model.addAttribute("ftpDomain", ftpUtils.getDomain());
        return LIST;
    }
    
    /**
     * @Description: 跳转议会厅新增页面
     * @return String
     * @author chen fei
     * @date 2018/3/27 09:24
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model){
    	ExhibitionHall exhibitionHall =new ExhibitionHall();
    	exhibitionHall.setExhibition(new Exhibition());
    	model.addAttribute("exhibitionHall",exhibitionHall);
    	model.addAttribute("list",exhibitionService.findAll());
        return ADD;
    }
    
    /**
     * @Description: 跳转议会厅查看页面
     * @param exhibitionHallId 议会厅id
     * @return String
     * @author chen fei
     * @date 2018/3/27 09:24
     */
    @RequestMapping(value = "view", method = RequestMethod.GET)
    public String view(String exhibitionHallId, Model model){
    	logger.debug("exhibitionHallId:"+exhibitionHallId);
    	ExhibitionHall exhibitionHall =exhibitionHallService.findOne(exhibitionHallId);
    	
    	if(!StringUtils.isEmpty(exhibitionHall.getPicture())) {
        	exhibitionHall.setPicture(ftpUtils.getDomain()+exhibitionHall.getPicture());
        	}
    	model.addAttribute("exhibitionHall",exhibitionHall);    
        return VIEW;
    }
    
    /**
     * @Description: 跳转议会厅编辑页面
     * @param exhibitionHallId 议会厅id
     * @return String
     * @author chen fei
     * @date 2018/3/27 09:24
     */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit(String exhibitionHallId, Model model){
    	logger.debug("exhibitionHallId:"+exhibitionHallId);
    	ExhibitionHall exhibitionHall =exhibitionHallService.findOne(exhibitionHallId);
    	
    	if(!StringUtils.isEmpty(exhibitionHall.getPicture())) {
        	model.addAttribute("imgUrl",ftpUtils.getDomain()+exhibitionHall.getPicture());
        	}
    	model.addAttribute("exhibitionHall",exhibitionHall);   
    	model.addAttribute("list", exhibitionService.findAll());
        return EDIT;
    }
    
    /**
     * @Description: 分页查询议会厅信息
     * @param page 页码
     * @param  limit 每页显示条数
     * @param searchName 搜索关键词
     * @return PageResult<ExhibitionHall>
     * @author chen fei
     * @date 2018/3/27 09:24
     */
    @RequestMapping(value = "findByPagging", method = RequestMethod.GET)
    @ResponseBody
    public PageResult<ExhibitionHall> findByPagging(Integer page, Integer limit, String searchName){
        if(page == null) {
            page = 0;
            limit = 10;
        }
        logger.debug("searchName:"+ searchName);
        return exhibitionHallService.getExhibitionHalls(page, limit, searchName);
    }

    /**
     * @Description: 保存议会厅信息
     * @param exhibitionHall 议会厅信息
     * @return MapResult
     * @author chen fei
     * @date 2018/3/27 09:24
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public MapResult save(ExhibitionHall exhibitionHall){
        ExhibitionHall oldExhibitionHall = null;
        if(!StringUtils.isEmpty(exhibitionHall.getId())){
            oldExhibitionHall = exhibitionHallService.findOne(exhibitionHall.getId());
            if(oldExhibitionHall == null){
                return MapResult.error("议会厅不存在或已被删除");
            }
            oldExhibitionHall.setId(exhibitionHall.getId());
            oldExhibitionHall.setName(exhibitionHall.getName());
            oldExhibitionHall.setExhibition(exhibitionHall.getExhibition());
            oldExhibitionHall.setPicture(exhibitionHall.getPicture());
            oldExhibitionHall.setDescription(exhibitionHall.getDescription());
            oldExhibitionHall.setUpdateTime(new Date());            
        }else{
        	oldExhibitionHall = exhibitionHall;        	
        	
        	oldExhibitionHall.setStatus("0");
        	oldExhibitionHall.setCreateTime(new Date());
        	oldExhibitionHall.setUpdateTime(new Date());
        	
        }
        return exhibitionHallService.save(oldExhibitionHall);
    }

  

    /**
     * @Description: 删除议会厅
     * @param exhibitionHallId 议会厅id
     * @return MapResult
     * @author chen fei
     * @date 2018/3/27 09:24
     */
    @RequestMapping(value = "deleteExhibitionHall", method = RequestMethod.POST)
    @ResponseBody
    public MapResult deleteExhibitionHall(String exhibitionHallId){
        ExhibitionHall exhibitionHall = exhibitionHallService.findOne(exhibitionHallId);
        if(exhibitionHall == null){
            return MapResult.error("议会厅不存在或已被删除");
        }
        return exhibitionHallService.delete(exhibitionHallId);
    }
    
    /**
	 * @Description: 返回所有的议会厅信息，不分页
	 * @return List<ExhibitionHall>
	 * @author chen,fei
	 * @date 2018/3/27
	 */
	@RequestMapping(value = "findAll", method = RequestMethod.GET)
	@ResponseBody
	public List<ExhibitionHall> findAll() {
		return exhibitionHallService.findAll();
	}
	
	 /**
	 * @Description: 返回议会厅信息，不分页
	 * @param exhibitionHallId 会场id
	 * @return List<ExhibitionHall>
	 * @author chen,fei
	 * @date 2018/3/28
	 */
	@RequestMapping(value = "findExhibitionHall", method = RequestMethod.GET)
	@ResponseBody
	public List<ExhibitionHall> findExhibitionHall(String exhibitionId) {
			return exhibitionHallService.findExhibitionHall(exhibitionId);
		}   
}
