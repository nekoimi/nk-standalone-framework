package com.nekoimi.standalone.framework.web;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.nekoimi.standalone.framework.constants.DateTimeConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * nekoimi  2021/12/22 10:04
 */
public class HttpJackson2ObjectMapperBuilderCustomizer implements
        Jackson2ObjectMapperBuilderCustomizer, Ordered {

    private final ApplicationContext applicationContext;
    private final JacksonProperties jacksonProperties;

    public HttpJackson2ObjectMapperBuilderCustomizer(ApplicationContext applicationContext,
                                                     JacksonProperties jacksonProperties) {
        this.applicationContext = applicationContext;
        this.jacksonProperties = jacksonProperties;
    }

    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        if (this.jacksonProperties.getDefaultPropertyInclusion() != null) {
            builder.serializationInclusion(this.jacksonProperties.getDefaultPropertyInclusion());
        }
        if (this.jacksonProperties.getTimeZone() != null) {
            builder.timeZone(this.jacksonProperties.getTimeZone());
        }
        configureVisibility(builder, this.jacksonProperties.getVisibility());
        configureFeatures(builder, this.jacksonProperties.getDeserialization());
        configureFeatures(builder, this.jacksonProperties.getSerialization());
        configureFeatures(builder, this.jacksonProperties.getMapper());
        configureFeatures(builder, this.jacksonProperties.getParser());
        configureFeatures(builder, this.jacksonProperties.getGenerator());
        configureDateFormat(builder);
        configurePropertyNamingStrategy(builder);
        configureModules(builder);
        configureLocale(builder);
        //Long 转String类型，否则js丢失精度
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        //支持jdk8新特性
        builder.modules(new ParameterNamesModule(),
                new Jdk8Module(),
                new JavaTimeModule(),
                simpleModule);
        builder.serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeConstants.DEFAULT_DATE_FORMATTER));
        builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeConstants.DEFAULT_DATE_TIME_FORMATTER));
        builder.deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeConstants.DEFAULT_DATE_FORMATTER));
        builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeConstants.DEFAULT_DATE_TIME_FORMATTER));
    }

    private void configureFeatures(Jackson2ObjectMapperBuilder builder, Map<?, Boolean> features) {
        features.forEach((feature, value) -> {
            if (value != null) {
                if (value) {
                    builder.featuresToEnable(feature);
                } else {
                    builder.featuresToDisable(feature);
                }
            }
        });
    }

    private void configureVisibility(Jackson2ObjectMapperBuilder builder, Map<PropertyAccessor, JsonAutoDetect.Visibility> visibilities) {
        visibilities.forEach(builder::visibility);
    }

    private void configureDateFormat(Jackson2ObjectMapperBuilder builder) {
        // We support a fully qualified class name extending DateFormat or a date
        // pattern string value
        String dateFormat = this.jacksonProperties.getDateFormat();
        if (dateFormat != null) {
            try {
                Class<?> dateFormatClass = ClassUtils.forName(dateFormat, null);
                builder.dateFormat((DateFormat) BeanUtils.instantiateClass(dateFormatClass));
            } catch (ClassNotFoundException ex) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
                // Since Jackson 2.6.3 we always need to set a TimeZone (see
                // gh-4170). If none in our properties fallback to the Jackson's
                // default
                TimeZone timeZone = this.jacksonProperties.getTimeZone();
                if (timeZone == null) {
                    timeZone = new ObjectMapper().getSerializationConfig().getTimeZone();
                }
                simpleDateFormat.setTimeZone(timeZone);
                builder.dateFormat(simpleDateFormat);
            }
        }
    }

    private void configurePropertyNamingStrategy(Jackson2ObjectMapperBuilder builder) {
        // We support a fully qualified class name extending Jackson's
        // PropertyNamingStrategy or a string value corresponding to the constant
        // names in PropertyNamingStrategy which hold default provided
        // implementations
        String strategy = this.jacksonProperties.getPropertyNamingStrategy();
        if (strategy != null) {
            try {
                configurePropertyNamingStrategyClass(builder, ClassUtils.forName(strategy, null));
            } catch (ClassNotFoundException ex) {
                configurePropertyNamingStrategyField(builder, strategy);
            }
        }
    }

    private void configurePropertyNamingStrategyClass(Jackson2ObjectMapperBuilder builder, Class<?> propertyNamingStrategyClass) {
        builder.propertyNamingStrategy((PropertyNamingStrategy) BeanUtils.instantiateClass(propertyNamingStrategyClass));
    }

    private void configurePropertyNamingStrategyField(Jackson2ObjectMapperBuilder builder, String fieldName) {
        // Find the field (this way we automatically support new constants
        // that may be added by Jackson in the future)
        Field field = ReflectionUtils.findField(PropertyNamingStrategy.class, fieldName,
                PropertyNamingStrategy.class);
        Assert.notNull(field, () -> "Constant named '" + fieldName + "' not found on "
                + PropertyNamingStrategy.class.getName());
        try {
            builder.propertyNamingStrategy((PropertyNamingStrategy) field.get(null));
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private void configureModules(Jackson2ObjectMapperBuilder builder) {
        Collection<Module> moduleBeans = getBeans(this.applicationContext, Module.class);
        builder.modulesToInstall(moduleBeans.toArray(new Module[0]));
    }

    private void configureLocale(Jackson2ObjectMapperBuilder builder) {
        Locale locale = this.jacksonProperties.getLocale();
        if (locale != null) {
            builder.locale(locale);
        }
    }

    private static <T> Collection<T> getBeans(ListableBeanFactory beanFactory, Class<T> type) {
        return BeanFactoryUtils.beansOfTypeIncludingAncestors(beanFactory, type).values();
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
