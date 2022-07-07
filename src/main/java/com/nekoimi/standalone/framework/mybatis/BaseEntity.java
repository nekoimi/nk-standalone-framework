package com.nekoimi.standalone.framework.mybatis;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nekoimi.standalone.framework.contract.TypeConvertible;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Nekoimi  2020/5/30 23:25
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity implements TypeConvertible {
    public static final String FIELD_ID = "id";
    public static final String FIELD_CREATED_AT = "created_at";
    public static final String FIELD_UPDATED_AT = "updated_at";
    public static final String FIELD_DELETED = "deleted";
    public static final String PROPERTY_CREATED_AT = "createdAt";
    public static final String PROPERTY_UPDATED_AT = "updatedAt";

    @TableId(type = IdType.ASSIGN_UUID)
    protected String id;

    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updatedAt;

    // 软删除字段；1 - 已删除，0 - 未删除
    @JsonIgnore
    @TableLogic(value = "0", delval = "1")
    protected Integer deleted;
}
