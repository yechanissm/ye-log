package com.yelog.config;

import com.yelog.config.data.UserSession;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
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

import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;
    private final static String KEY = "4+rwsQ2gJvu0yrkdJnwftn9o30Das9vy4XpI9+t2G3M=";


    //supprotsParameter -> resolveArgument
    //컨트롤러에서 온 요청이 내가 원하는 DTO 인지 확인
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);

    }

    //넘어온 DTO 값 세팅
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String jws = webRequest.getHeader("Authorization");
        if (jws ==null || jws.equals("")) {
            throw new Unauthorized();
        }

        byte[] decodedKEy = Base64.getDecoder().decode(KEY);

        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(decodedKEy)
                    .build()
                    .parseClaimsJws(jws);
            String userId = claims.getBody().getSubject();
            return new UserSession(Long.parseLong(userId));
            //OK, we can trust this JWT
        } catch (JwtException e) {
            throw  new Unauthorized();
        }
        //return new UserSession(session.getUser().getId());
    }
}
