package com.yelog.service;

import com.yelog.domain.Session;
import com.yelog.domain.User;
import com.yelog.exception.AlreadyExistsEmailException;
import com.yelog.exception.InvalidRequest;
import com.yelog.exception.InvalidSigninInformation;
import com.yelog.repository.UserRepository;
import com.yelog.request.Login;
import com.yelog.request.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public Long signin(Login login) {
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(() -> new InvalidSigninInformation());

        Session session = user.addSession();

        return user.getId();
    }

    public void signUp(SignUp signUp) {
        Optional<User> userOptional = userRepository.findByEmail(signUp.getEmail());
        if(userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        SCryptPasswordEncoder encoder = new SCryptPasswordEncoder(16, 8, 1, 32, 64);
        String encodedPassword = encoder.encode(signUp.getPassword());

        User user = User.builder()
                .name(signUp.getName())
                .password(encodedPassword)
                .email(signUp.getEmail())
                .build();
        userRepository.save(user);
    }
}
