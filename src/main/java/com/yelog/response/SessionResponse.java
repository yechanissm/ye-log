package com.yelog.response;

import lombok.Getter;

import javax.crypto.SecretKey;

@Getter
public class SessionResponse {

    private final String accessToken;


    public SessionResponse(String accessToken) {
        this.accessToken = accessToken;
    }

}
