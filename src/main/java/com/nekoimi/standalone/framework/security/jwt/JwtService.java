package com.nekoimi.standalone.framework.security.jwt;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Optional;

/**
 * nekoimi  2022/2/16 16:36
 */
@Slf4j
@Service
public class JwtService {
    private final static String issuer = ClassUtil.getShortClassName(JwtService.class.getName());
    private final JWTProperties properties;
    private final JwtStorage jwtStorage;
    private final ReactiveUserDetailsService userDetailsService;

    private Algorithm algorithm;
    private Integer ttl;
    private Integer refreshTtl;
    private Integer refreshConcurrentTtl;

    public JwtService(JWTProperties properties, JwtStorage jwtStorage, ReactiveUserDetailsService userDetailsService) {
        this.properties = properties;
        this.jwtStorage = jwtStorage;
        this.userDetailsService = userDetailsService;

        this.algorithm = Algorithm.HMAC256(properties.getSecret());
        this.ttl = properties.getTtl();
        this.refreshTtl = properties.getRefreshTtl();
        this.refreshConcurrentTtl = properties.getRefreshConcurrentTtl();
    }

    public String encode(UserDetails userDetails) {
        Date now = new Date();
        JWTCreator.Builder builder = JWT.create();
        builder = builder.withIssuer(issuer);
        builder = builder.withIssuedAt(now);
        builder = builder.withExpiresAt(DateUtil.offsetMinute(now, ttl));
        builder = builder.withSubject(userDetails.getUsername());
        String token = builder.sign(algorithm);
        // ??????Token???????????????
        jwtStorage.setRefreshTtl(token, Optional.ofNullable(refreshTtl).orElse(0));
        return token;
    }

    public String decodeSubject(String token) {
        return checkParse(token).getSubject();
    }

    public synchronized String refresh(String token) {
        // todo ????????????????????????token???????????????????????? ???Token??????????????????????????????????????????
        String refreshedToken = jwtStorage.getRefreshed(token);
        if (StrUtil.isNotBlank(refreshedToken)) {
            jwtStorage.black(token);
            return refreshedToken;
        }

        String refreshToken = jwtStorage.getRefreshTtl(token);
        if (StrUtil.isBlank(refreshToken)) {
            // ??????Token???????????????????????????
            throw new TokenCannotBeRefreshException();
        }

        String subject = parse(token).getSubject();
        Mono<UserDetails> detailsMono = userDetailsService.findByUsername(subject);
        return detailsMono.map(userDetails -> {
            String newToken = encode(userDetails);
            // todo ??????Token???????????????????????????
            jwtStorage.setRefreshed(token, newToken, refreshConcurrentTtl <= ttl ? refreshConcurrentTtl : ttl);
            // ???????????????
            jwtStorage.black(token);
            return newToken;
        }).block();
    }

    public DecodedJWT parse(String token) {
        if (jwtStorage.isBlack(token)) {
            String refreshedToken = jwtStorage.getRefreshed(token);
            if (StrUtil.isBlank(refreshedToken)) {
                throw new TokenBlackListException();
            }

            token = refreshedToken;
        }
        return JWT.decode(token);
    }

    public DecodedJWT checkParse(String token) {
        JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return parse(token);
    }
}
