package com.yelog.config;

import com.yelog.config.data.UserSession;
import com.yelog.domain.Session;
import com.yelog.exception.Unauthorized;
import com.yelog.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
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
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (servletRequest == null) {
            log.error("serveletRequest null");
            throw  new Unauthorized();
        }
        Cookie[] cookies = servletRequest.getCookies();

        if (cookies.length ==0) {
            log.error("쿠키가 없음");
            throw new Unauthorized();
        }
        String accessToken = cookies[0].getValue();

        // 값이 있을 경우, 데이터 베이스 사용자 확인 작업
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new Unauthorized());

        return new UserSession(session.getUser().getId());

    }
}
