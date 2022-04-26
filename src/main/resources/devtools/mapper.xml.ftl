<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">
    <!--
        ${entity} Mapper XML

        通用返回结果映射
    -->
    <resultMap id="base_result_map" type="${package.Entity}.${entity}">
<#list table.fields as field>
    <#if field.keyFlag == true >
        <id column="${field.name}" property="${field.propertyName}"/>
    </#if>
</#list>
<#list table.commonFields as field>
        <result column="${field.name}" property="${field.propertyName}"/>
</#list>
<#list table.fields as field>
    <#if field.keyFlag == false>
        <result column="${field.name}" property="${field.propertyName}"/>
    </#if>
</#list>
    </resultMap>

    <!--  通用字段列表  -->
    <sql id="base_column_list">
<#list table.fields as field>
    <#if field.keyFlag == true >
        ${field.name},
    </#if>
</#list>
<#list table.commonFields as field>
        ${field.name},
</#list>
<#list table.fields as field>
    <#if field.keyFlag == false>
        ${field.name},
    </#if>
</#list>
    </sql>

</mapper>