package ${package.Controller};

import ${package.Entity}.${entity};
import com.nekoimi.standalone.framework.web.PageReq;
import com.nekoimi.standalone.framework.web.PageResult;
import ${package.Service}.${table.serviceName};
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * ${entity} Controller
 *
 * nekoimi  ${date}
 */
@RestController
@Api(tags = "${table.comment}", produces = "application/json", consumes = "application/json")
public class ${table.controllerName} {
    @Autowired
    private ${table.serviceName} ${entity?lower_case}Service;

    /**
     * 获取${table.comment}分页列表
     * @param request
     * @return
     */
    @GetMapping("${table.entityPath}/list")
    @ApiOperation(value = "获取${table.comment}分页列表", response = PageResult.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "分页页码", required = false, defaultValue = "1", paramType = "query", dataType = "Integer"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示条数", required = false, defaultValue = "10", paramType = "query", dataType = "Integer"),
        @ApiImplicitParam(name = "sort", value = "排序字段", required = false, defaultValue = "", paramType = "query", dataType = "String",
        allowableValues = "<#list table.commonFields as field>${field.name},</#list><#list table.fields as field><#if field.keyFlag == false>${field.name}<#if field_has_next>,</#if></#if></#list>"),
        @ApiImplicitParam(name = "order", value = "排序规则", required = false, defaultValue = "", paramType = "query", dataType = "String",
        allowableValues = "asc,desc")
    })
    public Mono<PageResult<${entity}>> pageList(@ApiIgnore ServerHttpRequest request) {
        return ${entity?lower_case}Service.page(PageReq.buildFromRequest(request));
    }

    /**
     * 根据id获取${table.comment}数据
     *
     * @param id
     * @return
     */
    @GetMapping("${table.entityPath}/{id}")
    @ApiOperation(value = "根据id获取${table.comment}数据", response = ${entity}.class)
    public Mono<${entity}> get(@PathVariable("id") String id) {
        return ${entity?lower_case}Service.getByIdOrFail(id);
    }

    /**
     * 添加${table.comment}数据
     *
     * @return
     */
    @PostMapping("${table.entityPath}")
    @ApiOperation(value = "添加${table.comment}数据", response = ${entity}.class)
    public Mono<Void> save(@Validated @RequestBody ${entity} body) {
        return ${entity?lower_case}Service.save(body).then();
    }

    /**
     * 根据id更新${table.comment}
     *
     * @param body
     * @return
     */
    @PutMapping("${table.entityPath}/update")
    @ApiOperation(value = "根据id更新${table.comment}")
    public Mono<Void> update(@PathVariable("id") String id, @Validated @RequestBody ${entity} body) {
        return ${entity?lower_case}Service.updateById(id, body).then();
    }

    /**
     * 根据id更新${table.comment}
     *
     * @param body
     * @return
     */
    @PutMapping("${table.entityPath}/full")
    @ApiOperation(value = "根据id更新${table.comment}")
    public Mono<Void> updateFull(@PathVariable("id") String id, @Validated @RequestBody ${entity} body) {
        return ${entity?lower_case}Service.updateById(id, body).then();
    }

    /**
     * 根据id删除${table.comment}数据
     *
     * @param id
     * @return
     */
    @DeleteMapping("${table.entityPath}/{id}")
    @ApiOperation(value = "根据id删除${table.comment}数据")
    public Mono<Void> remove(@PathVariable("id") String id) {
        return ${entity?lower_case}Service.removeById(id).then();
    }

    /**
     * 批量删除${table.comment}数据
     *
     * @return
     */
    @DeleteMapping("${table.entityPath}/batch")
    @ApiOperation(value = "批量删除${table.comment}数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "id的列表，使用,分割", required = true, example = "1,2,3", paramType = "query", dataType = "String")
    })
    public Mono<Void> removeBatch(@RequestParam(value = "ids") String ids) {
        return ${entity?lower_case}Service.removeBatch(ids);
    }
}
