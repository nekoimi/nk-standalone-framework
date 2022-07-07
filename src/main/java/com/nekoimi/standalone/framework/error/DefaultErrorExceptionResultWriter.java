package com.nekoimi.standalone.framework.error;

import cn.hutool.core.util.ClassUtil;
import com.nekoimi.standalone.framework.contract.error.ErrorExceptionResultWriter;
import com.nekoimi.standalone.framework.contract.error.ErrorExceptionWriter;
import com.nekoimi.standalone.framework.protocol.JsonResp;
import com.nekoimi.standalone.framework.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


/**
 * nekoimi  2021/12/13 22:23
 */
@Slf4j
@Component
@AllArgsConstructor
public class DefaultErrorExceptionResultWriter implements ErrorExceptionResultWriter {
    private final List<ErrorExceptionWriter> writers;

    private void doWriter(ServerWebExchange exchange, IErrorDetails error) {
        writers.forEach(writer -> {
            log.debug("result writer do writer -- {}", ClassUtil.getClassName(writer, true));
            writer.writer(exchange, error);
        });
    }

    public Mono<Void> httpWriter(ServerWebExchange exchange, IErrorDetails error) {
        return Mono.defer(() -> {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.OK);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            JsonResp<Object> resp = JsonResp.error(error.code(), error.message());
            byte[] valueAsBytes = JsonUtils.writeBytes(resp);
            DataBuffer buffer = response.bufferFactory().wrap(valueAsBytes);
            return response.writeWith(Mono.just(buffer)).doOnError(e -> DataBufferUtils.release(buffer));
        });
    }

    @Override
    public Mono<Void> writer(ServerWebExchange exchange, IErrorDetails error) {
        doWriter(exchange, error);
        return httpWriter(exchange, error);
    }
}
