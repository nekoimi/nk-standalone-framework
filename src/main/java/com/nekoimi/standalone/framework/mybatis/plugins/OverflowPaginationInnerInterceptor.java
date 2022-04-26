package com.nekoimi.standalone.framework.mybatis.plugins;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
 * nekoimi  2022/3/16 9:21
 *
 * 分页溢出默认返回最后一页
 */
public class OverflowPaginationInnerInterceptor extends PaginationInnerInterceptor {

    /**
     * 默认设置成最后一页
     * @param page
     */
    @Override
    protected void handlerOverflow(IPage<?> page) {
        page.setCurrent(page.getPages());
    }
}
