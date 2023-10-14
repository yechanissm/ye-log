package com.yelog.config;

import com.yelog.config.data.UserSession;
import com.yelog.exception.Unauthorized;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthResolver implements HandlerMethodArgumentResolver {

    //supprotsParameter -> resolveArgument
    //컨트롤러에서 온 요청이 내가 원하는 DTO 인지 확인
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);

    }

    //넘어온 DTO 값 세팅
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessToken = webRequest.getParameter("accessToken");
        if (accessToken ==null || accessToken.equals("")) {
            throw new Unauthorized();
        }

        UserSession userSession = new UserSession();
        userSession.name = accessToken;
        return userSession;
    }
}
