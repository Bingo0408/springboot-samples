package org.sample.bingo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** 0.模拟优酷需要QQ授权的功能
 *  1.pom.xml
 *    导入spring-boot-starter-security, spring-security-oauth2
 *  2.为QQ添加用户: user_1:test => Authorization: Basic dXNlcl8xOnRlc3Q=
 *    假设优酷已经向QQ申请了OAuth的接入认证信息: client_1:test => Authorization: Basic Y2xpZW50XzE6dGVzdA==
 *  3-1.开启QQ的用户认证: 见@EnableWebSecurity
 *  3-2.开启QQ作为OAuth2的认证Server, 见@EnableAuthorizationServer
 *  ====================================验证====================================
 *  4-1.授权码方式, QQ此时需要用户登陆QQ系统同意授予令牌 - 可以只勾选同意授予Read授权
 *      http://localhost:8080/oauth/authorize?response_type=code&client_id=client_1&redirect_uri=http://localhost:8080/index (Authorization: Basic dXNlcl8xOnRlc3Q=)
 *      重定到优酷(使用/index作为代替)后在URL中截获令牌填入如下URL, 模拟优酷的后台申请OAuth令牌
 *      http://localhost:8080/oauth/token?grant_type=authorization_code&client=client_1&client_secret=test&code={code}&redirect_uri=http://localhost:8080/index (Authorization: Basic Y2xpZW50XzE6dGVzdA==)
 *  4-2.简化模式
 *      http://localhost:8080/oauth/authorize?response_type=token&client_id=client_1&redirect_uri=http://localhost:8080/index
 *  5.系统返回OAuth令牌
 *  注意: 同一个用户即使多次多种方式申请也仅能获得一个令牌 */
@SpringBootApplication
@RestController
public class SampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleApplication.class, args);
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @GetMapping(path = {"/", "/index"})
    public String index() {
        return "认证服务器>首页。";
    }
}

