package com.zxm.utils;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

@Component
public class FtpUtil {
    private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);
    /**
     * ftp服务器ip地址
     */
    private static String host;
    @Value("${ftp.host}")
    public void setHost(String val){
        FtpUtil.host = val;
    }
    /**
     * 端口
     */
    private static int port;
    @Value("${ftp.port}")
    public void setPort(int val){
        FtpUtil.port = val;
    }
    /**
     * 用户名
     */
    private static String userName;
    @Value("${ftp.userName}")
    public void setUserName(String val){
        FtpUtil.userName = val;
    }
    /**
     * 密码
     */
    private static String password;
    @Value("${ftp.password}")
    public void setPassword(String val){
        FtpUtil.password = val;
    }
    /**
     * 超时时间
     */
    private static int timeout;
    @Value("${ftp.timeout}")
    public void setTimeout(int val){
        FtpUtil.timeout = val;
    }
    /**
     * 存放图片的根目录
     */
    private static String rootPath;
    @Value("${ftp.rootPath}")
    public void setRootPath(String val){
        FtpUtil.rootPath = val;
    }
    /**
     * 存放图片的路径
     */
    private static String imgUrl;
    @Value("${ftp.img.url}")
    public void setImgUrl(String val){
        FtpUtil.imgUrl = val;
    }
    /**
     * 获取连接
     */
    private static ChannelSftp getChannel() throws Exception{
        JSch jsch = new JSch();
        //->ssh root@host:port
        Session sshSession = jsch.getSession(userName,host,port);
        //密码
        sshSession.setPassword(password);
        sshSession.setTimeout(timeout);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        sshSession.setConfig(sshConfig);
        sshSession.connect();
        Channel channel = sshSession.openChannel("sftp");
        channel.connect();
        return (ChannelSftp) channel;
    }
    /**
     * ftp上传图片
     * @param inputStream 图片io流
     * @param imagePath 路径，不存在就创建目录
     * @param imagesName 图片名称
     * @return urlStr 图片的存放路径
     */
    public static String putImages(InputStream inputStream, String imagePath, String imagesName){
        try {
            ChannelSftp sftp = getChannel();
            String path = rootPath + imagePath + "/";
            createDir(path,sftp);
            //上传文件
            sftp.put(inputStream, path + imagesName);
            logger.info("上传成功！");
            sftp.quit();
            sftp.exit();
            //处理返回的路径
            String resultFile;
            resultFile = imgUrl + imagePath + "/" + imagesName;
            return resultFile;
        } catch (Exception e) {
            logger.error("上传失败：" + e.getMessage());
        }
        return "";
    }
    /**
     * 创建目录
     */
    private static void createDir(String path,ChannelSftp sftp) throws SftpException {
        String[] folders = path.split("/");
        sftp.cd("/");
        for ( String folder : folders ) {
            if ( folder.length() > 0 ) {
                try {
                    sftp.cd( folder );
                }catch ( SftpException e ) {
                    sftp.mkdir( folder );
                    sftp.cd( folder );
                }
            }
        }
    }
    /**
     * 删除图片
     */
    public static void delImages(String imagesPath){
        try {
            ChannelSftp sftp = getChannel();
            String path = rootPath + imagesPath;
            sftp.rm(path);
            sftp.quit();
            sftp.exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 删除文件夹多余的图片
     * @param folderPath 文件夹路径
     * @param usingImgNames 非删除的图片名称集合
     * @return void
     * @author zhixiumao
     * @creed: Talk is cheap,show me the code
     * @date 2022/1/1 19:17
     */
    public static void clearOldImg(String folderPath, Set<String> usingImgNames){
        try {
            ChannelSftp sftp = getChannel();
            String path=rootPath+folderPath;
            createDir(path,sftp);
            sftp.cd(path);
            Vector ls =sftp.ls("./");
            for (Object o: ls) {
                String[] str=o.toString().split("\\s+");
                String name=str[str.length-1];
                if(usingImgNames.contains(name)||name.equals(".")||name.equals(".."))continue;
                sftp.rm(name);
            }
            sftp.quit();
            sftp.exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 检查当前用户图片内存是否足够，限制每人
     * @param folderPath
     * @return boolean
     * @author zhixiumao
     * @creed: Talk is cheap,show me the code
     * @date 2022/1/6 14:42
     */
    public static boolean checkMemory(String folderPath){
        long availM=50*1024*1024L;
        long total=0;
        try {
            ChannelSftp sftp = getChannel();
            String path=rootPath+folderPath;
            createDir(path,sftp);
            sftp.cd(path);
            Vector ls =sftp.ls("./");
            for (Object o: ls) {
                String[] str=o.toString().split("\\s+");
                String m=str[4];
                total+=Long.valueOf(m);
            }
            sftp.quit();
            sftp.exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(total>availM)return false;
        return true;
    }
}