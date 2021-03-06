package ru.bmstu.cp.rsoi.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value( "${service.session.host}" )
    private String sessionServiceHost;

    @Value( "${service.session.port}" )
    private Integer sessionServicePort;

    @Value( "${service.session.clientId}" )
    private String sessionServiceClientId;

    @Value( "${service.session.secret}" )
    private String sessionServiceSecret;

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .requestMatcher(request -> {
                    String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
                    return auth == null || auth.startsWith("Bearer");
                })
                .authorizeRequests()
                .antMatchers("/**/protected/**").hasAnyRole("ADMIN", "USER", "OPERATOR", "EXPERT")
                .antMatchers("/**/private/**").denyAll()
                .anyRequest().permitAll()
                .and()
                .httpBasic()
                .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

    @Bean
    @Primary
    public RemoteTokenServices tokenServices() {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setClientId(sessionServiceClientId);
        tokenServices.setClientSecret(sessionServiceSecret);
        tokenServices.setCheckTokenEndpointUrl(String.format("http://%s:%d/oauth/check_token", sessionServiceHost, sessionServicePort));
        return tokenServices;
    }
}