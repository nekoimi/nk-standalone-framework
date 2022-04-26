package com.nekoimi.standalone.framework.web;

import com.nekoimi.standalone.framework.contract.TypeConvertible;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>FileSaveInfo</p>
 *
 * @author nekoimi 2022/4/26
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileSaveInfo implements TypeConvertible {
    // 文件名称
    private String filename;
    // 文件大小
    private Long fileSize;
    // 文件类型
    private String mimeType;
    // 相对路径
    private String relativePath;
}
