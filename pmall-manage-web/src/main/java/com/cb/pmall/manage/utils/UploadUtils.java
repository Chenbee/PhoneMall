package com.cb.pmall.manage.utils;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author 陈彬
 * date 2020/4/2 11:39 AM
 */
public class UploadUtils {
    public static String uploadFile(MultipartFile multipartFile){
        // 配置fdfs的全局链接地址
        // 获得配置文件的路径
        String tracker = UploadUtils.class.getResource("/tracker.conf").getPath();

        try {
            ClientGlobal.init(tracker);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TrackerClient trackerClient = new TrackerClient();

        // 获得一个trackerServer的实例
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerClient.getTrackerServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 通过tracker获得一个Storage链接客户端
        StorageClient storageClient = new StorageClient(trackerServer,null);
        String[] uploadInfos = null;
        try {
            // 获取二进制文件
            byte[] imgBytes = multipartFile.getBytes();
            // 获取文件名字并分割获取后缀
            String originalFilename = multipartFile.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
            uploadInfos = storageClient.upload_file(imgBytes, suffix, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = "http://172.16.189.136";
        for (String uploadInfo : uploadInfos) {
            url += "/"+uploadInfo;

            //url = url + uploadInfo;
        }
        return url;
    }
}
