package com.sokpulee.crescendo.global.auth;

import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.util.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JWTUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasAuthPrincipalAnnotation = parameter.hasParameterAnnotation(AuthPrincipal.class);
        boolean hasLongType = Long.class.isAssignableFrom(parameter.getParameterType());
        return hasAuthPrincipalAnnotation && hasLongType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        final String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

        return jwtUtil.getUserId(authorization);
    }
}
