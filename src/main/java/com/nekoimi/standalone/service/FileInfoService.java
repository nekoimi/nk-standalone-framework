package com.nekoimi.standalone.service;

import com.nekoimi.standalone.framework.mybatis.ReactiveICrudService;
import com.nekoimi.standalone.entity.FileInfo;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * FileInfo Service
 * <p>
 * nekoimi  2022-04-26
 */
public interface FileInfoService extends ReactiveICrudService<FileInfo> {

    /**
     * <p>上传文件</p>
     *
     * @param filePart      文件
     * @param contentLength 数据长度
     * @return
     */
    Mono<FileInfo> upload(FilePart filePart, long contentLength);

    /**
     * <p>显示文件</p>
     *
     * @param response 响应
     * @param fileId   文件ID
     * @return
     */
    Mono<Void> show(ServerHttpResponse response, String fileId);

    /**
     * <p>下载文件</p>
     *
     * @param response 响应
     * @param fileId   文件ID
     * @return
     */
    Mono<Void> download(ServerHttpResponse response, String fileId);

    /**
     * <p>下载静态文件</p>
     *
     * @param response  响应
     * @param staticKey 静态文件Key
     * @return
     */
    Mono<Void> downloadStatic(ServerHttpResponse response, String staticKey);
}
