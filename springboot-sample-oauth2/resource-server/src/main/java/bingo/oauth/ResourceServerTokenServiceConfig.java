package bingo.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
public class ResourceServerTokenServiceConfig {
    @Bean
    /**RemoteTokenServices每次都通过HTTP请求授权服务器端点/oauth/check_token验证令牌是否有效 - 独立部署ResourceServer时使用*/
    public ResourceServerTokenServices tokenService() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl("http://localhost:8080/oauth/check_token");
        tokenService.setClientId("client_1");
        tokenService.setClientSecret("test");
        return tokenService;
    }
}
