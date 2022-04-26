package com.nekoimi.standalone.framework.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * nekoimi  2022/3/15 15:50
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "认证结果返回结构")
public class LoginResult implements Serializable {
    @ApiModelProperty(value = "jwt-token")
    private String token;
    @ApiModelProperty(value = "用户信息对象")
    private Object user;
}
