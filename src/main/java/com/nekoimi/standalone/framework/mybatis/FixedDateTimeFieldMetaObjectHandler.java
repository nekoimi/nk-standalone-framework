package com.nekoimi.standalone.framework.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * nekoimi  2021/7/2 下午3:40
 */
public class FixedDateTimeFieldMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName(BaseEntity.PROPERTY_CREATED_AT, LocalDateTime.now(), metaObject);
        this.setFieldValByName(BaseEntity.PROPERTY_UPDATED_AT, LocalDateTime.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName(BaseEntity.PROPERTY_UPDATED_AT, LocalDateTime.now(), metaObject);
    }
}
