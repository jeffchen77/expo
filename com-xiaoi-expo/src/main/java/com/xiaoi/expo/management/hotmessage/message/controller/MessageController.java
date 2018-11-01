package com.xiaoi.expo.management.hotmessage.message.controller;

import com.xiaoi.expo.common.interceptor.Auth;
import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.common.utils.FtpUtils;
import com.xiaoi.expo.management.hotmessage.message.entity.Message;
import com.xiaoi.expo.management.hotmessage.message.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author guangjian.zeng
 * @Description: 图文短信controller
 * @date 2018/3/1911:47
 */
@RequestMapping("/management/message")
@Controller
public class MessageController {

    private Logger logger =  LoggerFactory.getLogger(MessageController.class);

    private static final String MESSAGE = "hotmessage/message/message";
    private static final String MESSAGE_ADD = "hotmessage/message/add";
    private static final String MESSAGE_LOOK = "hotmessage/message/look";
    private static final String MESSAGE_EDIT = "hotmessage/message/edit";

    @Autowired
    MessageService messageService;
    @Autowired
    private FtpUtils ftpUtils;

    /**
     * @Description: 跳转到图文短讯
     * @return String
     * @author guangjian.zeng
     * @date 2018/3/19 17:04
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @Auth
    public String message(HttpServletRequest request){
        return MESSAGE;
    }

    /**
     * @Description: 跳转到图文短讯新增页面
     * @return String
     * @author guangjian.zeng
     * @date 2018/3/19 17:04
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    @Auth
    public String messageAdd(HttpServletRequest request, String id, String type, Model model){
        Message message = null;
        String imgDomain = null;
        if("look".equals(type)){
            imgDomain = ftpUtils.getDomain();
            message = messageService.findOne(id);
            model.addAttribute("message",message);
            //model.addAttribute("type", type);
            model.addAttribute("pictures", message.getPicture() != null ? message.getPicture().split(","): null);
            model.addAttribute("domain",imgDomain );
            return MESSAGE_LOOK;
        }else if("edit".equals(type)){
            imgDomain = ftpUtils.getDomain();
            message = messageService.findOne(id);
            model.addAttribute("message",message);
            model.addAttribute("pictures", message.getPicture() != null ? message.getPicture().split(","): null);
            model.addAttribute("domain",imgDomain );
            //model.addAttribute("type", type);
            return MESSAGE_EDIT;
        }
        return MESSAGE_ADD;
    }

    /**
     * @Description: 分页查询大佬说
     * @param page 页码
     * @param  limit 每页显示条数
     * @param searchName 搜索关键词
     * @return PageResult<Message>
     * @author guangjian.zeng
     * @date 2018/3/19 11:49
     */
    @RequestMapping(value = "findByPagging", method = RequestMethod.GET)
    @ResponseBody
    public PageResult<Message> findByPagging(Integer page, Integer limit, String searchName){
        PageResult<Message> pageResult = messageService.getGuestTalks(page, limit, searchName);
        if(pageResult.getData() != null && pageResult.getData().size() > 0){
            List<Message> messageList = pageResult.getData();
           // String imgDomain = ftpUtils.getDomain();
            for(Message message: messageList){
                transferUrl(message);
            }
        }
        return pageResult;
    }

    /**
     * @Description: 修改图文短讯
     * @param message 图文短讯
     * @return MapResult
     * @author guangjian.zeng
     * @date 2018/3/20 14:04
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public MapResult save(Message message){
        Message oldMessage = null;
        if(!StringUtils.isEmpty(message.getId())) {
            oldMessage = messageService.findOne(message.getId());
            oldMessage.setTitle(message.getTitle());
            oldMessage.setContent(message.getContent());
            oldMessage.setPicture(message.getPicture());
            oldMessage.setKnowledge(message.getKnowledge() != null ? message.getKnowledge().trim(): null);
            oldMessage.setUpdateTime(new Date());
        }else {
            oldMessage = message;
            //oldMessage.setCreateTime(new Date());
            oldMessage.setStatus("0");//可用
            //oldMessage.setIsShowScreen("0");//可展示
            oldMessage.setIsShowIndex("0");
        }
        return messageService.save(oldMessage);
    }

    /**
     * @Description: 删除
     * @param id 用户id
     * @return MapResult
     * @author guangjian.zeng
     * @date 2018/3/21 10:44
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public MapResult delete(String id){
        Message message = messageService.findOne(id);
        if(message == null){
            return MapResult.error("用户不存在或已被删除");
        }
        message.setUpdateTime(new Date());
        message.setStatus("1");//不可用
        messageService.save(message);
        return MapResult.ok("删除图文短讯成功");
    }

    /**
     * @Description: 更新message状态(是否大屏显示)
     * @param messageId message id
     * @param show 状态
     * @return MapResult
     * @author guangjian.zeng
     * @date 2018/3/23 10:04
     */
    @RequestMapping(value = "updateShow", method = RequestMethod.POST)
    @ResponseBody
    public MapResult updateShow(String messageId, String show){
        Message message  = messageService.findOne(messageId);
        if(message == null){
            return MapResult.error("短讯不存在或已被删除");
        }
        message.setIsShowScreen(show);
        message.setUpdateTime(new Date());
        messageService.save(message);
        return MapResult.ok();
    }

    /**
     * @Description: 更新message状态（是否首页显示）
     * @param messageId message id
     * @param show 状态
     * @return MapResult
     * @author guangjian.zeng
     * @date 2018/3/23 10:04
     */
    @RequestMapping(value = "updateIndex", method = RequestMethod.POST)
    @ResponseBody
    public MapResult updateIndex(String messageId, String show){
        Message message  = messageService.findOne(messageId);
        if(message == null){
            return MapResult.error("短讯不存在或已被删除");
        }
        message.setIsShowIndex(show);
        message.setUpdateTime(new Date());
        messageService.save(message);
        return MapResult.ok();
    }

    /**
     * @Description: 添加ftp路径
     * @param message
     * @return MapResult
     * @author guangjian.zeng
     * @date 2018/3/23 10:04
     */
    private void transferUrl(Message message){
        String imgDomain = ftpUtils.getDomain();
        if(!StringUtils.isEmpty(message.getPicture())){
            String[] imgs = message.getPicture().split(",");
            StringBuffer imgUrls = new StringBuffer();
            for(int j=0; j<imgs.length; j++){
                imgUrls.append(imgDomain);
                imgUrls.append(imgs[j]);
                if(j != imgs.length - 1){
                    imgUrls.append(",");
                }
            }
            message.setPicture(imgUrls.toString());
        }
    }

}
