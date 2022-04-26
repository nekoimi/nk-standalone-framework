package com.nekoimi.standalone.framework.utils;

import cn.hutool.core.util.StrUtil;

import java.io.InputStream;

/**
 * nekoimi  2022/3/27 20:27
 */
public class ResourceFileUtils {

    /**
     * @param filename 资源文件名称
     * @return
     */
    public static InputStream getResourceInputStream(String filename) {
        filename = "/" + StrUtil.removePrefix(filename, "/");
        return ResourceFileUtils.class.getResourceAsStream(filename);
    }
}
