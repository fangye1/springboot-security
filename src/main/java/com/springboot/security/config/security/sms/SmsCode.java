package com.springboot.security.config.security.sms;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SmsCode {
    public final static String SESSION_KEY = "sms_code_";
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 手机号验证码
     */
    private String code;
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * @param mobile   手机号
     * @param code     手机号验证码
     * @param expireIn 过期时效，单位秒
     */
    public SmsCode(String mobile, String code, Integer expireIn) {
        this.mobile = mobile;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }
}
