package com.qiandi.utils;

import com.obs.services.ObsClient;
import com.obs.services.model.ProgressListener;
import com.obs.services.model.ProgressStatus;
import com.obs.services.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * 华为云工具类
 *
 * @author fxc
 * @create 2020-07-03 9:14
 */
public class HuaWeiUtils {

    private static String endPoint = "https://obs.cn-north-4.myhuaweicloud.com";
    private static String ak = "ak";
    private static String sk = "sk";
    private static String bucket = "health01";


    /**
     * 上传文件
     *
     * @param filePath 文件地址
     * @param fileName 文件名
     */
    public static void uploadHuaWei(String filePath, String fileName) {

        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        PutObjectRequest request = new PutObjectRequest(bucket, fileName);
        request.setFile(new File(filePath));
        request.setProgressListener(new ProgressListener() {

            @Override
            public void progressChanged(ProgressStatus status) {
                // 获取上传平均速率
                System.out.println("AverageSpeed:" + status.getAverageSpeed());
                // 获取上传进度百分比
                System.out.println("TransferPercentage:" + status.getTransferPercentage());
            }
        });
        // 每上传1MB数据反馈上传进度
        request.setProgressInterval(1024 * 1024L);
        obsClient.putObject(request);

    }

    /**
     * 上传字节流
     *
     * @param fileName 文件名
     * @param bytes
     */
    public static void uploadByteHuaWei(String fileName, byte[] bytes) {
        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        obsClient.putObject(bucket, fileName, new ByteArrayInputStream(bytes));
    }


    /**
     * 删除文件
     *
     * @param fileName 文件名
     */
    public static void deleteFileFromHuaWei(String fileName) {
        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        obsClient.deleteObject(bucket, fileName);
    }


}
