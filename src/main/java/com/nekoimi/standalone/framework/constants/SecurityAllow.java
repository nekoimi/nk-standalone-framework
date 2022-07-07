package com.nekoimi.standalone.framework.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * nekoimi  2022/1/12 20:08
 */
public final class SecurityAllow {
    public static final List<String> RESOURCES_ALL = new ArrayList<>();

    static {
        RESOURCES_ALL.add("/");
        RESOURCES_ALL.add("/error");
        RESOURCES_ALL.add("/favicon.ico");
        RESOURCES_ALL.add("/doc.html");
        RESOURCES_ALL.add("/v2/api-docs");
        RESOURCES_ALL.add("/webjars/**");
        RESOURCES_ALL.add("/swagger-resources");
        RESOURCES_ALL.add("/swagger-resources/**");
    }
}
