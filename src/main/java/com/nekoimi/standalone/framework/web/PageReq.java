package com.nekoimi.standalone.framework.web;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * nekoimi  2021/12/10 17:53
 */
@Getter
@Setter
@Slf4j
public class PageReq {
    private static final String PAGE = "page";
    private static final String PAGE_SIZE = "pageSize";
    private static final String SORT = "sort";
    private static final String ORDER = "od";

    private static final String ORDER_ASC = "asc";

    public static <T> Page<T> buildFromRequest(ServerHttpRequest request) {
        Dict dict = Dict.create();
        dict.putAll(request.getQueryParams().toSingleValueMap());
        if (log.isDebugEnabled()) {
            dict.forEach((key, value) -> log.debug("{} -> {}", key, value));
        }
        Page<T> result = new Page<>();
        int page = dict.get(PAGE, 1);
        int pageSize = dict.get(PAGE_SIZE, 10);
        String sort = dict.getStr(SORT);
        String order = dict.get(ORDER, ORDER_ASC);
        result.setCurrent(page);
        result.setSize(pageSize);
        if (StrUtil.isNotBlank(sort)) {
            if (ORDER_ASC.equalsIgnoreCase(order)) {
                result.addOrder(OrderItem.asc(sort));
            } else {
                result.addOrder(OrderItem.desc(sort));
            }
        }
        return result;
    }
}
