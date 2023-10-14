package com.yelog.service;

import com.yelog.domain.Session;
import com.yelog.domain.User;
import com.yelog.exception.InvalidSigninInformation;
import com.yelog.repository.UserRepository;
import com.yelog.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public String signin(Login login) {
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(() -> new InvalidSigninInformation());

        Session session = user.addSession();

        return session.getAccessToken();
    }
}
