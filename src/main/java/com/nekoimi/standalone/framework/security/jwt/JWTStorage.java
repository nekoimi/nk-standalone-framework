package com.nekoimi.standalone.framework.security.jwt;

/**
 * @author Nekoimi  2020/5/28 上午11:01
 *
 * jwt token 保存接口
 */
public interface JWTStorage {
    String BLACK_LIST = "black_list";
    String REFRESH_TTL = "refresh:";
    String REFRESHED = "refreshed:";

    /**
     * 获取Token的刷新期限
     * @param token
     * @return
     */
    String getRefreshTtl(String token);

    /**
     * 设置Token刷新期限
     * @param token
     * @param minutes
     */
    void setRefreshTtl(String token, int minutes);

    /**
     * 换取刷新后的token
     * @param token
     * @return
     */
    String getRefreshed(String token);

    /**
     * 旧token为键 新token为value 临时保存
     * 旧token已经被刷新的状态
     * @param token
     * @param newToken
     */
    void setRefreshed(String token, String newToken, int minutes);

    /**
     * 将token加入黑名单
     * @param token
     */
    void black(String token);

    /**
     * 判断token是否被拉黑
     * @param token
     * @return
     */
    boolean isBlack(String token);
}
