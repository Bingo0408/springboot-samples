package org.sample.bingo.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    /** 配置Client认证信息, 这些信息的收集应另建应用提供申请, 此处为硬编码
     *  - 包括: {.withClient(): ClientID, 类比为用户名
     *          .secret(): 客户端秘钥, 类比为密码
     *          .authorizedGrantTypes(): 针对该Client所支持的授权类型
     *          .redirectUris(): 在authorization_code与implicit授权方式时分发Token的回调地址(注意:在申请Token的URL中的该地址必须严格一致)
     *          .autoApprove(): 自动许可Client的许可请求, 不会跳出网页要求认证
     *          .scopes(): 授权范围
     *          .accessTokenValiditySeconds(): Token有效期(-1:永久有效)
     *          .refreshTokenValiditySeconds(): 刷新Token的有效期(一般应该由服务器端自定义, 不应该交由客户端定义)
     *          其他字段参考: ClientBuilder.java }
     *  - 附录: 4种授权类型("authorization_code", "implicit", "refresh_token", "client_credentials", "password") */
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        /**本例使用内存存储方式, SpringSecurityOAuth提供了默认的数据库实现*/
        clients.inMemory()
                .withClient("client_1")
                .secret(passwordEncoder.encode("test"))
                .authorizedGrantTypes("authorization_code", "implicit", "refresh_token", "client_credentials", "password")
                .scopes("read", "write", "mobile")
                .redirectUris("http://localhost:8080/index"); //正常情况, 此处应该为第三方应用自身提供的回调接口, 如http://www.youku.com/oauth/callback
    }
    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        /**允许OAuth表单授权, 仅仅在autoApprove设置为false时起作用*/
        oauthServer.allowFormAuthenticationForClients();
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }
    @Override
    /** AuthorizationServerEndpointsConfigurer的可配属性
     *  - AuthenticationManager: 认证管理器, 如果选择了password的授权方式时, 需要注入该对象
     *  - UserDetailsService: 自定义用户认证接口
     *  - TokenEnhancer: 自定义令牌策略, 在令牌被AuthorizationServerTokenServices存储之前增强策略, 有两个实现类
     *    /JwtAccessTokenConverter: 用于令牌 JWT 编码与解码
     *    /TokenEnhancerChain: 令牌链，可以存放多个令牌，并循环的遍历令牌并将结果传递给下一个令牌
     *  - TokenStore: 用于定义 Token 如何存储 */
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }
}
