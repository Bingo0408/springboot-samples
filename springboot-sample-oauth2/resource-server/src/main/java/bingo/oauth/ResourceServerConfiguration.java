package bingo.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    /** ResourceServerSecurityConfigurer的可配属性
     *  - tokenServices: ResourceServerTokenServices的实例，实现令牌业务逻辑服务 - Bean实例化后会自动注入
     *    /DefaultTokenServices: 在资源服务器本地配置令牌存储、解码、解析
     *    /RemoteTokenServices: 每次都通过HTTP请求授权服务器端点/oauth/check_token -- 独立部署ResourceServer时使用
     *  - tokenExtractor: 令牌提取器用以提取请求中的令牌
     *  - resourceId: 本资源服务的ID, 可选属性, 推荐设置并在授权服务中进行校验 */
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("resource_id_1");
        /**由于ResourceServerTokenServiceConfig.java中自动装配, 相当于本处自动注入了.tokenServices(RemoteTokenServices) */
    }
    @Override
    /**本方法可以代替SpringSecurity原生的访问控制器配置资源访问规则*/
    public void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        /**所有请求都需要经过OAuth鉴权, 不同的url需要有不同的oauth授权*/
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/url1").access("#oauth2.hasScope('read')")
                .antMatchers(HttpMethod.GET, "/url2").access("#oauth2.hasScope('write')")
                .anyRequest().authenticated();
    }
}
