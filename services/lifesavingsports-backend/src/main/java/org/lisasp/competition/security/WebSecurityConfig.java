package org.lisasp.competition.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Value("${app.development.test-ui-enabled:false}")
    private boolean testUIEnabled;

    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";

    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(r -> r
                .requestMatchers("/h2-console/").access((authentication, object) -> new AuthorizationDecision(testUIEnabled))
                .requestMatchers("/swagger-ui.html", "/swagger-ui/", "/v3/api-docs", "/v3/api-docs/").permitAll()
                .requestMatchers(HttpMethod.GET).permitAll()
                .requestMatchers(HttpMethod.PUT, "/result/import/").permitAll()
                .requestMatchers(HttpMethod.GET, "/actuator/").permitAll()
                .requestMatchers(HttpMethod.POST, "/competition/").authenticated()
                .anyRequest().authenticated());
        // http.oauth2ResourceServer(s -> s.jwt(j -> j.jwtAuthenticationConverter(jwtAuthConverter)));
        http.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();
    }
}
