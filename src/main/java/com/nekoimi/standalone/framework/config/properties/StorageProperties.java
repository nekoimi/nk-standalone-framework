package com.nekoimi.standalone.framework.config.properties;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nekoimi  2020/11/27 上午11:31
 */
@Slf4j
@Getter
@Component
@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {
    /**
     * <p>服务器地址</p>
     */
    private String host;
    /**
     * <p>本地文件保存路径</p>
     */
    private String localPath;
    /**
     * <p>静态文件下载映射</p>
     * key: 键值
     * value: 下载文件名
     */
    private Map<String, String> staticMap = new HashMap<>();

    public void setHost(String host) {
        this.host = StrUtil.removeSuffix(host, "/") + "/";
    }

    public void setLocalPath(String localPath) {
        this.localPath = StrUtil.removeSuffix(localPath, "/") + "/";
    }

    public void setStaticMap(Map<String, String> staticMap) {
        this.staticMap = staticMap;
    }
}
