package org.bingo.sample.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**指定用户认证的实现类(接口为约定的固定值)*/
        auth.userDetailsService(userDetailsService);
    }

    /** 表单登录: 使用SpringSecurity默认的表单登录页面和登录端点/login进行登录, 其它:自定义处理login的url, 指定login页面上的用户名密码码的字段名
     *  退出登录: 使用默认的退出登录端点/logout退出登录
     *  记住我: 使用默认的"记住我"功能, 有效期30分钟
     *  Access控制: 除了url1和url2外, 其它请求都要求登陆
     *             /admin/**必须有ADMIN权限, /user/**必须有user权限
     *  - 注意: Controller中也有权限控制, 两者同时发生时, 按照较小权限处理 */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().defaultSuccessUrl("/index", false).permitAll();
        http.logout().permitAll();
        http.rememberMe().tokenValiditySeconds(1800);
        http.authorizeRequests().antMatchers("/url1", "/url2").permitAll()
                                .antMatchers("/admin/**").hasRole("ADMIN") // 这里ROLE不需要加ROLE, SpringSecurity会自动增加
                                .antMatchers("/user/**").hasRole("USER")
                                .anyRequest().authenticated(); // authorizeRequests应该贯连写, 不应分开写
        /**其它功能, 禁用表单登陆, csrf, cors, basic认证*/
    }

    /**HardCode用户名和密码以及权限
     * user-test拥有USER权限
     * admin-test拥有ADMIN,USER权限*/
    @Service
    class UserDetailsServiceImpl implements UserDetailsService {

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            String passowrd = passwordEncoder.encode("test");
            if ("user".equals(username)) {
                List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
                return new User(username, passowrd, authorities);
            } else if ("admin".equals(username)) {
                List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
                return new User(username, passowrd, authorities);
            } else if ("temp".equals(username)) {
                return new User(username, passowrd, new ArrayList());
            } else {
                throw new UsernameNotFoundException("User Not Found!");
            }
        }
    }
}
