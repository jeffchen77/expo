package com.xiaoi.expo.management.ftp;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.utils.DateFormatUtils;
import com.xiaoi.expo.common.utils.FtpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

/**
 * @author bright.liang
 * @Description: ${todo}
 * @date 2018/3/1915:49
 */
@Controller
@RequestMapping(value = "/management/ftp")
public class FtpController {


    private Logger logger =  LoggerFactory.getLogger(FtpController.class);

    @Autowired
    private FtpUtils ftpUtils;

//    private static final String FTP_
    /**
     * 上传图片
     *
     * @param request  包含文件流
     * @param response
     * @return MapResult 上传成功时返回图片访问地址
     * @method uploadFile
     */
    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public MapResult uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> fileNames = multipartRequest.getFileNames();
        MultipartFile multipartFile = multipartRequest.getFile(fileNames.next());
        String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        String suffix1 = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);

        String randomFileName = UUID.randomUUID().toString();
        String dateDirectory = DateFormatUtils.dateToString(new Date(), "yyyy-MM-dd");
        String filePath = dateDirectory + "/" + randomFileName + suffix;
        Boolean flag =ftpUtils.uploadFileByStream(dateDirectory + "/", randomFileName + suffix, multipartFile.getInputStream());

        if(flag){
            MapResult mapResult = MapResult.ok();
            mapResult.put("url", ftpUtils.getDomain() + filePath);
            mapResult.put("relativeUrl", filePath);
            return mapResult;
        }
        return MapResult.error("上传文件失败");
    }
}
