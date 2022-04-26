package ${package.Entity};

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nekoimi.standalone.framework.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * ${entity} Entity
 *
 * nekoimi  ${date}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder(builderMethodName = "create")
@ApiModel(description = "${table.comment}")
@TableName(value = "${table.name}", autoResultMap = true)
public class ${entity} extends BaseEntity {
    private static final long serialVersionUID = 1L;

    <#list table.fields as field>
    // ${field.comment}
    public static final String FIELD_${field.name?upper_case} = "${field.name}";
    </#list>

<#-- ----------  BEGIN 字段循环遍历  ---------- -->
    /**
     * ===================================================
     * 数据表字段
     * ===================================================
     */
    <#list table.fields as field>

    @TableField
    @ApiModelProperty(value = "${field.comment}")
    private ${field.propertyType} ${field.propertyName};
    </#list>

    /**
     * ===================================================
     * 非数据表字段
     * ===================================================
     */
<#-- ----------  END 字段循环遍历  ---------- -->
}
