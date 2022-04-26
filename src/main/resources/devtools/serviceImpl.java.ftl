package ${package.ServiceImpl};

import ${superServiceImplClassPackage};
import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * ${entity} Service
 *
 * nekoimi  ${date}
 */
@Service
//@CacheConfig(cacheNames = "${entity?lower_case}-service", keyGenerator = "cacheKeyGenerator", cacheManager = "redisCacheManager")
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {
}
