package com.xiaoi.expo.management.baseconfig.enterprise.service.impl;
import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.constants.Constants;
import com.xiaoi.expo.management.baseconfig.enterprise.dao.EnterpriseDao;
import com.xiaoi.expo.management.baseconfig.enterprise.dto.EnterpriseDTO;
import com.xiaoi.expo.management.baseconfig.enterprise.entity.Enterprise;
import com.xiaoi.expo.management.baseconfig.enterprise.service.EnterpriseService;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.entity.ExhibitionBooth;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.service.ExhibitionBoothService;
import com.xiaoi.expo.management.dictionary.entity.Dictionary;
import com.xiaoi.expo.management.dictionary.service.DictionaryService;
import com.xiaoi.expo.management.enterexhibition.entity.EnterpriseExhibition;
import com.xiaoi.expo.management.enterexhibition.service.EnterpriseExhibitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import java.util.*;

/**
 * @author Qu，De-Hui
 * @Description: 企业库管理serivce
 * @date 2018/3/20
 */
@Service
public class EnterpriseServiceImpl implements EnterpriseService{
    private Logger logger = LoggerFactory.getLogger(EnterpriseServiceImpl.class);

    @Autowired
    private EnterpriseDao enterpriseDao;
    
    @Autowired
	private DictionaryService dictionaryService;
    
    @Autowired
	private EnterpriseExhibitionService enterpriseExhibitionService;
	
	@Autowired
	private ExhibitionBoothService exhibitionBoothService;

	@Value("${ftp.domain}")
    private String domain;

	@Value("${enterprise.separator}")
    private String separator;

	@Override
	public PageResult<Enterprise> getEnterprise(int pageNum, int pageSize, String searchName) {
		PageResult<Enterprise> pageResult = new PageResult<Enterprise>();
        Sort sort = new Sort(Sort.Direction.DESC, new String[]{"updateDate"});
        PageRequest startPage = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<Enterprise> pageUser = enterpriseDao.findAll(new Specification<Enterprise>() {
            @Override
            public Predicate toPredicate(Root<Enterprise> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(!StringUtils.isEmpty(searchName)){
                	//查询字典表进行筛选
                	//predicates.add(criteriaBuilder.like(root.<Dictionary>get("typeDic").<String>get("displayName"), "%" + searchName + "%"));
                	//predicates.add(criteriaBuilder.like(root.<Dictionary>get("showTypeDic").<String>get("displayName"), "%" + searchName + "%"));
                	Join<Enterprise,Dictionary> typeJoin = root.join("typeDic",JoinType.LEFT);
                	predicates.add(criteriaBuilder.like(typeJoin.get("displayName"), "%" + searchName + "%"));
                	
                	SetJoin<Enterprise,Dictionary> showTypeJoin = root.join(root.getModel().getSet("showTypeDic",Dictionary.class) , JoinType.LEFT);
                	predicates.add(criteriaBuilder.like(showTypeJoin.get("displayName").as(String.class), "%" + searchName + "%"));
                	               	
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchName + "%"));
                    predicates.add(criteriaBuilder.like(root.get("theme"), "%" + searchName + "%"));
                    predicates.add(criteriaBuilder.like(root.get("showHighlights"), "%" + searchName + "%"));
                }
                if(predicates.size() > 0) {
                	criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));
                }
                return criteriaQuery.getRestriction();
            }
        }, startPage);
        pageResult.setData(pageUser.getContent());
        pageResult.setCount(pageUser.getTotalElements());
        return pageResult;
	}

	@Transactional
	@Override
	public MapResult save(EnterpriseDTO dto) {
		try{
			if(dto != null) {
				//如果展台信息被删除了
				if(isExhibitionDeleted(dto)) {
					return MapResult.error("保存失败，所选展台信息已经被删除了");
				}
	        	Enterprise ep = new Enterprise();
	        	//展出类型
	        	if(dto.getThemeTypeList() != null && !dto.getThemeTypeList().isEmpty()) {
	        		ep.setShowTypeDic(dictionaryService.findAllByIdIn(dto.getThemeTypeList()));
	        	}
	        	//企业类型
	        	if(!StringUtils.isEmpty(dto.getEnterpriseType())) {
		        	ep.setTypeDic(dictionaryService.findOne(dto.getEnterpriseType())); 
	        	}      	
	        	//企业名字
	        	ep.setName(dto.getEnterpriseName());
	        	//展出主题
	        	ep.setTheme(dto.getEnterpriseTheme());
	        	//企业log
	        	ep.setLogo(dto.getEnterPriseLog());
	        	//企业描述
	        	ep.setDescrption(dto.getEnterpriseDesc());
	        	//dataResource
	        	ep.setDataResource("0");
	        	//状态
	        	ep.setStatus("0");
	        	//展出亮点
	        	if(dto.getHightArr()!=null && dto.getHightArr().size()>0) {
	        		String highStr = "";
	        		for(int i=0; i<dto.getHightArr().size(); i++) {
	        			if(!StringUtils.isEmpty(dto.getHightArr().get(i))) {
	        				highStr+=dto.getHightArr().get(i);
	        				highStr+=separator;
	        			}	
	        		}
	        		if(!StringUtils.isEmpty(highStr)) {
	        			ep.setShowHighlights(highStr.substring(0, highStr.length()-separator.length()));
	        		}	
	        	}
	        	//创建时间
	        	ep.setCreateTime(new Date());
	        	//更新时间
	        	ep.setUpdateDate(new Date());
	        	
	        	enterpriseDao.save(ep);
	        	
	        	//保存展台信息到关联表
	        	saveExhibition(ep.getId(), dto);
			}
			else{
				return MapResult.error("保存企业信息出错");
			}
        }catch (Exception e){
            logger.error("保存企业信息出错");
            e.printStackTrace();
            return MapResult.error("保存企业信息出错");
        }
        return MapResult.ok();
	}

	@Override
	public Enterprise findOne(String enterpriseId) {
		return enterpriseDao.getOne(enterpriseId);
	}

	@Override
	public MapResult delete(String enterpriseId) {
		Enterprise ep = null;
		try{			
			ep = this.findOne(enterpriseId);
			if(ep == null) {
				return MapResult.error("企业信息不存在或已删除");
			}
			enterpriseDao.deleteEnterprise(enterpriseId);
			//删除关联表
        	enterpriseExhibitionService.deleteByEnterpriseId(ep.getId());
        }catch(DataIntegrityViolationException e) {
        	return MapResult.error("此企业信息已被引用，不能删除");
        }catch (Exception e){
            logger.error("删除企业信息出错，企业ID："+enterpriseId);
            e.printStackTrace();
            return MapResult.error("删除企业信息出错");
        }
        return MapResult.ok();
	}

	@Override
	public List<Enterprise> findAll() {
		return enterpriseDao.findAll();
	}

	@Transactional
	@Override
	public MapResult update(EnterpriseDTO dto) {
		try{
			if(dto!=null) {
				//如果展台信息被删除了
				if(isExhibitionDeleted(dto)) {
					return MapResult.error("保存失败，所选展台信息已经被删除了");
				}
				
	        	Enterprise ep = enterpriseDao.getOne(dto.getId());
	        	//展出类型
	        	if(dto.getThemeTypeList() != null && !dto.getThemeTypeList().isEmpty()) {
	        		ep.setShowTypeDic(dictionaryService.findAllByIdIn(dto.getThemeTypeList()));
	        	}else {
	        		ep.setShowTypeDic(new HashSet<Dictionary>());
	        	}
	        	//企业类型
	        	if(!StringUtils.isEmpty(dto.getEnterpriseType())) {
		        	ep.setTypeDic(dictionaryService.findOne(dto.getEnterpriseType()));   
	        	}
	        	//企业名字
	        	ep.setName(dto.getEnterpriseName());
	        	//展出主题
	        	ep.setTheme(dto.getEnterpriseTheme());
	        	//企业log
	        	ep.setLogo(dto.getEnterPriseLog());
	        	//企业描述
	        	ep.setDescrption(dto.getEnterpriseDesc());
	        	//展出亮点
	        	if(dto.getHightArr()!=null && dto.getHightArr().size()>0) {
	        		String highStr = "";
	        		for(int i=0; i<dto.getHightArr().size(); i++) {
	        			if(!StringUtils.isEmpty(dto.getHightArr().get(i))) {
	        				highStr+=dto.getHightArr().get(i);
	        				highStr+=separator;
	        			}	
	        		}
	        		if(!StringUtils.isEmpty(highStr)) {
	        			ep.setShowHighlights(highStr.substring(0, highStr.length()-separator.length()));
	        		}	
	        	}
	        	//更新时间
	        	ep.setUpdateDate(new Date());
	        	//删除关联表
	        	enterpriseExhibitionService.deleteByEnterpriseId(ep.getId());
	        	enterpriseDao.save(ep);	        		        	
	        	//保存关联信息
	        	saveExhibition(ep.getId(), dto);
			}
			else{
				return MapResult.error("更新企业信息出错");
			}
        }catch (Exception e){
            logger.error("更新企业信息出错");
            e.printStackTrace();
            return MapResult.error("更新企业信息出错");
        }
        return MapResult.ok();
	}

	//在保存之前，判断插入的展台是否已经删除
	private Boolean isExhibitionDeleted(EnterpriseDTO dto) {
		try {
			List<String> echIds = dto.getExhibitionIds();
			if(echIds!=null && echIds.size()>0) {
				for(int i=0; i<echIds.size(); i++) {
					String echId = echIds.get(i);
					if(!StringUtils.isEmpty(echId)) {
						int count = exhibitionBoothService.countById(echId);
						if(count == 0) {
							return true;
						}
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			return true;
		}
		return false;
	}
	
	private void saveExhibition(String enterPriseId, EnterpriseDTO dto) {
		List<String> echIds = dto.getExhibitionIds();
		if(echIds!=null && echIds.size()>0) {
			for(int i=0; i<echIds.size(); i++) {
				String echId = echIds.get(i);
				if(!StringUtils.isEmpty(echId)) {
					EnterpriseExhibition exh = new EnterpriseExhibition();
					exh.setEnterpriseId(enterPriseId);
					exh.setExhibitionId(echId);
					enterpriseExhibitionService.save(exh);
				}
			}
		}
	}

	@Override
	public PageResult<Map<String, Object>> findByPage(int pageNum, int pageSize) {
		PageResult<Map<String, Object>> pageResult = new PageResult<Map<String, Object>>();
		Sort sort = new Sort(Sort.Direction.ASC, new String[]{"rank"});
		PageRequest startPage = PageRequest.of(pageNum - 1, pageSize, sort);
		Page<Enterprise> pageEnterprise = enterpriseDao.findAll(new Specification<Enterprise>() {
            @Override
            public Predicate toPredicate(Root<Enterprise> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                	Join<Enterprise,Dictionary> typeJoin = root.join("typeDic",JoinType.LEFT);
                	predicates.add(criteriaBuilder.notEqual(typeJoin.get("value"), "ENT0"));
                	predicates.add(criteriaBuilder.isNull(typeJoin.get("value")));
                if(predicates.size() > 0) {
                	criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));
                }
                return criteriaQuery.getRestriction();
            }
        }, startPage);
		if(pageEnterprise.getContent() != null && pageEnterprise.getContent().size() > 0){
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for(Enterprise enteriprise: pageEnterprise.getContent()){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", enteriprise.getId());
				map.put("name", enteriprise.getName());
				map.put("image", enteriprise.getLogo() != null ? domain + enteriprise.getLogo(): null);
				mapList.add(map);
			}
			pageResult.setData(mapList);
		}

		int totalPage = (int)Math.ceil((float)pageEnterprise.getTotalElements()/(float) pageSize);
		pageResult.setTotalPage(totalPage);
		pageResult.setCurrentPage(pageNum);
		pageResult.setCode(Constants.SUCESS_CODE);
		return pageResult;
	}

	@Override
	public ExhibitionBooth checkExhibitionBooth(String name) {
		return exhibitionBoothService.findByCode(name);
	}

	@Override
	public List<Enterprise> findByNameLike(String name) {
		return enterpriseDao.findByNameLike(name);
	}
}
