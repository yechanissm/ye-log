package com.yelog.repository;

import com.yelog.domain.Session;
import com.yelog.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {


}
