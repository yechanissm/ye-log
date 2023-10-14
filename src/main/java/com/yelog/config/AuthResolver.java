package com.yelog.config;

import com.yelog.config.data.UserSession;
import com.yelog.domain.Session;
import com.yelog.exception.Unauthorized;
import com.yelog.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    //supprotsParameter -> resolveArgument
    //컨트롤러에서 온 요청이 내가 원하는 DTO 인지 확인
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);

    }

    //넘어온 DTO 값 세팅
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessToken = webRequest.getHeader("Authorization");
        if (accessToken ==null || accessToken.equals("")) {
            throw new Unauthorized();
        }
        // 값이 있을 경우, 데이터 베이스 사용자 확인 작업
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new Unauthorized());

        return new UserSession(session.getUser().getId());

    }
}
