package com.xiaoi.expo.management.baseconfig.exhibition.controller;

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

/**
 * @author chen fei
 * @Description: 会场库controller层
 * @date 2018/3/23 09:20
 */
@Controller
@RequestMapping(value = "/management/config/exhibition")
public class ExhibitionController {

    private Logger logger =  LoggerFactory.getLogger(ExhibitionController.class);
    private static final String LIST = "config/exhibition/list"; // 列表页面;
    private static final String ADD = "config/exhibition/add"; // 新增页面
    private static final String VIEW = "config/exhibition/view"; // 查看页面
    private static final String EDIT = "config/exhibition/add"; // 编辑页面

    @Autowired
    private ExhibitionService exhibitionService;
    
    @Autowired
    private FtpUtils ftpUtils;
    
    /**
     * @Description: 加载会场列表页面
     * @return String
     * @author chen fei
     * @date 2018/3/23 09:24
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String exhibitionList(Model model){
    	model.addAttribute("ftpDomain", ftpUtils.getDomain());
        return LIST;
    }
    
    /**
     * @Description: 跳转会场新增页面
     * @return String
     * @author chen fei
     * @date 2018/3/23 09:24
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model){
    	Exhibition exhibition =new Exhibition();
    	model.addAttribute("exhibition",exhibition);  
        return ADD;
    }
    
    /**
     * @Description: 跳转会场查看页面
     * @param exhibitionId 会场id
     * @return String
     * @author chen fei
     * @date 2018/3/23 09:24
     */
    @RequestMapping(value = "view", method = RequestMethod.GET)
    public String view(String exhibitionId, Model model){
    	logger.debug("exhibitionId:"+exhibitionId);
    	Exhibition exhibition =exhibitionService.findOne(exhibitionId);
    	
    	if(!StringUtils.isEmpty(exhibition.getPicture())) {
        	exhibition.setPicture(ftpUtils.getDomain()+exhibition.getPicture());
        	}
    	model.addAttribute("exhibition",exhibition);    
        return VIEW;
    }
    
    /**
     * @Description: 跳转会场编辑页面
     * @param exhibitionId 会场id
     * @return String
     * @author chen fei
     * @date 2018/3/23 09:24
     */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit(String exhibitionId, Model model){
    	logger.debug("exhibitionId:"+exhibitionId);
    	Exhibition exhibition =exhibitionService.findOne(exhibitionId);
    	
    	if(!StringUtils.isEmpty(exhibition.getPicture())) {
    	model.addAttribute("imgUrl",ftpUtils.getDomain()+exhibition.getPicture());
    	}
    	model.addAttribute("exhibition",exhibition);   
        return EDIT;
    }
    
    /**
     * @Description: 分页查询会场信息
     * @param page 页码
     * @param  limit 每页显示条数
     * @param searchName 搜索关键词
     * @return PageResult<Exhibition>
     * @author chen fei
     * @date 2018/3/23 09:24
     */
    @RequestMapping(value = "findByPagging", method = RequestMethod.GET)
    @ResponseBody
    public PageResult<Exhibition> findByPagging(Integer page, Integer limit, String searchName){
        if(page == null) {
            page = 0;
            limit = 10;
        }
        logger.debug("searchName:"+ searchName);
        return exhibitionService.getExhibitions(page, limit, searchName);
    }

    /**
     * @Description: 保存会场信息
     * @param exhibition 会场信息
     * @return MapResult
     * @author chen fei
     * @date 2018/3/23 09:24
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public MapResult save(Exhibition exhibition){
        Exhibition oldExhibition = null;
        if(!StringUtils.isEmpty(exhibition.getId())){
            oldExhibition = exhibitionService.findOne(exhibition.getId());
            if(oldExhibition == null){
                return MapResult.error("会场不存在或已被删除");
            }
            oldExhibition.setId(exhibition.getId());
            oldExhibition.setName(exhibition.getName());
            oldExhibition.setAddr(exhibition.getAddr());
            oldExhibition.setPicture(exhibition.getPicture());
            oldExhibition.setUpdateTime(new Date());          
            oldExhibition.setDescription(exhibition.getDescription());
        }else{
        	oldExhibition = exhibition;
        	
        	oldExhibition.setStatus("0");
        	oldExhibition.setCreateTime(new Date());
        	oldExhibition.setUpdateTime(new Date());
        	
        }
        return exhibitionService.save(oldExhibition);
    }

  

    /**
     * @Description: 删除会场
     * @param exhibitionId 会场id
     * @return MapResult
     * @author chen fei
     * @date 2018/3/23 09:24
     */
    @RequestMapping(value = "deleteExhibition", method = RequestMethod.POST)
    @ResponseBody
    public MapResult deleteExhibition(String exhibitionId){
        Exhibition exhibition = exhibitionService.findOne(exhibitionId);
        if(exhibition == null){
            return MapResult.error("会场不存在或已被删除");
        }
        return exhibitionService.delete(exhibitionId);        
    }
    
    /**
	 * @Description: 返回所有的会场信息，不分页
	 * @return List<Exhibition>
	 * @author chen,fei
	 * @date 2018/3/23
	 */
	@RequestMapping(value = "findAll", method = RequestMethod.GET)
	@ResponseBody
	public List<Exhibition> findAll() {
		return exhibitionService.findAll();
	}
	
	   
}
