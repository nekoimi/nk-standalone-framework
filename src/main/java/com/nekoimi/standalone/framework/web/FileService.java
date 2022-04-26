package com.nekoimi.standalone.framework.web;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.io.InputStream;

/**
 * File Service
 * <p>
 * nekoimi  2022-04-25
 */
public interface FileService {

    /**
     * <p>保存文件</p>
     *
     * @param file          文件
     * @param contentLength 长度
     * @return
     */
    Mono<FileSaveInfo> save(FilePart file, long contentLength);

    /**
     * <p>获取文件流</p>
     *
     * @param fileUrl 文件路径
     * @return
     */
    Mono<InputStream> getFileInputStream(String fileUrl);

    /**
     * <p>零拷贝输出</p>
     *
     * @param response 响应对象
     * @param fileUrl  文件路径
     * @return
     */
    Mono<Void> zeroCopyToResponse(ServerHttpResponse response, String fileUrl);

    /**
     * <p>零拷贝输出</p>
     *
     * @param response             响应对象
     * @param inputStreamPublisher 输入流
     * @return
     */
    Mono<Void> zeroCopyToResponse(ServerHttpResponse response, Mono<InputStream> inputStreamPublisher);

    /**
     * <p>显示文件, 如：图片文件</p>
     *
     * @param response 响应对象
     * @param fileUrl  文件路径
     */
    Mono<Void> show(ServerHttpResponse response, String fileUrl);

    /**
     * <p>下载文件</p>
     *
     * @param response 响应对象
     * @param filename 文件名称
     * @param fileUrl  文件路径
     */
    Mono<Void> download(ServerHttpResponse response, String filename, String fileUrl);

    /**
     * <p>下载静态文件</p>
     *
     * @param response  响应对象
     * @param staticKey 静态文件标识
     */
    Mono<Void> downloadStatic(ServerHttpResponse response, String staticKey);
}
