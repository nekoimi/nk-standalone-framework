package com.nekoimi.standalone;

import cn.hutool.core.util.StrUtil;
import com.nekoimi.standalone.framework.annotations.EnableQuartz;
import com.nekoimi.standalone.framework.annotations.EnableSecurityAccess;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import reactor.netty.http.server.HttpServer;

import java.net.Inet4Address;

@Slf4j
//@EnableQuartz
//@EnableSecurityAccess
@SpringBootApplication
public class StartApplication {

    @SneakyThrows
    public static void main(String[] args) {
        HttpServer.create().bind().block();
        SpringApplicationBuilder builder = new SpringApplicationBuilder(StartApplication.class);
        builder.bannerMode(Banner.Mode.OFF);
        builder.application().setWebApplicationType(WebApplicationType.REACTIVE);
        ConfigurableApplicationContext context = builder.run(args);
        ServerProperties properties = context.getBean(ServerProperties.class);
        String hostAddress = StrUtil.format("{}:{}", Inet4Address.getLocalHost().getHostAddress(), properties.getPort());
        log.info("Listening on http://{}", hostAddress);
        log.info("Swagger url: http://{}/doc.html", hostAddress);
    }
}
