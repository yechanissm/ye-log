package com.yelog.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "users")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private LocalDateTime created_At;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Session> sessions = new ArrayList<>();

    public Session addSession() {
        Session session = Session.builder()
                .user(this)
                .build();
        sessions.add(session);
        return session;
    }

    @Builder
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.created_At = LocalDateTime.now();
    }
}
