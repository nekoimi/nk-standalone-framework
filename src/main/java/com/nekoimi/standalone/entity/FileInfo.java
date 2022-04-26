package com.nekoimi.standalone.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nekoimi.standalone.framework.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * FileInfo Entity
 *
 * nekoimi  2022-04-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder(builderMethodName = "create")
@ApiModel(description = "文件信息表")
@TableName(value = "sys_file_info", autoResultMap = true)
public class FileInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    // 文件名称
    public static final String FIELD_FILENAME = "filename";
    // 文件大小
    public static final String FIELD_FILE_SIZE = "file_size";
    // 文件类型
    public static final String FIELD_MIME_TYPE = "mime_type";
    // 相对路径
    public static final String FIELD_RELATIVE_PATH = "relative_path";

    /**
     * ===================================================
     * 数据表字段
     * ===================================================
     */

    @TableField
    @ApiModelProperty(value = "文件名称")
    private String filename;

    @TableField
    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @TableField
    @ApiModelProperty(value = "文件类型")
    private String mimeType;

    @TableField
    @ApiModelProperty(value = "相对路径")
    private String relativePath;

    /**
     * ===================================================
     * 非数据表字段
     * ===================================================
     */
}
