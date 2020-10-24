package com.springboot.security.config.security;

import com.springboot.security.config.security.sms.SmsAuthenticationConfig;
import com.springboot.security.config.security.sms.SmsCodeFilter;
import com.springboot.security.config.verification.ImageCodeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private MyAuthenticationSucessHandler myAuthenticationSucessHandler;
    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Resource
    private DataSource dataSource;
    @Resource
    private MyUserDetailService myUserDetailService;
    @Resource
    private ImageCodeFilter imageCodeFilter;
    @Resource
    private SmsCodeFilter smsCodeFilter;
    @Resource
    private SmsAuthenticationConfig smsAuthenticationConfig;

    /**
     * 用户密码登录的密码加密永久
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置"记住我"的token 持久化
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        //数据源
        jdbcTokenRepository.setDataSource(dataSource);
        //项目启动是是否自动创建token表：true 自动创建，false 需要用户手动创建
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    /**
     * spring security 默认的 DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(myUserDetailService);
        return daoAuthenticationProvider;
    }

    /**
     * 配置多个Provider： daoAuthenticationProvider , smsAuthenticationProvider
     */
    @Override
    protected AuthenticationManager authenticationManager() {
        ProviderManager authenticationManager = new ProviderManager(Arrays.asList(
                daoAuthenticationProvider(), smsAuthenticationConfig.smsAuthenticationProvider()));
        authenticationManager.setEraseCredentialsAfterAuthentication(false);
        return authenticationManager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //图片验证码过滤器
                .addFilterBefore(imageCodeFilter, UsernamePasswordAuthenticationFilter.class)
                //短信验证码过滤器
                .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin() // 表单登录
                // 指定了跳转到登录页面的请求UR
                .loginPage("/authentication/login")
                // 对应登录页面form表单的action="/login"
                .loginProcessingUrl("/login")
                // 指定登录成功处理器
                .successHandler(myAuthenticationSucessHandler)
                // 指定登录失败处理器
                .failureHandler(myAuthenticationFailureHandler)
                .and()
                .logout()
                //自定义退出成功的url
                .logoutSuccessUrl("/index.html")
                .and()
                .rememberMe()
                // 配置 token 持久化仓库
                .tokenRepository(persistentTokenRepository())
                // remember 过期时间，单为秒
                .tokenValiditySeconds(3600)
                // 处理自动登录逻辑
                .userDetailsService(myUserDetailService)
                .and()
                .authorizeRequests() // 授权配置
                .antMatchers("/swagger*/**", "/v2/api-docs", "/webjars/springfox-swagger*/**",
                        "/authentication/login", "static/**", "/login.html", "/css/**", "/logout",
                        "/image/code", "/loginSms.html", "/sms/code", "/login/sms").permitAll()
                .anyRequest()  // 所有请求
                .authenticated() // 都需要认证
                .and().csrf().disable()
                // 将短信验证码认证配置加到 Spring Security 中;
                .apply(smsAuthenticationConfig);
    }
}
