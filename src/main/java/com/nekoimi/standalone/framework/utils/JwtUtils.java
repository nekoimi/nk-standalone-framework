package com.nekoimi.standalone.framework.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nekoimi.standalone.framework.config.properties.JWTProperties;
import com.nekoimi.standalone.framework.error.exception.token.TokenBlackListException;
import com.nekoimi.standalone.framework.error.exception.token.TokenCannotBeRefreshException;
import com.nekoimi.standalone.framework.security.jwt.JWTStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Optional;

/**
 * nekoimi  2022/2/16 16:36
 */
@Slf4j
public class JwtUtils {
    private final static String issuer = ClassUtil.getShortClassName(JwtUtils.class.getName());
    private static JWTStorage jwtStorage;
    private static ReactiveUserDetailsService userDetailsService;

    private static Algorithm algorithm;
    private static Integer ttl;
    private static Integer refreshTtl;
    private static Integer refreshConcurrentTtl;

    public static void initialization(JWTProperties properties, ReactiveUserDetailsService userDetailsService) {
        JwtUtils.algorithm = Algorithm.HMAC256(properties.getSecret());
        JwtUtils.ttl = properties.getTtl();
        JwtUtils.refreshTtl = properties.getRefreshTtl();
        JwtUtils.refreshConcurrentTtl = properties.getRefreshConcurrentTtl();
        JwtUtils.userDetailsService = userDetailsService;
    }

    public static String encode(UserDetails userDetails) {
        Date now = new Date();
        JWTCreator.Builder builder = JWT.create();
        builder = builder.withIssuer(issuer);
        builder = builder.withIssuedAt(now);
        builder = builder.withExpiresAt(DateUtil.offsetMinute(now, ttl));
        builder = builder.withSubject(userDetails.getUsername());
        String token = builder.sign(algorithm);
        // 设置Token的刷新期限
        jwtStorage.setRefreshTtl(token, Optional.ofNullable(refreshTtl).orElse(0));
        return token;
    }

    public static String decodeSubject(String token) {
        return checkParse(token).getSubject();
    }

    public static synchronized String refresh(String token) {
        // todo 这里需要检查这个token是否已经被刷新过 旧Token已经被刷新过就不需要在刷新了
        String refreshedToken = jwtStorage.getRefreshed(token);
        if (StrUtil.isNotBlank(refreshedToken)) {
            jwtStorage.black(token);
            return refreshedToken;
        }

        String refreshToken = jwtStorage.getRefreshTtl(token);
        if (StrUtil.isBlank(refreshToken)) {
            // 当前Token已经超过刷新期限了
            throw new TokenCannotBeRefreshException();
        }

        String subject = parse(token).getSubject();
        Mono<UserDetails> detailsMono = userDetailsService.findByUsername(subject);
        return detailsMono.map(userDetails -> {
            String newToken = encode(userDetails);
            // todo 将旧Token设置为已经刷新过了
            jwtStorage.setRefreshed(token, newToken, refreshConcurrentTtl <= ttl ? refreshConcurrentTtl : ttl);
            // 加入黑名单
            jwtStorage.black(token);
            return newToken;
        }).block();
    }

    public static DecodedJWT parse(String token) {
        if (jwtStorage.isBlack(token)) {
            String refreshedToken = jwtStorage.getRefreshed(token);
            if (StrUtil.isBlank(refreshedToken)) {
                throw new TokenBlackListException();
            }

            token = refreshedToken;
        }
        return JWT.decode(token);
    }

    public static DecodedJWT checkParse(String token) {
        JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return parse(token);
    }
}
