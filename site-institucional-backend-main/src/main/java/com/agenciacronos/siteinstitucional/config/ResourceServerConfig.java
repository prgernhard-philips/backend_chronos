package com.agenciacronos.siteinstitucional.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/v1/users", "/v1/integrants/**", "/v1/posts/**", "/v1/services/**", "/v2/api-docs",
                            "/v3/api-docs",
                            "/swagger-resources/**",
                            "/swagger-ui/**").permitAll()
//                    .antMatchers("/v1/integrants/**").authenticated()
                    .anyRequest().denyAll();
    }
}
