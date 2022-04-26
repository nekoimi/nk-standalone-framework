package com.nekoimi.standalone.framework.resolver;

import com.nekoimi.standalone.framework.error.exception.InjectRequestAttributeErrorException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

/**
 * nekoimi  2022/3/26 16:36
 */
public abstract class AbstractInjectRequestAttributeArgumentResolver implements HandlerMethodArgumentResolver {

    abstract protected String attributeName();

    abstract protected Class<? extends Annotation> annotation();

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(annotation());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        Object o = nativeWebRequest.getAttribute(attributeName(), RequestAttributes.SCOPE_REQUEST);
        if (o == null) {
            throw new InjectRequestAttributeErrorException();
        }
        Parameter p = methodParameter.getParameter();
        Class<?> typeClazz = p.getType();
        if (!typeClazz.isInstance(o)) {
            throw new InjectRequestAttributeErrorException();
        }
        return o;
    }
}
