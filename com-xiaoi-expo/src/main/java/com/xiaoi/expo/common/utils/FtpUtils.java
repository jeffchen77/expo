package com.xiaoi.expo.common.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;

/**
 * @author bright.liang
 * @Description: ftp上传文件公共类
 * @date 2018/3/2010:41
 */
@Component
public class FtpUtils {
    //服务器主机
    @Value("${ftp.host}")
    private  String host;

    //服务器端口
    @Value("${ftp.port}")
    private  String port;

    //用户名
    @Value("${ftp.username}")
    private  String userName;


    //密码
    @Value("${ftp.passwd}")
    private  String password;


    //服务器上传相对路径ftp.directory
    @Value("${ftp.directory}")
    private  String directory;


    //访问地址
    @Value("${ftp.domain}")
    private  String domain;

    // 内网访问地址
    private  String innerDomain;

    //上传文件
    private  FtpUtils instance = null;


    /**
     * 功能描述：ftp文件上传
     *
     * @param originFileName 上传服务器文件名
     * @param uploadFile     上传文件
     * @return boolean 成功标志 true:成功 false:失败
     * @method uploadFile
     */
    public boolean uploadFile(String originFileName, String uploadFile) {
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();
        FileInputStream fis = null;
        try {
            ftpClient.connect(host);
            ftpClient.login(userName, password);
            File srcFile = new File(uploadFile);
            fis = new FileInputStream(srcFile);
            //设置上传目录
            ftpClient.changeWorkingDirectory(directory);
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("UTF-8");
            //设置文件类型（二进制） 
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.storeFile(originFileName, fis);
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            try {
                fis.close();
                ftpClient.disconnect();
            } catch (IOException e) {
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
        return flag;
    }

    /**
     * 功能描述：ftp文件下载
     *
     * @param fileName  要下载的文件名
     * @param localPath 下载后保存到本地的路径
     * @return boolean 成功标志 true:成功 false:失败
     */
    public  boolean downFile(String fileName, String localPath) {
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();
        try {
            //连接ftp服务器
            ftpClient.connect(host);
            ftpClient.login(userName, password);
            //转移到FTP服务器目录
            ftpClient.changeWorkingDirectory(directory);
            FTPFile[] fs = ftpClient.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(localPath + "/" + ff.getName());
                    OutputStream is = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(ff.getName(), is);
                    is.close();
                }
            }
            ftpClient.logout();
            flag = true;
        } catch (IOException e) {

        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
            } catch (IOException e) {

            }
        }
        return flag;
    }

    /**
     * 功能描述：ftp文件上传
     *
     * @param filepath        上传文件路径及文件名称
     * @param fileInputStream 上传文件文件流
     * @return boolean 成功标志 true:成功 false:失败
     * @method uploadFileByStream
     */
    public boolean  uploadFileByStream(String filepath, String fileName, InputStream fileInputStream) {
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.setDefaultPort(Integer.parseInt(port));
            ftpClient.connect(host);
            boolean loginflag = ftpClient.login(userName, password);

            //设置上传目录
            boolean dirflag = ftpClient.changeWorkingDirectory(directory);

            changePath(filepath, ftpClient);
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("UTF-8");
            //设置文件类型（二进制） 
//            ftpClient.enterLocalPassiveMode();
            ftpClient.enterLocalActiveMode();
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            boolean succFlag = ftpClient.storeFile(fileName, fileInputStream);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
        return flag;
    }

    public String getHost() {
        return "http://" + host + "/";
    }

    public String getDirectory() {
        return directory + "/";
    }

    public String getDomain() {
        return domain;
    }

    public String getInnerDomain(){
        return  innerDomain;
    }

    public void changePath(String path, FTPClient ftp) {
        String[] arraypath = path.split("/");
        int i = 0;
        while (i < arraypath.length) {
            try {
                ftp.makeDirectory(arraypath[i]);
                ftp.changeWorkingDirectory(arraypath[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;
        }
    }
}
