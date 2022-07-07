package com.nekoimi.standalone.framework.error.handler;

import com.nekoimi.standalone.framework.contract.error.ErrorExceptionHandler;
import com.nekoimi.standalone.framework.error.ErrorDetails;
import com.nekoimi.standalone.framework.error.Errors;
import com.nekoimi.standalone.framework.error.IErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * nekoimi  2021/7/21 下午3:34
 * <p>
 * HTTP的异常处理
 */
@Slf4j
@Component
public class ResponseStatusErrorExceptionHandler implements ErrorExceptionHandler<ResponseStatusException> {
    @Override
    public Class<ResponseStatusException> getType() {
        return ResponseStatusException.class;
    }

    @Override
    public Mono<? extends IErrorDetails> handle(ServerWebExchange exchange, ResponseStatusException e) {
        HttpStatus status = e.getStatus();
        return Mono.fromCallable(() -> {
            IErrorDetails error = Errors.DEFAULT_SERVER_ERROR;
            if (status.is4xxClientError()) {
                error = buildHttpStatusClientError(status);
            } else if (status.is5xxServerError()) {
                error = buildHttpStatusServerError(status);
            }
            StringWriter stackTrace = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stackTrace);
            e.printStackTrace(printWriter);
            String traceMessage = stackTrace.toString();
            return ErrorDetails.of(error.code(), e.getMessage(), traceMessage);
        });
    }

    private Errors buildHttpStatusClientError(HttpStatus status) {
        return getClientError(status);
    }

    private Errors buildHttpStatusServerError(HttpStatus status) {
        log.error("Http status server error! {}", status);
        return Errors.DEFAULT_SERVER_ERROR;
    }

    public static Errors getClientError(HttpStatus status) {
        switch (status) {
            case BAD_REQUEST:
                return Errors.HTTP_STATUS_BAD_REQUEST;
            case UNAUTHORIZED:
                return Errors.HTTP_STATUS_UNAUTHORIZED;
            case FORBIDDEN:
                return Errors.HTTP_STATUS_FORBIDDEN;
            case NOT_FOUND:
                return Errors.HTTP_STATUS_NOT_FOUND;
            case METHOD_NOT_ALLOWED:
                return Errors.HTTP_STATUS_METHOD_NOT_ALLOWED;
            case NOT_ACCEPTABLE:
                return Errors.HTTP_STATUS_NOT_ACCEPTABLE;
        }
        return Errors.DEFAULT_CLIENT_ERROR;
    }
}
