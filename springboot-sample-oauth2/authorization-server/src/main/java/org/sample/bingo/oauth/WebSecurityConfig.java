package org.sample.bingo.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    /**内置QQ系统的用户认证信息*/
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder);
        auth.inMemoryAuthentication().withUser("user_1").password(passwordEncoder.encode("test")).roles("USER");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /** /oauth开头及login请求不需要认证 */
        http.authorizeRequests().antMatchers("/oauth/**").permitAll();
        http.formLogin().permitAll();
        http.httpBasic();
    }
}
