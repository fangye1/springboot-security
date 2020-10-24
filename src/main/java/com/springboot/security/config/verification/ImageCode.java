package com.springboot.security.config.verification;

import lombok.Getter;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

@Getter
public class ImageCode {
    public final static String SESSION_KEY = "image_code";
    /**
     * 图形验证码图片
     */
    private BufferedImage image;
    /**
     * 图形验证码文字
     */
    private String code;
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * @param code     图形验证码文字
     * @param image    图形验证码图片
     * @param expireIn 过期时效，单位秒
     */
    public ImageCode(String code, BufferedImage image, int expireIn) {
        this.code = code;
        this.image = image;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }
}
