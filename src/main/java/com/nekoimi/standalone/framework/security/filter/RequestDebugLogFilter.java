package com.nekoimi.standalone.framework.security.filter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * nekoimi  2021/12/21 11:37
 * <p>
 * 请求/响应日志过滤器
 */
@Slf4j
public class RequestDebugLogFilter implements WebFilter {
    private static final String START_TIME = "startTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 构建成一条长 日志，避免并发下日志错乱
        StringBuilder beforeReqLog = new StringBuilder(300);
        // 日志参数
        List<Object> beforeReqArgs = new ArrayList<>();
        beforeReqLog.append("\n\n================ Request Start  ================\n");
        // 打印路由
        beforeReqLog.append("===> {}: {}\n\n");
        // 参数
        String requestMethod = request.getMethodValue();
        URI requestUri = request.getURI();
        StringBuilder requestUrlBuilder = new StringBuilder(requestUri.getPath());
        String query = requestUri.getQuery();
        if (StrUtil.isNotEmpty(query)) {
            requestUrlBuilder.append("?");
            requestUrlBuilder.append(URLDecoder.decode(query, StandardCharsets.UTF_8));
        }
        String requestUrl = requestUrlBuilder.toString();
        beforeReqArgs.add(requestMethod);
        beforeReqArgs.add(requestUrl);

        // 打印请求头
        HttpHeaders headers = request.getHeaders();
        headers.forEach((headerName, headerValue) -> {
            beforeReqLog.append("Header {}: {}\n");
            beforeReqArgs.add(headerName);
            if (headerValue.size() == 1) {
                beforeReqArgs.add(headerValue.get(0));
            } else {
                beforeReqArgs.add(StrUtil.join(",", headerValue));
            }
        });
        beforeReqLog.append("================ Request End =================\n");
        // 打印
        log.info(beforeReqLog.toString(), beforeReqArgs.toArray());

        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            Long startTime = exchange.getAttribute(START_TIME);
            long executeTime = 0L;
            if (startTime != null) {
                executeTime = (System.currentTimeMillis() - startTime);
            }

            // 构建成一条长 日志，避免并发下日志错乱
            StringBuilder responseLog = new StringBuilder(300);
            // 日志参数
            List<Object> responseArgs = new ArrayList<>();
            responseLog.append("\n\n================ Response Start  ================\n");
            // 打印路由 200 get: /mate*/xxx/xxx
            responseLog.append("<=== {} {}: {}: {}\n\n");
            // 参数
            responseArgs.add(Objects.requireNonNull(response.getStatusCode()).value());
            responseArgs.add(requestMethod);
            responseArgs.add(requestUrl);
            responseArgs.add(executeTime + "ms");

            // 打印请求头
            HttpHeaders httpHeaders = response.getHeaders();
            httpHeaders.forEach((headerName, headerValue) -> {
                responseLog.append("Header  {}: {}\n");
                responseArgs.add(headerName);
                if (headerValue.size() == 1) {
                    beforeReqArgs.add(headerValue.get(0));
                } else {
                    beforeReqArgs.add(StrUtil.join(",", headerValue));
                }
            });

            responseLog.append("================  Response End  =================\n");
            // 打印执行时间
            log.info(responseLog.toString(), responseArgs.toArray());
        }));
    }
}
