package com.nekoimi.standalone.framework.web;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.nekoimi.standalone.framework.config.properties.StorageProperties;
import com.nekoimi.standalone.framework.constants.DateTimeConstants;
import com.nekoimi.standalone.framework.error.exception.FailedToNotFoundErrorException;
import com.nekoimi.standalone.framework.error.exception.RequestValidationException;
import com.nekoimi.standalone.framework.utils.LocalDateTimeUtils;
import com.nekoimi.standalone.framework.utils.ResourceFileUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * <p>LocalDiskFileServiceImpl</p>
 *
 * @author nekoimi 2022/4/25
 */
@Slf4j
@Primary
@Component
@AllArgsConstructor
public class LocalDiskFileService implements FileService {
    private final StorageProperties properties;
    private final IdentifierGenerator idGenerator;
    private final WebClient webClient;

    /**
     * <p>创建日期文件目录</p>
     */
    private String createDateTimeDir() {
        String dateTimeDir = LocalDateTimeUtils.nowFormat(DateTimeConstants.DEFAULT_DATE_FORMAT);
        FileUtil.mkdir(properties.getLocalPath() + dateTimeDir);
        return dateTimeDir;
    }

    /**
     * <p>创建文件信息</p>
     *
     * @param filePart      文件
     * @param contentLength 长度
     * @return
     */
    private Mono<FileSaveInfo> createFileInfo(FilePart filePart, long contentLength) {
        String fileId = idGenerator.nextUUID(null);
        String filename = filePart.filename();
        String extName = FileNameUtil.getSuffix(filename);
        String mimeType = FileUtil.getMimeType(filename);
        String relativePath = StrUtil.format("{}/{}.{}", createDateTimeDir(), fileId, extName);
        return Mono.just(new FileSaveInfo()).flatMap(saveInfo -> {
            saveInfo.setFilename(filename);
            saveInfo.setFileSize(contentLength);
            saveInfo.setMimeType(mimeType);
            saveInfo.setRelativePath(relativePath);
            return Mono.just(saveInfo);
        });
    }

    @Override
    public Mono<FileSaveInfo> save(FilePart file, long contentLength) {
        if (file == null) {
            return Mono.error(new RequestValidationException("文件不能为空"));
        }

        return Mono.just(file).map(part -> Flux.from(part.content())
                .flatMap(dataBuffer -> dataBuffer.readableByteCount() <= 0 ? Mono.error(new RequestValidationException("文件不能为空")) : Mono.just(dataBuffer)))
                .flatMap(dataBufferFlux -> Mono.zip(createFileInfo(file, contentLength), Mono.just(dataBufferFlux)))
                .flatMap(tuple -> DataBufferUtils.write(tuple.getT2(),
                        FileUtil.newFile(properties.getLocalPath() + tuple.getT1().getRelativePath()).toPath())
                        .then(Mono.just(tuple.getT1())));
    }

    @Override
    public Mono<InputStream> getFileInputStream(String fileUrl) {
        return Mono.defer(() -> {
            if (fileUrl.startsWith("http")) {
                return webClient
                        .get()
                        .uri(fileUrl)
                        .accept(MediaType.APPLICATION_OCTET_STREAM)
                        .retrieve()
                        .bodyToMono(Resource.class)
                        .flatMap(resource -> Mono.fromCallable(resource::getInputStream));
            } else {
                return Mono.fromCallable(() -> ResourceFileUtils.getResourceInputStream(fileUrl))
                        .switchIfEmpty(Mono.fromCallable(() -> new FileInputStream(fileUrl)));
            }
        });
    }

    @Override
    public Mono<Void> zeroCopyToResponse(ServerHttpResponse response, String fileUrl) {
        return zeroCopyToResponse(response, getFileInputStream(fileUrl));
    }

    @Override
    public Mono<Void> zeroCopyToResponse(ServerHttpResponse response, Mono<InputStream> inputStreamPublisher) {
        return inputStreamPublisher.switchIfEmpty(Mono.error(new RequestValidationException("No input stream!"))).flatMap(inputStream -> {
            response.getHeaders().setExpires(0);
            response.getHeaders().addIfAbsent(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
            try {
                response.getHeaders().setContentLength(inputStream.available());
            } catch (IOException e) {
                if (log.isDebugEnabled()) {
                    e.printStackTrace();
                }
                log.error(e.getMessage(), e);
            }
            return Mono.just(response).cast(ZeroCopyHttpOutputMessage.class)
                    .flatMap(zeroCopy -> Mono.just(DataBufferUtils.readInputStream(() -> inputStream,
                            zeroCopy.bufferFactory(), 128))
                            .flatMap(zeroCopy::writeWith));
        });
    }

    @Override
    public Mono<Void> show(ServerHttpResponse response, String fileUrl) {
        return zeroCopyToResponse(response, fileUrl);
    }

    @Override
    public Mono<Void> download(ServerHttpResponse response, String filename, String fileUrl) {
        response.getHeaders().setContentDisposition(ContentDisposition.attachment().filename(filename, StandardCharsets.UTF_8).build());
        return zeroCopyToResponse(response, fileUrl);
    }

    @Override
    public Mono<Void> downloadStatic(ServerHttpResponse response, String staticKey) {
        Map<String, String> staticMap = properties.getStaticMap();
        String filePath = staticMap.get(staticKey);
        if (StrUtil.isEmpty(filePath)) {
            return Mono.error(new FailedToNotFoundErrorException());
        }
        String filename = FileNameUtil.getName(filePath);
        return download(response, filename, filePath);
    }
}
