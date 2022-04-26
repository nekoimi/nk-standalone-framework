package com.nekoimi.standalone.service.impl;

import com.nekoimi.standalone.framework.config.properties.StorageProperties;
import com.nekoimi.standalone.framework.mybatis.ReactiveCrudService;
import com.nekoimi.standalone.entity.FileInfo;
import com.nekoimi.standalone.framework.web.FileService;
import com.nekoimi.standalone.mapper.FileInfoMapper;
import com.nekoimi.standalone.service.FileInfoService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * FileInfo Service
 * <p>
 * nekoimi  2022-04-26
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "fileinfo-service", keyGenerator = "cacheKeyGenerator", cacheManager = "redisCacheManager")
public class FileInfoServiceImpl extends ReactiveCrudService<FileInfoMapper, FileInfo> implements FileInfoService {
    private final StorageProperties properties;
    private final FileService fileService;

    @Override
    public Mono<FileInfo> upload(FilePart filePart, long contentLength) {
        return fileService.save(filePart, contentLength)
                .map(saveInfo -> saveInfo.convert(FileInfo.class))
                .flatMap(fileInfo -> save(fileInfo).flatMap(this::getById));
    }

    @Override
    public Mono<Void> show(ServerHttpResponse response, String fileId) {
        return getByIdOrFail(fileId).flatMap(fileInfo -> fileService.show(response, properties.getLocalPath() + fileInfo.getRelativePath()));
    }

    @Override
    public Mono<Void> download(ServerHttpResponse response, String fileId) {
        return getByIdOrFail(fileId).flatMap(fileInfo -> fileService.download(response, fileInfo.getFilename(), properties.getLocalPath() + fileInfo.getRelativePath()));
    }

    @Override
    public Mono<Void> downloadStatic(ServerHttpResponse response, String staticKey) {
        return fileService.downloadStatic(response, staticKey);
    }
}
