package com.xiaoi.expo.management.hotmessage.guesttalk.controller;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.common.utils.FtpUtils;
import com.xiaoi.expo.management.baseconfig.guest.entity.Guest;
import com.xiaoi.expo.management.baseconfig.guest.service.GuestService;
import com.xiaoi.expo.management.hotmessage.guesttalk.entity.GuestTalk;
import com.xiaoi.expo.management.hotmessage.guesttalk.service.GuestTalkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author bright.liang
 * @Description: 大佬说controller
 * @date 2018/3/199:47
 */
@RequestMapping("/management/guesttalk")
@Controller
public class GuestTalkController {

    private Logger logger =  LoggerFactory.getLogger(GuestTalkController.class);

    private static final String LIST = "hotmessage/guesttalk/list"; // 大佬说列表页面

    private static final String ADD = "hotmessage/guesttalk/add"; // 大佬说新增页面

    private static final String VIEW = "hotmessage/guesttalk/view"; // 大佬说查看页面

    private static final String EDIT = "hotmessage/guesttalk/edit"; // 大佬说修改页面

    @Autowired
    private GuestTalkService guestTalkService;

    @Autowired
    private GuestService guestService;

    @Autowired
    private FtpUtils ftpUtils;

    /**
     * @Description: 分页查询大佬说
     * @param page 页码
     * @param  limit 每页显示条数
     * @param searchName 搜索关键词
     * @return PageResult<GuestTalk>
     * @author bright.liang
     * @date 2018/3/13 18:04
     */
    @RequestMapping(value = "findByPagging", method = RequestMethod.GET)
    @ResponseBody
    public PageResult<GuestTalk> findByPagging(Integer page, Integer limit, String searchName){
        PageResult<GuestTalk> pageResult = guestTalkService.getGuestTalks(page, limit, searchName);
        if(pageResult.getData() != null && pageResult.getData().size() > 0){
            List<GuestTalk> guestTalkList = pageResult.getData();
            String imgDomain = ftpUtils.getDomain();
            for(GuestTalk guestTalk: guestTalkList){
                transferUrl(guestTalk);
            }
        }
        return pageResult;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String guestTalkIndex(){
        return LIST;
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(){
        return ADD;
    }


    @RequestMapping(value = "view", method = RequestMethod.GET)
    public String view(String id,  ModelMap model){
        if(!StringUtils.isEmpty(id)){
            GuestTalk guestTalk = guestTalkService.findOne(id);
            String imgDomain = ftpUtils.getDomain();
            if(guestTalk != null){
//                transferUrl(guestTalk);
                model.put("guestTalk", guestTalk);
                model.put("pictures", guestTalk.getPicture() != null ? guestTalk.getPicture().split(","): null);
                model.put("domain",imgDomain );
            }
        }
        return VIEW;
    }

    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit(String id,  ModelMap model){
        if(!StringUtils.isEmpty(id)){
            GuestTalk guestTalk = guestTalkService.findOne(id);
            String imgDomain = ftpUtils.getDomain();
            if(guestTalk != null){
//                transferUrl(guestTalk);
                model.put("guestTalk", guestTalk);
                model.put("pictures", guestTalk.getPicture() != null ? guestTalk.getPicture().split(","): null);
                model.put("domain",imgDomain );
            }
        }
        return EDIT;
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    @ResponseBody
    public MapResult delete(String id){
        if(StringUtils.isEmpty(id)){
            return MapResult.error("信息不存在或已被删除");
        }

        GuestTalk guestTalk = guestTalkService.findOne(id);

        if(guestTalk == null){
            return MapResult.error("信息不存在或已被删除");
        }

        guestTalkService.delete(id);
        return MapResult.ok();
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public MapResult save(GuestTalk guestTalk){

        Guest guest = guestService.findOne(guestTalk.getGuestId());
        if(guest == null){
            return MapResult.error("大咖不存在或已被删除");
        }
        try{
            guestTalk.setGuest(guest);
            guestTalkService.save(guestTalk);
            return MapResult.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("保存大佬说出错");
            return MapResult.error("操作出错，请稍后再试");
        }
    }

    private void transferUrl(GuestTalk guestTalk){
        String imgDomain = ftpUtils.getDomain();
        if(!StringUtils.isEmpty(guestTalk.getPicture())){
            String[] imgs = guestTalk.getPicture().split(",");
            StringBuffer imgUrls = new StringBuffer();
            for(int j=0; j<imgs.length; j++){
                imgUrls.append(imgDomain);
                imgUrls.append(imgs[j]);
                if(j != imgs.length - 1){
                    imgUrls.append(",");
                }
            }
            guestTalk.setPicture(imgUrls.toString());
        }
    }

    @RequestMapping(value = "updateStatus", method = RequestMethod.POST)
    @ResponseBody
    public MapResult updateStatus(String id, String status){
        GuestTalk guestTalk = null;
        try{
            guestTalk = guestTalkService.findOne(id);
            if(guestTalk == null){
                return MapResult.error("您选择的大佬说不存在或已被删除");
            }
            guestTalk.setIsShowScreen(status);
            guestTalkService.save(guestTalk);
            return MapResult.ok();
        }catch (Exception e){
            logger.error("更新大佬说状态出错");
            e.printStackTrace();
            return MapResult.error("更新大佬说状态出错");
        }
    }
}
