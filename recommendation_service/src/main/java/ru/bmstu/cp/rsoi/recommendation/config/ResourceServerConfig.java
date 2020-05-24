package ru.bmstu.cp.rsoi.recommendation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
                .authorizeRequests()
                .antMatchers("/api/**/protected/**").hasAnyRole("USER", "EXPERT", "OPERATOR", "ADMIN", "INTERNAL_CLIENT")
                .antMatchers("/api/**/public/**").permitAll()
                .antMatchers("/api/**/private/**").hasRole("INTERNAL_CLIENT")
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