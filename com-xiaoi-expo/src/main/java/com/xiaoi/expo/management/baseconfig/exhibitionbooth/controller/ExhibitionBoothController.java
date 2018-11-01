package com.xiaoi.expo.management.baseconfig.exhibitionbooth.controller;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.entity.ExhibitionBooth;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.service.ExhibitionBoothService;
import com.xiaoi.expo.management.dictionary.service.DictionaryService;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Qu, De-Hui
 * @Description: 站台库管理
 * @date 2018/3/20
 */
@Controller
@RequestMapping(value = "/management/config/exhibitionbooth")
public class ExhibitionBoothController {

	private Logger logger = LoggerFactory.getLogger(ExhibitionBoothController.class);

	private static final String EXHIBITIONBOOTH_LIST = "config/exhibitionbooth/list";
	
	private static final String EXHIBITIONBOOTH_ADD = "config/exhibitionbooth/add";
	
	private static final String EXHIBITIONBOOTH_VIEW = "config/exhibitionbooth/view";
	
	private static final String EXHIBITIONBOOTH_EDIT = "config/exhibitionbooth/edit";

	@Autowired
	private ExhibitionBoothService exhibitionBoothService;

	@Autowired
    private DictionaryService dictionaryService;
	
	@Value("${ftp.domain}")
    private String ftpDomain;
	/**
	 * @Description: 加载展台列表页面
	 * @return String
	 * @author Qu, De-Hui
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list() {
		return EXHIBITIONBOOTH_LIST;
	}
	
	/**
	 * @Description: 加载展台新增页面
	 * @return String
	 * @author Qu, De-Hui
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add(Model model) {
		ExhibitionBooth exhibitionBooth = new ExhibitionBooth();
		model.addAttribute("exhibitionBooth", exhibitionBooth);
		return EXHIBITIONBOOTH_ADD;
	}

	/**
	 * @Description: 分页查询展台信息
	 * @param page
	 *            页码
	 * @param limit
	 *            每页显示条数
	 * @param searchName
	 *            搜索关键词
	 * @return PageResult<User>
	 * @author Qu, De-Hui
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "findByPagging", method = RequestMethod.GET)
	@ResponseBody
	public PageResult<ExhibitionBooth> findByPagging(Integer page, Integer limit, String searchName) {
		if (page == null) {
			page = 0;
			limit = 10;
		}
		return convertImgUrl(exhibitionBoothService.getExhibitionBooth(page, limit, searchName));
	}
	
	/**
	 * @Description: 删除展台信息
	 * @return MapResult
	 * @author Qu, De-Hui
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public MapResult delete(String exhibitionId) {
		return exhibitionBoothService.delete(exhibitionId);
	}
	
	/**
     * @Description: 保存展台信息
     * @param dto 展台信息DTO
     * @return MapResult
     * @author Qu, De-Hui
     * @date 2018/3/21
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public MapResult save(ExhibitionBooth exhibitionBooth){
    	ExhibitionBooth exhibition = null;
    	if(!StringUtils.isEmpty(exhibitionBooth.getId())) {
    		//更新
    		exhibition = exhibitionBoothService.findOne(exhibitionBooth.getId());
    		if(exhibition!=null){
    			exhibition.setCode(exhibitionBooth.getCode());
    			exhibition.setPosition(exhibitionBooth.getPosition());
    			exhibition.setPictureAddr(exhibitionBooth.getPictureAddr());
    			exhibition.setUpdateTime(new Date());
    		}
    		else {
    			MapResult.error("保存展台信息出错");
    		}
    	}else {
    		//新增
    		exhibition = exhibitionBooth;
    		exhibition.setCreateTime(new Date());
    		exhibition.setUpdateTime(new Date());
    		exhibition.setStatus("0");          
    	}
    	return exhibitionBoothService.save(exhibition);
    }
    
    /**
	 * @Description: 加载展台详情界面
	 * @return String
	 * @author Qu, De-Hui
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(Model model, String id) {
		ExhibitionBooth exhibitionBooth = exhibitionBoothService.findOne(id);
		if(!StringUtils.isEmpty(exhibitionBooth.getPictureAddr())) {
			exhibitionBooth.setPictureAddr(ftpDomain+exhibitionBooth.getPictureAddr());
		}
		model.addAttribute("exhibitionBooth", exhibitionBooth);
		return EXHIBITIONBOOTH_VIEW;
	}
	
	/**
	 * @Description: 修改展台详情界面
	 * @return String
	 * @author Qu, De-Hui
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(Model model, String id) {
		ExhibitionBooth exhibitionBooth = exhibitionBoothService.findOne(id);
		model.addAttribute("exhibitionBooth", exhibitionBooth);
		model.addAttribute("ftpDomain", ftpDomain);
		model.addAttribute("pavlist",dictionaryService.getByParentId("PAV"));
		return EXHIBITIONBOOTH_EDIT;
	}
	
	/**
     * @Description: 返回展台信息
     * @return List<ExhibitionBooth>
     * @author Qu, De-Hui
     * @date 2018/3/21
     */
    @RequestMapping(value = "listExhibitions", method = RequestMethod.GET)
    @ResponseBody
    public List<ExhibitionBooth> listExhibitions(){
    	return exhibitionBoothService.findExhibitionBoothWithoutUsed();
    }
    
    /**
	 * @Description: 返回所有的展台信息，不分页
	 * @return List<ExhibitionBooth>
	 * @author chen,fei
	 * @date 2018/3/28
	 */
	@RequestMapping(value = "findAll", method = RequestMethod.GET)
	@ResponseBody
	public List<ExhibitionBooth> findAll() {
		return exhibitionBoothService.findAll();
	}
	
	 /**
		 * @Description: 返回所有的展台信息，不分页
		 * @return List<ExhibitionBooth>
		 * @author chen,fei
		 * @date 2018/3/28
		 */
	@RequestMapping(value = "findExhibitionBooth", method = RequestMethod.GET)
	@ResponseBody
	public List<ExhibitionBooth> findExhibitionBooth(String pavilionId) {
			return exhibitionBoothService.findExhibitionBooth(pavilionId);
		}
	
	/**
	 * @Description: 转换图片的url地址
	 * @return List<ExhibitionBooth>
	 * @author chen,fei
	 * @date 2018/3/28
	 */
	private PageResult<ExhibitionBooth> convertImgUrl(PageResult<ExhibitionBooth> data) {
		if(data != null) {
			List<ExhibitionBooth> exhibitionBooth = data.getData();
			for(ExhibitionBooth exh : exhibitionBooth) {
				if(exh != null && !StringUtils.isEmpty(exh.getPictureAddr())) {
					exh.setPictureAddr(ftpDomain+exh.getPictureAddr());
				}
			}
		}
		return data;
	}
}
