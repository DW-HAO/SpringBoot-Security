package com.luxiaobu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// AOP:拦截器
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // 链式编程
    // 权限
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 首页所有人可以访问 功能页只有对应权限的人才能访问
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("vip1")
                .antMatchers("/level2/**").hasRole("vip2")
                .antMatchers("/level3/**").hasRole("vip3");

        // 没有权限默认跳转到登录页
        //定制登录页
        http.formLogin().loginPage("/toLogin").usernameParameter("user").passwordParameter("pwd").loginProcessingUrl("/login");
        http.csrf().disable();// 关闭csrf功能
        // 开启注销功能 跳转到首页
        http.logout().logoutSuccessUrl("/");
        // 开启记住我功能
        // 自定义
        http.rememberMe().rememberMeParameter("rememberMe");
    }

    // 认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("bigFish").password(new BCryptPasswordEncoder().encode("0000")).roles("vip2", "vip1")
                .and()
                .withUser("root").password(new BCryptPasswordEncoder().encode("0000")).roles("vip1", "vip2", "vip3");
    }

}

