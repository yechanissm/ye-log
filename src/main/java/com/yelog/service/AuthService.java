package com.yelog.service;

import com.yelog.crypto.PasswordEncoder;
import com.yelog.crypto.ScryptPasswordEncoder;
import com.yelog.domain.User;
import com.yelog.exception.AlreadyExistsEmailException;
import com.yelog.exception.InvalidSigninInformation;
import com.yelog.repository.UserRepository;
import com.yelog.request.Login;
import com.yelog.request.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Transactional
    public Long signin(Login login) {
        //User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
        //        .orElseThrow(() -> new InvalidSigninInformation());

        //Session session = user.addSession();

        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new InvalidSigninInformation());

        boolean matches = encoder.matches(login.getPassword(), user.getPassword());
        if(!matches) {
            throw  new InvalidSigninInformation();
        }

        return user.getId();
    }

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
