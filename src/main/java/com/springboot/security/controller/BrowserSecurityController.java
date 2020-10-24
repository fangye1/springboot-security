package com.springboot.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qizenan
 */
@Slf4j
@RestController
public class BrowserSecurityController {
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private List<String> loginPaths = new ArrayList<>();

    @PostConstruct
    public void init() {
        loginPaths.add("/");
        loginPaths.add("/authentication/login");
    }

    @GetMapping("/authentication/login")
    public Map<String, String> loginPage(HttpServletRequest request, HttpServletResponse response) {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String targetUrl = savedRequest == null ? "" : savedRequest.getRedirectUrl();
        if (checkToLogin(targetUrl) || StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
            try {
                redirectStrategy.sendRedirect(request, response, "/login.html");
            } catch (IOException e) {
                log.error("页面跳转异常", e);
            }
        }
        Map<String, String> data = new HashMap<>();
        data.put("code", "500");
        data.put("message", "请先登录");
        return data;
    }

    private boolean checkToLogin(String urlPath) {
        if (StringUtils.isBlank(urlPath)) {
            return true;
        }
        try {
            URL url = new URL(urlPath);
            String path = url.getPath();
            return loginPaths.contains(path);
        } catch (MalformedURLException e) {
            log.info("urlPath={} 异常 ", e);
        }
        return false;
    }
}
