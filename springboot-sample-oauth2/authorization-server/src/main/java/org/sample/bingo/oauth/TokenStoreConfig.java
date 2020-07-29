package org.sample.bingo.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
/**本例中没有使用JWT*/
public class TokenStoreConfig {
    @Bean
    /** 如果资源服务器和认证服务器不在同一个应用中, 应告知资源服务器令牌解析及存储和秘钥的具体方式 - 与认证服务器应保持一致 */
    /** 三种实现: InMemoryTokenStore, JdbcTokenStore, JwtTokenStore
     *    其中: /JwtTokenStore并不存储令牌, 对于撤销已授权令牌会很困难, 通常用以处理一个生命周期较短及撤销刷新令牌 */
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        /** .setSigningKey表示签字秘钥, 使用的是对称秘钥加密方式 */
        converter.setSigningKey("Key"); // 签字秘钥
        /** 另外一种方式是使用非对称秘钥加密.setVerifierKey(PublicKey);
         *  - 公共秘钥有两种方式获取
         *  1.从本地目录xxx.txt中读取
         *  2.@Autowired OAuth2ResourceServerProperties.getJwt().getKeyUri()可以获取认证服务器上获得该公钥的URL, 通过httpClient获取 */
        return converter;
    }
}
