package com.springboot.security.config.verification;

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
public class ImageCodeFilter extends OncePerRequestFilter {
    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("/login".equals(request.getServletPath())) {
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
        String imageCodeRequest = ServletRequestUtils.getStringParameter(request, "imageCode");
        Object imageCodeSession = request.getSession().getAttribute(ImageCode.SESSION_KEY);
        if (StringUtils.isBlank(imageCodeRequest)) {
            throw new ImageCodeException("图片验证码不能为空！");
        }
        if (imageCodeSession == null) {
            throw new ImageCodeException("图片验证码不存在！");
        }
        ImageCode imageCode = (ImageCode) imageCodeSession;
        if (LocalDateTime.now().isAfter(imageCode.getExpireTime())) {
            throw new ImageCodeException("图片验证码已过期！");
        }
        if (!StringUtils.equalsIgnoreCase(imageCodeRequest, imageCode.getCode())) {
            throw new ImageCodeException("图片验证码错误！");
        }
        request.getSession().removeAttribute(ImageCode.SESSION_KEY);
    }
}
