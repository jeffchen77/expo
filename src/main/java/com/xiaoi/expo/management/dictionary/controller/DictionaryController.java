package com.xiaoi.expo.management.dictionary.controller;

import com.xiaoi.expo.management.dictionary.entity.Dictionary;
import com.xiaoi.expo.management.dictionary.service.DictionaryService;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Qu, De-Hui
 * @Description: 数据字典controller
 * @date 2018/3/21
 */
@RequestMapping("/management/dictionary")
@Controller
public class DictionaryController {

    private Logger logger =  LoggerFactory.getLogger(DictionaryController.class);

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * @Description: 获取对应节点下面的字典数据
     * @param parentId 父节点ID
     * @return PageResult<Dictionary>
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    @RequestMapping(value = "findByparentId", method = RequestMethod.GET)
    @ResponseBody
    public List<Dictionary> findByparentId(String parentId){
        return dictionaryService.getByParentId(parentId);
    }

}
