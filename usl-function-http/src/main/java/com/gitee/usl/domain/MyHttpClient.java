package com.gitee.usl.domain;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.http.client.HttpClient;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
/**
 * @author jingshu.zeng
 */
@Slf4j
public class MyHttpClient {
    public static void get(String url) {
        HttpClient httpClient = new HttpClient(url);
        try {
            httpClient.get()
                    .onSuccess(response -> log.info("成功响应体：{}", response.body()))
                    .onFailure(e -> log.error("请求执行失败", e))
                    .done();
        } catch (Exception e) {
            log.error("请求执行过程中发生异常", e);
        }
    }

    public static void get(String host, int port, String uri) {
        HttpClient httpClient = new HttpClient(host, port);
        try {
            httpClient.get(uri)
                    .onSuccess(response -> log.info("成功响应体：{}", response.body()))
                    .onFailure(e -> log.error("请求执行失败", e))
                    .done();
        } catch (Exception e) {
            log.error("请求执行过程中发生异常", e);
        }
    }

    public static void post(String url) {
        HttpClient httpClient = new HttpClient(url);
        try {
            httpClient.post()
                    .onSuccess(response -> log.info("成功响应体：{}", response.body()))
                    .onFailure(e -> log.error("请求执行失败", e))
                    .done();
        } catch (Exception e) {
            log.error("请求执行过程中发生异常", e);
        }
    }


    public static void post(String host, int port, String uri) {
        HttpClient httpClient = new HttpClient(host, port);
        try {
            httpClient.post(uri)
                    .onSuccess(response -> log.info("成功响应体：{}", response.body()))
                    .onFailure(e -> log.error("请求执行失败", e))
                    .done();
        } catch (Exception e) {
            log.error("请求执行过程中发生异常", e);
        }
    }

    public static String post(String url, Map<String, Object> paramMap) {
        try {
            return HttpUtil.post(url, paramMap);
        } catch (Exception e) {
            log.error("请求执行过程中发生异常", e);
            return null;
        }
    }

    public static String uploadFile(String serverUrl, String filePath) {
        try {
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("file", FileUtil.file(filePath));
            return HttpUtil.post(serverUrl, paramMap);
        } catch (Exception e) {
            log.error("文件上传过程中发生异常", e);
            return null;
        }
    }


    /**
     * 下载文件并保存到指定路径
     * @param fileUrl 文件的URL地址
     * @param savePath 文件保存的路径
     * @return 下载的文件大小（字节数），如果下载失败返回0
     */
    public static long downloadFile(String fileUrl, String savePath) {
        try {
            File saveFile = FileUtil.file(savePath); // 保存文件的路径
            return HttpUtil.downloadFile(fileUrl, saveFile); // 下载文件
        } catch (Exception e) {
            log.error("文件下载过程中发生异常", e);
            return 0; // 如果下载过程中发生异常，则返回0表示下载失败
        }
    }


}
