package com.xiaoi.expo.management.baseconfig.enterprise.controller;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.management.baseconfig.enterprise.dto.EnterpriseDTO;
import com.xiaoi.expo.management.baseconfig.enterprise.entity.Enterprise;
import com.xiaoi.expo.management.baseconfig.enterprise.service.EnterpriseService;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.entity.ExhibitionBooth;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.service.ExhibitionBoothService;
import com.xiaoi.expo.management.dictionary.entity.Dictionary;
import com.xiaoi.expo.management.dictionary.service.DictionaryService;
import com.xiaoi.expo.management.enterexhibition.entity.EnterpriseExhibition;
import com.xiaoi.expo.management.enterexhibition.service.EnterpriseExhibitionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
 * @Description: 企业库管理
 * @date 2018/3/20
 */
@Controller
@RequestMapping(value = "/management/config/enterprise")
public class EnterpriseController {

	private Logger logger = LoggerFactory.getLogger(EnterpriseController.class);

	private static final String ENTERPRISE_LIST = "config/enterprise/list";
	
	private static final String ENTERPRISE_ADD = "config/enterprise/add";
	
	private static final String ENTERPRISE_VIEW = "config/enterprise/view";
	
	private static final String ENTERPRISE_EDIT = "config/enterprise/edit";
	
	//企业类型
	private static final String ENTERPRISE_TYPE = "ENTTYPE";
	
	//展出类型
	private static final String ENTERPRISE_SHOW_TYPE = "SHOWTYPE";

	@Autowired
	private EnterpriseService enterpriseService;
	
	@Autowired
	private DictionaryService dictionaryService;
	
	@Autowired
	private EnterpriseExhibitionService enterpriseExhibitionService;
	
	@Autowired
	private ExhibitionBoothService exhibitionBoothService;
	
	@Value("${enterprise.separator}")
    private String separator;
	
	@Value("${ftp.domain}")
    private String ftpDomain;
	
	/**
	 * @Description: 加载企业列表页面
	 * @return String
	 * @author Qu, De-Hui
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list() {
		return ENTERPRISE_LIST;
	}
	
	/**
	 * @Description: 加载企业新增页面
	 * @return String
	 * @author Qu, De-Hui
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add() {
		return ENTERPRISE_ADD;
	}

	/**
	 * @Description: 分页查询企业信息
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
	public PageResult<Enterprise> findByPagging(Integer page, Integer limit, String searchName) {
		if (page == null) {
			page = 0;
			limit = 10;
		}
		//处理搜索条件为展台的情况
		ExhibitionBooth eb = null;
		if(!StringUtils.isEmpty(searchName)) {
			eb = enterpriseService.checkExhibitionBooth(searchName);
			if(eb != null) {
				EnterpriseExhibition relation = enterpriseExhibitionService.findByExhibitionId(eb.getId());
				if(relation != null) {
					Enterprise enterprise = enterpriseService.findOne(relation.getEnterpriseId());
					if(enterprise != null) {
						PageResult<Enterprise> pageResult = new PageResult<Enterprise>();
						List<Enterprise> list = new ArrayList<Enterprise>();
						list.add(enterprise);
						pageResult.setData(list);
				        pageResult.setCount(1);
				        return convertHighLight(pageResult);
					}
				}
			}
		}
		return convertHighLight(enterpriseService.getEnterprise(page, limit, searchName));
	}
	
	/**
	 * @Description: 删除企业信息
	 * @return MapResult
	 * @author Qu, De-Hui
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public MapResult delete(String enterpriseId) {
		return enterpriseService.delete(enterpriseId);
	}
	
	/**
	 * @Description: 返回所有的企业信息，不分页
	 * @return List<Enterprise>
	 * @author Qu, De-Hui
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "findAll", method = RequestMethod.GET)
	@ResponseBody
	public List<Enterprise> findAll() {
		return enterpriseService.findAll();
	}
	
	/**
     * @Description: 保存企业信息
     * @param dto 企业信息DTO
     * @return MapResult
     * @author Qu, De-Hui
     * @date 2018/3/21
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public MapResult save(EnterpriseDTO dto){
        System.out.println(dto);
        //return MapResult.ok();
        return enterpriseService.save(dto);
    }
    
    /**
     * @Description: 转换Enterpirse对象
     * @param dto 企业信息DTO
     * @return PageResult<Enterprise>
     * @author Qu, De-Hui
     * @date 2018/3/21
     */
    private PageResult<Enterprise> convertHighLight(PageResult<Enterprise> enterPrise){
    	if(enterPrise!=null && enterPrise.getData()!=null && enterPrise.getData().size()>0) {
    		List<Enterprise> epList = enterPrise.getData();
    		for(int i=0; i<epList.size(); i++) {
    			Enterprise ep = epList.get(i);
    			if(ep != null && ep.getShowHighlights() != null) {
    				String hlight = ep.getShowHighlights();
    				ep.setShowHighlights(hlight.replaceAll(separator, "、"));
    			}
    			//log地址加上domian
    			if(!StringUtils.isEmpty(ep.getLogo())) {
    				ep.setLogo(ftpDomain+ep.getLogo());
    			}
    			//加载展台信息
    			ep.setExhibtionList(listExhibition(ep.getId()));   			
    		}
    	}
    	return enterPrise;
    }
    
    /**
	 * @Description: 加载企业详情界面
	 * @return String
	 * @author Qu, De-Hui
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(Model model, String id) {
		String[] strArr = null;
		Enterprise enterprise = enterpriseService.findOne(id);
		if(enterprise != null) {			
			if(!StringUtils.isEmpty(enterprise.getShowHighlights())) {
				strArr = enterprise.getShowHighlights().split(separator);
			}else {
				strArr = new String[] {""};
			}
			model.addAttribute("hightLight", strArr);
			Set<Dictionary> themeList = enterprise.getShowTypeDic();
			String themeListStr = "";
			if(themeList!=null && themeList.size() > 0) {
				for(Dictionary dic : themeList) {
					if(dic != null) {
						themeListStr += dic.getDisplayName();
						themeListStr += "、";
					}
				}
				if(!StringUtils.isEmpty(themeListStr)) {
					themeListStr = themeListStr.substring(0, themeListStr.length()-"、".length());
				}
			}
			model.addAttribute("themeList", themeListStr);
			//log地址加上domian
			if(!StringUtils.isEmpty(enterprise.getLogo())) {
				enterprise.setLogo(ftpDomain+enterprise.getLogo());
			}			
			//显示展台编号
			enterprise.setExhibtionList(listExhibition(enterprise.getId()));
			model.addAttribute("enterprise", enterprise);
		}
		return ENTERPRISE_VIEW;
	}
	
	/**
	 * @Description: 修改企业详情界面
	 * @return String
	 * @author Qu, De-Hui
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(Model model, String id) {
		Enterprise enterprise = enterpriseService.findOne(id);
		model.addAttribute("ftpDomain", ftpDomain);
		model.addAttribute("enterprise", enterprise);
		//企业亮点
		if(enterprise.getShowHighlights()!=null){
			String[] strArr = enterprise.getShowHighlights().split(separator);
			model.addAttribute("hLight", strArr);
		}else {
			model.addAttribute("hLight", null);
		}

		//企业类型下拉框
		List<Dictionary> enterType = dictionaryService.getByParentId(ENTERPRISE_TYPE);
		model.addAttribute("enterType", enterType);
		//展出类型下拉框
		List<Dictionary> showType = dictionaryService.getByParentId(ENTERPRISE_SHOW_TYPE);
		model.addAttribute("showType", showType);
		//展台下拉框列表
		List<ExhibitionBooth> exhibitionBooth = new ArrayList<ExhibitionBooth>();
		
		//封装选中的展出类型的value值
		List<String> selectedItem = null;
		Set<Dictionary> selectedItemSet = enterprise.getShowTypeDic();
		if(selectedItemSet!=null && selectedItemSet.size() > 0) {
			selectedItem = new ArrayList<String>();
			for (Dictionary dic : selectedItemSet) {  
				selectedItem.add(dic.getValue());  
			}  
		}
		model.addAttribute("selectedItem", selectedItem);
		
		//封装选中的展台列表
		List<String> selectedExhItem = null;
		List<EnterpriseExhibition> entExh = enterpriseExhibitionService.findAllByEnterpriseId(enterprise.getId());
		if(entExh!=null && entExh.size()>0) {
			selectedExhItem = new ArrayList<String>();
			for (EnterpriseExhibition dic : entExh) {
				if(dic!=null && dic.getExhibitionBooth()!=null) {
					selectedExhItem.add(dic.getExhibitionBooth().getId());
					exhibitionBooth.add(dic.getExhibitionBooth());
				}	
			}
		}
		exhibitionBooth.addAll(exhibitionBoothService.findExhibitionBoothWithoutUsed());
		model.addAttribute("exhibitionBooth", exhibitionBooth);
		
		model.addAttribute("selectedExhItem", selectedExhItem);
		return ENTERPRISE_EDIT;
	}
	
	/**
     * @Description: 保存企业信息
     * @param dto 企业信息DTO
     * @return MapResult
     * @author Qu, De-Hui
     * @date 2018/3/21
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public MapResult update(EnterpriseDTO dto){
        return enterpriseService.update(dto);
    }
    
    private List<ExhibitionBooth> listExhibition(String enterpriseId){
    	List<ExhibitionBooth> exh = new ArrayList<ExhibitionBooth>();
    	List<EnterpriseExhibition> enter = enterpriseExhibitionService.findAllByEnterpriseId(enterpriseId);
    	if(enter!=null && enter.size()>0) {
    		for(EnterpriseExhibition e : enter) {
    			exh.add(e.getExhibitionBooth());
    		}
    	}
    	return exh;
    }
}
