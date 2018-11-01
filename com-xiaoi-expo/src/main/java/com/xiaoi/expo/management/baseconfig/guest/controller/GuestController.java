package com.xiaoi.expo.management.baseconfig.guest.controller;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.response.PageResult;
import com.xiaoi.expo.common.utils.FtpUtils;
import com.xiaoi.expo.management.baseconfig.enterprise.entity.Enterprise;
import com.xiaoi.expo.management.baseconfig.enterprise.service.EnterpriseService;
import com.xiaoi.expo.management.baseconfig.guest.entity.Guest;
import com.xiaoi.expo.management.baseconfig.guest.service.GuestService;

/**
 * @author chen fei
 * @Description: 嘉宾库controller层
 * @date 2018/3/20 09:20
 */
@Controller
@RequestMapping(value = "/management/config/guest")
public class GuestController {

    private Logger logger =  LoggerFactory.getLogger(GuestController.class);
    private static final String LIST = "config/guest/list"; // 列表页面;
    private static final String ADD = "config/guest/add"; // 新增页面
    private static final String VIEW = "config/guest/view"; // 查看页面
    private static final String EDIT = "config/guest/add"; // 编辑页面
    private static final String AUTH = "config/guest/auth"; // 嘉宾认证页面

    @Autowired
    private GuestService guestService;
    
    @Autowired
    private EnterpriseService entService;
    
    @Autowired
    private FtpUtils ftpUtils;
    /**
     * @Description: 加载嘉宾列表页面
     * @return String
     * @author chen fei
     * @date 2018/3/20 09:24
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String guestList(Model model){
    	model.addAttribute("ftpDomain", ftpUtils.getDomain());
        return LIST;
    }
    
    /**
     * @Description: 嘉宾认证列表页面
     * @return String
     * @author Qu, De-Hui
     * @date 2018/4/8 09:24
     */
    @RequestMapping(value = "auth", method = RequestMethod.GET)
    public String authList(){
        return AUTH;
    }
    
    /**
     * @Description: 分页查询嘉宾认证信息
     * @param page 页码
     * @param  limit 每页显示条数
     * @param searchName 搜索关键词
     * @return PageResult<Guest>
     * @author Qu, De-Hui
     * @date 2018/4/8 09:24
     */
    @RequestMapping(value = "authFindByPagging", method = RequestMethod.GET)
    @ResponseBody
    public PageResult<Guest> authFindByPagging(Integer page, Integer limit, String searchName){
        if(page == null) {
            page = 0;
            limit = 10;
        }
        return guestService.authFindByPagging(page, limit, searchName);
    }
    
    /**
     * @Description: 跳转嘉宾新增页面
     * @return String
     * @author chen fei
     * @date 2018/3/20 09:24
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model){
    	Guest guest =new Guest();
    	guest.setEnterprise(new Enterprise());
    	model.addAttribute("guest",guest);    	
    	model.addAttribute("list",entService.findAll());
        return ADD;
    }
    
    /**
     * @Description: 跳转嘉宾查看页面
     * @param guestId 嘉宾id
     * @return String
     * @author chen fei
     * @date 2018/3/20 09:24
     */
    @RequestMapping(value = "view", method = RequestMethod.GET)
    public String view(String guestId, Model model){
    	logger.debug("guestId:"+guestId);
    	Guest guest =guestService.findOne(guestId);
    	
    	if(!StringUtils.isEmpty(guest.getPhotoAddr())) {
    		guest.setPhotoAddr(ftpUtils.getDomain() +guest.getPhotoAddr());
        	}
    	model.addAttribute("guest",guest); 
        return VIEW;
    }
    
    /**
     * @Description: 跳转嘉宾编辑页面
     * @param guestId 嘉宾id
     * @return String
     * @author chen fei
     * @date 2018/3/20 09:24
     */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit(String guestId, Model model){
    	logger.debug("guestId:"+guestId);
    	Guest guest =guestService.findOne(guestId);
    	
    	if(!StringUtils.isEmpty(guest.getPhotoAddr())) {
        	model.addAttribute("imgUrl",ftpUtils.getDomain()+guest.getPhotoAddr());
        	}
    	model.addAttribute("guest",guest);    	
    	model.addAttribute("list",entService.findAll());
        return EDIT;
    }
    
    /**
     * @Description: 分页查询嘉宾信息
     * @param page 页码
     * @param  limit 每页显示条数
     * @param searchName 搜索关键词
     * @return PageResult<Guest>
     * @author chen fei
     * @date 2018/3/20 09:24
     */
    @RequestMapping(value = "findByPagging", method = RequestMethod.GET)
    @ResponseBody
    public PageResult<Guest> findByPagging(Integer page, Integer limit, String searchName){
        if(page == null) {
            page = 0;
            limit = 10;
        }
        return guestService.getGuests(page, limit, searchName);
    }


    /**
     * @Description: 保存嘉宾信息
     * @param guest 嘉宾信息
     * @return MapResult
     * @author chen fei
     * @date 2018/3/20 09:24
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public MapResult save(Guest guest){
        Guest oldGuest = null;
        if(!StringUtils.isEmpty(guest.getId())){
            oldGuest = guestService.findOne(guest.getId());
            if(oldGuest == null){
                return MapResult.error("嘉宾不存在或已被删除");
            }
            oldGuest.setId(guest.getId());
            oldGuest.setName(guest.getName());
            oldGuest.setSex(guest.getSex());
            oldGuest.setPosition(guest.getPosition());
            oldGuest.setOrderNum(guest.getOrderNum());
            oldGuest.setEnterprise(guest.getEnterprise());
            oldGuest.setPhotoAddr(guest.getPhotoAddr());
            oldGuest.setUpdateTime(new Date());            
        }else{
        	oldGuest = guest;
            
        	oldGuest.setStatus("0");
        	oldGuest.setSignStatus("1");
        	oldGuest.setCreateTime(new Date());
        	oldGuest.setUpdateTime(new Date());
        	
        }
        return guestService.save(oldGuest);
    }

  

    /**
     * @Description: 删除嘉宾
     * @param guestId 嘉宾id
     * @return MapResult
     * @author chen fei
     * @date 2018/3/20 09:24
     */
    @RequestMapping(value = "deleteGuest", method = RequestMethod.POST)
    @ResponseBody
    public MapResult deleteGuest(String guestId){
        Guest guest = guestService.findOne(guestId);
        if(guest == null){
            return MapResult.error("嘉宾不存在或已被删除");
        }
       return guestService.delete(guestId);
    }
    
    /**
	 * @Description: 返回所有的嘉宾信息，不分页
	 * @return List<Guest>
	 * @author chen,fei
	 * @date 2018/3/20
	 */
	@RequestMapping(value = "findAll", method = RequestMethod.GET)
	@ResponseBody
	public List<Guest> findAll() {
		return guestService.findAll();
	}
	
	/**
     * @Description: 上传头像
     * @param file 嘉宾头像
     * @return MapResult
     * @author chen fei
     * @date 2018/3/20 09:24
     */
    @RequestMapping(value = "uploadImg", method = RequestMethod.POST)
    @ResponseBody
    public MapResult uploadImg(@RequestParam("file") MultipartFile file,HttpServletRequest request){
    	
    	String str ="";
     try {	
    	String oldName = file.getOriginalFilename();
    	String path = request.getServletContext().getRealPath("/upload/");
    	String fileName = changeName(oldName);
    	String rappendix = "/upload/" + fileName;
    	fileName = path + "/" + fileName;
    	File file1 = new File(fileName);
    	if(!file1.getParentFile().exists()) {
    		file1.getParentFile().mkdir();
    	}
    	file.transferTo(file1);
    	str = "{\"code\": 0,\"src\":\"" + rappendix + "\"}";
     }catch(Exception ex) {    	
    	 ex.printStackTrace();
    	 return MapResult.error("upload error");
     }
     
        return MapResult.ok(str);
    }
    
    public static String changeName(String oldName){
    				Random r = new Random();
    				Date d = new Date();
    				String newName = oldName.substring(oldName.indexOf('.'));
    				newName = r.nextInt(99999999) + d.getTime() + newName;
    				return newName;
			}
   
}
