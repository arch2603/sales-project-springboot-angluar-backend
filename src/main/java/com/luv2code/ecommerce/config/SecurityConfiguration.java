package com.luv2code.ecommerce.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //protect endpoint /api/orders
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/api/orders/**")
                        .authenticated().anyRequest().permitAll())
                .oauth2ResourceServer()
                .jwt();
        http.cors();

        //add content negotiation strategy
        http.setSharedObject(ContentNegotiationStrategy.class, new HeaderContentNegotiationStrategy());

        //force a non-empty response body for 401s to make the response more friendly
        Okta.configureResourceServer401ResponseBody(http);

        //disable CSRF since we are not using Cookies for session tracking
        http.csrf().disable();
        return http.build();
    }
}
