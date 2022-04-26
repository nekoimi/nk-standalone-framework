package com.nekoimi.standalone.controller;

import com.nekoimi.standalone.entity.FileInfo;
import com.nekoimi.standalone.service.FileInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

/**
 * File Controller
 * <p>
 * nekoimi  2022-04-25
 */
@RestController
@Api(tags = "文件信息", produces = "application/json", consumes = "application/json")
public class FileInfoController {
    @Autowired
    private FileInfoService fileInfoService;

    @PostMapping(value = "file/uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "上传文件")
    public Mono<FileInfo> upload(@RequestPart("file") FilePart filePart,
                                 @RequestHeader(HttpHeaders.CONTENT_LENGTH) long contentLength) {
        return fileInfoService.upload(filePart, contentLength);
    }

    @GetMapping("file/{fileId}")
    @ApiOperation(value = "显示文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileId", value = "文件ID", required = false, defaultValue = "", paramType = "path", dataType = "String")
    })
    public Mono<Void> show(@ApiIgnore ServerHttpResponse response, @PathVariable("fileId") String fileId) {
        return fileInfoService.show(response, fileId);
    }

    @GetMapping("file/{fileId}/download")
    @ApiOperation(value = "下载文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileId", value = "文件ID", required = false, defaultValue = "", paramType = "path", dataType = "String")
    })
    public Mono<Void> download(@ApiIgnore ServerHttpResponse response, @PathVariable("fileId") String fileId) {
        return fileInfoService.download(response, fileId);
    }

    @GetMapping("file/static/{staticKey}/download")
    @ApiOperation(value = "下载静态文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "staticKey", value = "静态文件key", required = false,
                    defaultValue = "", paramType = "path", dataType = "String",
                    allowableValues = "")
    })
    public Mono<Void> downloadStatic(@ApiIgnore ServerHttpResponse response, @PathVariable("staticKey") String staticKey) {
        return fileInfoService.downloadStatic(response, staticKey);
    }
}
