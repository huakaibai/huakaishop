package com.zhibinwang.oauth.config;

import com.zhibinwang.oauth.constant.SecurityConstants;
import com.zhibinwang.oauth.service.HkClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.sql.DataSource;

/**
 * @author zhibin.wang
 * @create 2019-12-04
 * @desc 认证服务器设置
 **/

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private  DataSource dataSource;
    @Autowired
    private  UserDetailsService userDetailsService;
    @Autowired
    private  AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


    /**
     * 用来配置客户端详情信息，一般使用数据库来存储或读取应用配置的详情信息（client_id ，client_secret，redirect_uri 等配置信息）
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        //https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql
        // 我们这里做了重写
         HkClientDetailsService clientDetailsService = new HkClientDetailsService(dataSource);
        clientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
        clientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * 用来配置令牌端点(Token Endpoint)的安全与权限访问。
     * @param security
     * @throws Exception
     */

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .allowFormAuthenticationForClients()
                .checkTokenAccess("permitAll()");
    }


    /**
     *
     * @param endpoints
     * @throws Exception
     * @desc 用来配置授权以及令牌（Token）的访问端点和令牌服务（比如：配置令牌的签名与存储方式）
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .tokenStore(tokenStore())
               // .tokenEnhancer(tokenEnhancer())
               // .userDetailsService(userDetailsService) refresh_token 需要 UserDetailsService is required
                .authenticationManager(authenticationManager);
              //  .reuseRefreshTokens(false);
                //.exceptionTranslator(new PigWebResponseExceptionTranslator());
    }


    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        tokenStore.setPrefix(SecurityConstants.PROJECT_PREFIX + SecurityConstants.OAUTH_PREFIX);
        return tokenStore;
    }

    @Bean
    AuthenticationManager authenticationManager() {
        AuthenticationManager authenticationManager = authentication -> daoAuhthenticationProvider().authenticate(authentication);
        return authenticationManager;
    }
    // 设置添加用户信息,正常应该从数据库中读取
    @Bean
    UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
        userDetailsService.createUser(User.withUsername("user_1").password(passwordEncoder().encode("123456"))
                .authorities("ROLE_USER").build());
        userDetailsService.createUser(User.withUsername("user_2").password(passwordEncoder().encode("1234567"))
                .authorities("ROLE_USER").build());
        return userDetailsService;
    }


    @Bean
    public AuthenticationProvider daoAuhthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        // 加密方式
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder;
    }


/*
   存储token对应的信息，暂时不需要
   @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            final Map<String, Object> additionalInfo = new HashMap<>(1);
            PigUser pigUser = (PigUser) authentication.getUserAuthentication().getPrincipal();
            additionalInfo.put(SecurityConstants.DETAILS_LICENSE, SecurityConstants.PROJECT_LICENSE);
            additionalInfo.put(SecurityConstants.DETAILS_USER_ID, pigUser.getId());
            additionalInfo.put(SecurityConstants.DETAILS_USERNAME, pigUser.getUsername());
            additionalInfo.put(SecurityConstants.DETAILS_DEPT_ID, pigUser.getDeptId());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }*/
}
