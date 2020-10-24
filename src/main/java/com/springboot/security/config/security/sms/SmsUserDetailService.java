package com.springboot.security.config.security.sms;

import com.springboot.security.config.security.MyUserDetail;
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
public class SmsUserDetailService implements UserDetailsService {
    @Resource
    private UserService userService;

    /**
     * @param mobile 手机号
     */
    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        User user = userService.getUserByMobile(mobile);
        if (user == null) {
            throw new UsernameNotFoundException("用户手机号" + mobile + "不存在");
        }
        MyUserDetail userDetails = new MyUserDetail();
        userDetails.setUser(user);
        return userDetails;
    }
}
