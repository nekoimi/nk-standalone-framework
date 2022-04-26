package com.nekoimi.standalone.framework.runner;

import com.nekoimi.standalone.framework.config.properties.JWTProperties;
import com.nekoimi.standalone.framework.holder.ContextHolder;
import com.nekoimi.standalone.framework.utils.JwtUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

/**
 * <p>JwtBuilderRunner</p>
 *
 * @author nekoimi 2022/4/24
 */
public class JwtBuilderRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        ApplicationContext context = ContextHolder.getInstance();
        JWTProperties properties = context.getBean(JWTProperties.class);
        ReactiveUserDetailsService userDetailsService = context.getBean(ReactiveUserDetailsService.class);
        JwtUtils.initialization(properties, userDetailsService);
    }
}
