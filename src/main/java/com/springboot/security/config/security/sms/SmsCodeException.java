package com.springboot.security.config.security.sms;

import org.springframework.security.core.AuthenticationException;

public class SmsCodeException extends AuthenticationException {
    public SmsCodeException(String msg) {
        super(msg);
    }
}
