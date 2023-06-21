package org.lisasp.competition.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Value("${app.development.test-ui-enabled:false}")
    private boolean testUIEnabled;

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers("/web/**").access((authentication, object) -> new AuthorizationDecision(testUIEnabled))
            .requestMatchers("/h2-console/**").access((authentication, object) -> new AuthorizationDecision(testUIEnabled))
            .requestMatchers(HttpMethod.GET, "", "/**").permitAll()
            .requestMatchers(HttpMethod.PUT, "/result/import/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
            .requestMatchers("", "/**").hasAnyRole(ADMIN, USER)
            .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
            .anyRequest().authenticated();
        http.oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthConverter);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors().and().csrf().disable();
        http.headers().frameOptions().sameOrigin();
        return http.build();
    }
}
