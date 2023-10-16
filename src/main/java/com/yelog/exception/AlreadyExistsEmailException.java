package com.yelog.exception;

public class AlreadyExistsEmailException extends YelogException{

    private final static String MESSAGE = "이미 가입된 이메일 입니다.";

    public AlreadyExistsEmailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
