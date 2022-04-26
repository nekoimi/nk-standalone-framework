package com.nekoimi.standalone.framework.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.nekoimi.standalone.framework.config.properties.IdGeneratorProperties;
import com.nekoimi.standalone.framework.mybatis.FixedDateTimeFieldMetaObjectHandler;
import com.nekoimi.standalone.framework.mybatis.helper.SnowflakeIdGenerator;
import com.nekoimi.standalone.framework.mybatis.injector.ExtensionSqlInjector;
import com.nekoimi.standalone.framework.mybatis.plugins.OverflowPaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * nekoimi  2020/7/6 下午3:51
 */
@Slf4j
@Configuration
@EnableTransactionManagement
public class MyBatisConfiguration {

    /**
     * <p>雪花算法ID生成器</p>
     *
     * @return
     */
    @Bean
    public IdentifierGenerator identifierGenerator(IdGeneratorProperties properties) {
        return new SnowflakeIdGenerator(properties.getDataCenterId(), properties.getWorkerId());
    }

    /**
     * <p>公共字段注入</p>
     *
     * @return
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new FixedDateTimeFieldMetaObjectHandler();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

//        /**
//         * 多租户插件
//         */
//        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
//            @Override
//            public Expression getTenantId() {
//                return null;
//            }
//        }));

//        /**
//         * 动态表名插件
//         */
//        interceptor.addInnerInterceptor(new DynamicTableNameInnerInterceptor(new TableNameHandler() {
//            @Override
//            public String dynamicTableName(String sql, String tableName) {
//                return null;
//            }
//        }));

        /**
         * 分页插件
         */
        OverflowPaginationInnerInterceptor overflowPaginationInnerInterceptor = new OverflowPaginationInnerInterceptor();
        overflowPaginationInnerInterceptor.setMaxLimit(500L);
        overflowPaginationInnerInterceptor.setOverflow(true);
        interceptor.addInnerInterceptor(overflowPaginationInnerInterceptor);

//        /**
//         * sql 性能规范插件
//         */
//        interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());
        return interceptor;
    }

    /**
     * <p>扩展</p>
     *
     * @return
     */
    @Bean
    public ISqlInjector extensionSqlInjector() {
        return new ExtensionSqlInjector();
    }
}
