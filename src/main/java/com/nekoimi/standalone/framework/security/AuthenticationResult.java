package com.nekoimi.standalone.framework.security;

import cn.hutool.core.lang.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * nekoimi  2021/12/16 20:12
 *
 * 用户登录返回信息
 */
@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@ApiModel(description = "认证返回结构")
public class AuthenticationResult {
    @ApiModelProperty("token")
    private String token;
    @ApiModelProperty("认证返回信息")
    private Dict info;
}
