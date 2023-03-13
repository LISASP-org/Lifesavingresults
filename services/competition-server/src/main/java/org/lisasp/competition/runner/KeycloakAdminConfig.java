package org.lisasp.competition.runner;


import lombok.Getter;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Configuration
public class KeycloakAdminConfig {

    @Value("${app.keycloak.server-url}")
    private String serverUrl;
    @Value("${app.keycloak.realm}")
    private String realmName;
    @Value("${app.keycloak.client-id}")
    private String clientId;
    @Value("${app.keycloak.valid-redirect-urls}")
    private List<String> validRedirectUrls;
    @Value("${app.keycloak.web-origins}")
    private List<String> webOrigins;
    @Value("${app.keycloak.users}")
    private List<String> usernames;

    public List<UserPass> getUsers() {
        return usernames.stream().map(name -> new UserPass(name, name)).toList();
    }


    @Bean
    public Keycloak keycloakAdmin(@Value("${app.keycloak.server-url}") String keycloakServerUrl) {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakServerUrl)
                .realm("master")
                .username("keycloak")
                .password("keycloak")
                .clientId("admin-cli")
                .build();
    }
}
