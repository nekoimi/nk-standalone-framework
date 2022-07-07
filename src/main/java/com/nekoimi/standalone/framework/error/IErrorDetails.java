package com.nekoimi.standalone.framework.error;

/**
 * nekoimi  2021/12/6 14:43
 *
 * 通用异常接口
 */
public interface IErrorDetails {
    Integer code();
    String message();
    String trace();
}
