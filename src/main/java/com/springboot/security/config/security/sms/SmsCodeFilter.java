package com.springboot.security.config.security.sms;

import com.springboot.security.config.security.MyAuthenticationFailureHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class SmsCodeFilter extends OncePerRequestFilter {
    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("/login/sms".equals(request.getServletPath())) {
            try {
                validateCode(request);
            } catch (AuthenticationException e) {
                myAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void validateCode(HttpServletRequest request) throws ServletRequestBindingException {
        String smsCodeRequest = ServletRequestUtils.getStringParameter(request, "smsCode");
        String mobile = ServletRequestUtils.getStringParameter(request, "mobile");
        String smsCodeKey = SmsCode.SESSION_KEY + mobile;
        Object smsCodeSession = request.getSession().getAttribute(smsCodeKey);
        if (StringUtils.isBlank(mobile)) {
            throw new SmsCodeException("手机号不能为空！");
        }
        if (StringUtils.isBlank(smsCodeRequest)) {
            throw new SmsCodeException("短信验证码不能为空！");
        }
        if (smsCodeSession == null) {
            throw new SmsCodeException("短信验证码不存在！");
        }
        SmsCode smsCode = (SmsCode) smsCodeSession;
        if (LocalDateTime.now().isAfter(smsCode.getExpireTime())) {
            throw new SmsCodeException("短信验证码已过期！");
        }
        if (!StringUtils.equalsIgnoreCase(smsCodeRequest, smsCode.getCode())) {
            throw new SmsCodeException("短信验证码错误！");
        }
        request.getSession().removeAttribute(smsCodeKey);
    }
}
