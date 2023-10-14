package com.yelog.repository;

import com.yelog.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

   Optional<User> findByEmailAndPassword(String email, String password);

}
