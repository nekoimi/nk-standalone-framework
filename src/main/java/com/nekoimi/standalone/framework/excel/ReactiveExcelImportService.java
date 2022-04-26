package com.nekoimi.standalone.framework.excel;

import com.nekoimi.standalone.framework.web.FileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.InputStream;

/**
 * <p>ReactiveExcelImportService</p>
 *
 * @author nekoimi 2022/4/25
 */
@Service
@AllArgsConstructor
public class ReactiveExcelImportService implements ExcelImportService {
    private final FileService fileService;

    @Override
    public <T> Flux<RowResult<T>> doImport(Class<T> rowType, InputStream stream) {
        return ReadExcelListener.of(stream, rowType);
    }

    @Override
    public <T> Flux<RowResult<T>> doImport(Class<T> rowType, String excelFileUrl) {
        return fileService.getFileInputStream(excelFileUrl).flatMapMany(stream -> doImport(rowType, stream));
    }
}
