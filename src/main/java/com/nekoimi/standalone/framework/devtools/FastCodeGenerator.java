package com.nekoimi.standalone.framework.devtools;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.nekoimi.standalone.framework.mybatis.BaseEntity;
import com.nekoimi.standalone.framework.mybatis.BaseMapper;
import com.nekoimi.standalone.framework.mybatis.ReactiveICrudService;
import com.nekoimi.standalone.framework.mybatis.ReactiveCrudService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Service;

/**
 * nekoimi  2022/2/17 10:59
 */
@Service
@AllArgsConstructor
public class FastCodeGenerator {
    private final DataSourceProperties properties;

    public void execute(String outputDir, String tableName, String...tablePrefix) {
        FastAutoGenerator.create(properties.getUrl(), properties.getUsername(), properties.getPassword())
                .globalConfig(builder -> {
                    builder.author("devtools") // 设置作者
                            .disableOpenDir()
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir(outputDir); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.nekoimi.standalone"); // 设置父包名
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tableName)
                            .addTablePrefix(tablePrefix)
                            .entityBuilder()
                            .addSuperEntityColumns("id", "created_at", "updated_at", "deleted")
                            .superClass(BaseEntity.class)

                            .mapperBuilder()
                            .superClass(BaseMapper.class)

                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .superServiceClass(ReactiveICrudService.class)

                            .serviceBuilder()
                            .formatServiceImplFileName("%sServiceImpl")
                            .superServiceImplClass(ReactiveCrudService.class); // 设置需要生成的表名
                })
                .templateConfig(builder -> builder.entity("devtools/entity.java")
                        .controller("devtools/controller.java")
                        .mapper("devtools/mapper.java")
                        .mapperXml("devtools/mapper.xml")
                        .service("devtools/service.java")
                        .serviceImpl("devtools/serviceImpl.java"))
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
