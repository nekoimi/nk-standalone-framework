package com.nekoimi.standalone.framework.mybatis;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author Nekoimi  2020/7/22 上午10:21
 * @see com.baomidou.mybatisplus.core.mapper.BaseMapper
 * <p>
 * TODO 自己在此扩展常用功能接口
 */
public interface BaseMapper<E extends BaseEntity> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<E> {

    /**
     * 根据 entity 条件，查询一条记录
     * <p>查询一条记录，例如 qw.last("limit 1") 限制取一条记录, 注意：多条数据会报异常</p>
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    default E selectOne(@Param(Constants.WRAPPER) Wrapper<E> queryWrapper) {
        List<E> ts = this.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(ts)) {
            if (ts.size() != 1) {
                System.err.println("=======================================");
                System.err.println("查询单个对象出现多条！！！");
                ts.forEach(e -> System.err.println("Id: " + e.getId()));
                System.err.println("=======================================");
                throw ExceptionUtils.mpe("One record is expected, but the query result is multiple records");
            }
            return ts.get(0);
        }
        return null;
    }

    /**
     * 查询列表
     *
     * @param queryWrapper
     * @param handler
     */
    void selectListWithHandler(@Param(Constants.WRAPPER) Wrapper<E> queryWrapper, ResultHandler<E> handler);

    /**
     * 根据idList查询列表
     *
     * @param idList
     * @param handler
     */
    void selectBatchIdsWithHandler(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList, ResultHandler<E> handler);

    /**
     * 查询分页列表
     *
     * @param page
     * @param queryWrapper
     * @param handler
     * @param <P>
     */
    <P extends IPage<E>> void selectPageWithHandler(P page, @Param(Constants.WRAPPER) Wrapper<E> queryWrapper, ResultHandler<E> handler);
}
