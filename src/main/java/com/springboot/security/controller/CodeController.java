package com.springboot.security.controller;

import com.google.code.kaptcha.Producer;
import com.springboot.security.config.security.sms.SmsCode;
import com.springboot.security.config.verification.ImageCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

@Slf4j
@Api(tags = "验证码")
@RestController
public class CodeController {
    @Resource
    private Producer producer;

    @ApiOperation(value = "手机验证码")
    @RequestMapping("/sms/code")
    public void getSmsCode(@ApiIgnore HttpServletRequest request,
                           @ApiParam(value = "手机号") @RequestParam String mobile) {
        String code = RandomUtils.nextLong(1000, 9999) + "";
        log.info("手机验证码 {} ", code);
        SmsCode smsCode = new SmsCode(mobile, code, 300);
        request.getSession().setAttribute(SmsCode.SESSION_KEY + mobile, smsCode);
    }

    @ApiOperation(value = "图片验证码")
    @RequestMapping("/image/code")
    public void getImageCode(@ApiIgnore HttpServletRequest request,
                             @ApiIgnore HttpServletResponse response) throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String code = producer.createText();
        BufferedImage image = producer.createImage(code);
        ImageCode imageCode = new ImageCode(code, image, 300);
        request.getSession().setAttribute(ImageCode.SESSION_KEY, imageCode);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(imageCode.getImage(), "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }
}
