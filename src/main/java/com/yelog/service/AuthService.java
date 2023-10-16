package com.yelog.service;

import com.yelog.crypto.PasswordEncoder;
import com.yelog.domain.User;
import com.yelog.exception.AlreadyExistsEmailException;
import com.yelog.repository.UserRepository;
import com.yelog.request.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public void signUp(SignUp signUp) {
        Optional<User> userOptional = userRepository.findByEmail(signUp.getEmail());
        if(userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }


        String encodedPassword = encoder.encrypt(signUp.getPassword());

        User user = User.builder()
                .name(signUp.getName())
                .password(encodedPassword)
                .email(signUp.getEmail())
                .build();
        userRepository.save(user);
    }
}
