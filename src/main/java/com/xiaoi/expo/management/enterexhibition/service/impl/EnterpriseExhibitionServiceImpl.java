package com.xiaoi.expo.management.enterexhibition.service.impl;
import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.common.utils.EncryptptUtils;
import com.xiaoi.expo.management.baseconfig.enterprise.dao.EnterpriseDao;
import com.xiaoi.expo.management.baseconfig.enterprise.dto.EnterpriseDTO;
import com.xiaoi.expo.management.baseconfig.enterprise.entity.Enterprise;
import com.xiaoi.expo.management.baseconfig.enterprise.service.EnterpriseService;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.dao.ExhibitionBoothDao;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.entity.ExhibitionBooth;
import com.xiaoi.expo.management.baseconfig.exhibitionbooth.service.ExhibitionBoothService;
import com.xiaoi.expo.management.baseconfig.guest.entity.Guest;
import com.xiaoi.expo.management.dictionary.service.DictionaryService;
import com.xiaoi.expo.management.enterexhibition.dao.EnterpriseExhibitionDao;
import com.xiaoi.expo.management.enterexhibition.entity.EnterpriseExhibition;
import com.xiaoi.expo.management.enterexhibition.service.EnterpriseExhibitionService;
import com.xiaoi.expo.management.user.dao.UserDao;
import com.xiaoi.expo.management.user.entity.User;
import com.xiaoi.expo.management.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Qu，De-Hui
 * @Description: 展台库管理serivce
 * @date 2018/3/20
 */
@Service
public class EnterpriseExhibitionServiceImpl implements EnterpriseExhibitionService{
    private Logger logger = LoggerFactory.getLogger(EnterpriseExhibitionServiceImpl.class);

    @Autowired
    private EnterpriseExhibitionDao enterpriseExhibitionDao;

	@Override
	public Boolean isExitByExhId(String exhId) {
		EnterpriseExhibition entExh = enterpriseExhibitionDao.findByExhibitionId(exhId);
		if(entExh==null) {
			return false;
		}else {
			return true;
		}
	}

	@Override
	public void deleteByExhId(String exhId) {
		enterpriseExhibitionDao.deleteByExhibitionId(exhId);
	}

	@Override
	public void save(EnterpriseExhibition enterpriseExhibition) {
		enterpriseExhibitionDao.save(enterpriseExhibition);
	}

	@Override
	public void deleteByEnterpriseId(String enterpriseId) {
		enterpriseExhibitionDao.deleteByEnterpriseId(enterpriseId);
	}

	@Override
	public List<EnterpriseExhibition> findAllByEnterpriseId(String enterpriseId) {
		return enterpriseExhibitionDao.findAllByEnterpriseId(enterpriseId);
	}

	@Override
	public EnterpriseExhibition findByExhibitionId(String exhId) {
		return enterpriseExhibitionDao.findByExhibitionId(exhId);
	}

}
