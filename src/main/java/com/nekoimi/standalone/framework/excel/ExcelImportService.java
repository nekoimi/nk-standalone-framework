package com.nekoimi.standalone.framework.excel;

import reactor.core.publisher.Flux;

import java.io.InputStream;

/**
 * <p>ExcelImportService</p>
 *
 * @author nekoimi 2022/4/25
 */
public interface ExcelImportService {

    /**
     * <p>根据文件流读取excel</p>
     *
     * @param rowType excel行数据类型
     * @param stream  文件流
     * @param <T>     泛型
     * @return
     */
    <T> Flux<RowResult<T>> doImport(Class<T> rowType, InputStream stream);

    /**
     * <p>根据excel文件地址读取excel</p>
     *
     * @param rowType      excel行数据类型
     * @param excelFileUrl excel文件地址, 可以是本地文件绝对路径，也可以是远程文件地址
     * @param <T>          泛型
     * @return
     */
    <T> Flux<RowResult<T>> doImport(Class<T> rowType, String excelFileUrl);
}
