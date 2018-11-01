package com.xiaoi.expo.middleware.web.controller;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.common.utils.FtpUtils;
import com.xiaoi.expo.constants.Constants;
import com.xiaoi.expo.middleware.web.entity.HotMessageView;
import com.xiaoi.expo.middleware.web.service.HotMessageViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bright.liang
 * @Description: ${todo}
 * @date 2018/3/2710:26
 */
@Controller
@RequestMapping(value = "/interface/hotmessage/")
public class HotMessageController {


    private Logger logger =  LoggerFactory.getLogger(HotMessageController.class);

    @Autowired
    private HotMessageViewService hotMessageViewService;

    @Autowired
    private FtpUtils ftpUtils;

    @RequestMapping(value = "getHotNews", method = RequestMethod.GET)
    @ResponseBody
    public PageResult findByPage(Integer pageSize){
        PageResult<HotMessageView> guestTalkPageResult = new PageResult<HotMessageView>();
        if(pageSize == null || pageSize == 0) {
            pageSize = 10;
        }
        try{
            List<HotMessageView> hotMessageViewList = hotMessageViewService.findHotNews(pageSize);

            if(hotMessageViewList != null){
                for(HotMessageView hotMessageView: hotMessageViewList){
                    hotMessageView.setImages(ftpUtils.getDomain());
                    if(!StringUtils.isEmpty(hotMessageView.getGusetPhoto())){
                        hotMessageView.setGusetPhoto(ftpUtils.getDomain() + hotMessageView.getGusetPhoto());
                    }
                }
            }

            guestTalkPageResult.setData(hotMessageViewList);
            guestTalkPageResult.setCode(Constants.SUCESS_CODE);

        }catch (Exception e){
            logger.error("大屏查询首页热点资讯出错");
            e.printStackTrace();
            guestTalkPageResult.setCode(Constants.ERROR_CODE_SYSTEM);
            guestTalkPageResult.setMsg("查询热点资讯出错");
            return guestTalkPageResult;
        }
        return guestTalkPageResult;
    }

    @RequestMapping(value = "getHotMessages", method = RequestMethod.GET)
    @ResponseBody
    public PageResult findHotMessagesByPage(Integer pageNum, Integer pageSize){
        PageResult<HotMessageView> guestTalkPageResult = new PageResult<HotMessageView>();
        if(pageSize == null || pageSize == 0) {
            pageSize = 10;
        }

        if(pageNum == null || pageNum == 0 || pageNum < 0){
            pageNum = 0;
        }
        try{
            return  hotMessageViewService.findHotMessageslist(pageNum, pageSize);
        }catch (Exception e){
            logger.error("大屏查询热点资讯列表出错");
            e.printStackTrace();
            guestTalkPageResult.setCode(Constants.ERROR_CODE_SYSTEM);
            guestTalkPageResult.setMsg("查询热点资讯列表出错");
            return guestTalkPageResult;
        }
    }



    @RequestMapping(value = "getMessage", method = RequestMethod.GET)
    @ResponseBody
    public MapResult getMessage(Integer pageNumber){
        List<HotMessageView> hotMessageViewList = null;
        try{
            hotMessageViewList = hotMessageViewService.findHotMessages(pageNumber, 1);
        }catch (Exception e){
            logger.error("查询热点资讯详情出错");
            e.printStackTrace();
            return MapResult.error("查询热点资讯详情出错");
        }

        if(hotMessageViewList == null || hotMessageViewList.size() == 0){
            return MapResult.error("没有下一条热点资讯");
        }

        MapResult mapResult = MapResult.ok();
        mapResult.put("data", hotMessageViewList.get(0));
        return mapResult;
    }

    @RequestMapping(value = "getImages", method = RequestMethod.GET)
    @ResponseBody
    public PageResult getImages(Integer pageSize){
        PageResult<Map<String, String>> guestTalkPageResult = new PageResult<Map<String, String>>();
        List<HotMessageView> hotMessageViewList = null;
        try{
            hotMessageViewList = hotMessageViewService.findHotMessages(0, pageSize);
        }catch (Exception e){
            logger.error("查询热点资讯详情出错");
            e.printStackTrace();
            guestTalkPageResult.setCode(Constants.ERROR_CODE_SYSTEM);
            guestTalkPageResult.setMsg("查询热点资讯详情出错");
            return guestTalkPageResult;
        }

        List<String> imageList = new ArrayList<String>();
        if(hotMessageViewList != null){
            for(HotMessageView hotMessageView : hotMessageViewList){
                hotMessageView.setImages(ftpUtils.getDomain());
                imageList.addAll(hotMessageView.getImages());
                if(imageList.size()>= 10){
                    break;
                }
            }
        }

        List<Map<String, String>> imgMapList = new ArrayList<Map<String, String>>();
        for(int i=0; i<imageList.size(); i++){
            Map<String, String> map = new HashMap<String, String>();
            map.put("img", imageList.get(i));
            imgMapList.add(map);
            if(i>=10){
                break;
            }
        }
        guestTalkPageResult.setData(imgMapList);
        return guestTalkPageResult;
    }

}
