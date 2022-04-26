package com.nekoimi.standalone.framework.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.nekoimi.standalone.framework.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.InputStream;

/**
 * nekoimi  2022/3/31 17:17
 * <p>
 * 该类不能交给Spring容器管理
 */
@Slf4j
public class ReadExcelListener<T> extends AnalysisEventListener<T> {
    private FluxSink<RowResult<T>> sink;

    public ReadExcelListener(FluxSink<RowResult<T>> sink) {
        this.sink = sink;
    }

    /**
     * <p>创建Excel读取解析实例</p>
     *
     * @param fileInputStream excel文件流
     * @param rowType         excel行数据类型
     * @param <T>             泛型
     * @return
     */
    public static <T> Flux<RowResult<T>> of(InputStream fileInputStream, Class<T> rowType) {
        return Flux.create(fluxSink -> EasyExcel.read(fileInputStream, rowType, new ReadExcelListener<>(fluxSink)).sheet().doRead());
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        log.debug("解析到一条数据:{}", JsonUtils.write(data));
        RowResult<T> rowResult = new RowResult<>();
        rowResult.setIndex(context.readRowHolder().getRowIndex());
        rowResult.setResult(data);
        this.sink.next(rowResult);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        this.sink.complete();
        log.debug("所有数据解析完成！");
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        this.sink.error(exception);
    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        return !this.sink.isCancelled();
    }
}
