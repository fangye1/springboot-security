package com.springboot.security.config.security;

import com.springboot.security.po.User;
import com.springboot.security.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;

/**
 * @author qizenan
 */
@Configuration
public class MyUserDetailService implements UserDetailsService {
    @Resource
    private UserService userService;

    /**
     * @param username 用户名称
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户" + username + "不存在");
        }
        MyUserDetail userDetails = new MyUserDetail();
        userDetails.setUser(user);
        return userDetails;
    }
}
