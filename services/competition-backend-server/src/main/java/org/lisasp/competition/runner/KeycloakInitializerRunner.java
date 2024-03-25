package org.lisasp.competition.runner;

import jakarta.ws.rs.ProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.lisasp.competition.security.WebSecurityConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class KeycloakInitializerRunner implements CommandLineRunner {

    private final Keycloak keycloakAdmin;
    private final KeycloakAdminConfig config;

    @Override
    public void run(String... args) {
        try {
            log.info("Initializing '{}' realm in Keycloak ...", config.getRealmName());

            Optional<RealmRepresentation> representationOptional = keycloakAdmin.realms()
                    .findAll()
                    .stream()
                    .filter(r -> r.getRealm().equals(config.getRealmName()))
                    .findAny();
            if (representationOptional.isPresent()) {
                log.info("Realm already initialized...");
                return;
            }

            // Realm
            RealmRepresentation realmRepresentation = new RealmRepresentation();
            realmRepresentation.setRealm(config.getRealmName());
            realmRepresentation.setEnabled(true);
            realmRepresentation.setRegistrationAllowed(true);

            // Client
            ClientRepresentation clientRepresentation = new ClientRepresentation();
            clientRepresentation.setClientId(config.getClientId());
            clientRepresentation.setSecret(config.getSecret());
            clientRepresentation.setDirectAccessGrantsEnabled(true);
            clientRepresentation.setPublicClient(true);
            clientRepresentation.setRedirectUris(config.getValidRedirectUrls());
            clientRepresentation.setWebOrigins(config.getWebOrigins());
            // clientRepresentation.setDefaultRoles(new String[]{WebSecurityConfig.USER});
            realmRepresentation.setClients(List.of(clientRepresentation));

            // Users
            List<UserRepresentation> userRepresentations = config.getUsers().stream()
                    .map(userPass -> {
                        // User Credentials
                        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
                        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
                        credentialRepresentation.setValue(userPass.password());

                        // User
                        UserRepresentation userRepresentation = new UserRepresentation();
                        userRepresentation.setUsername(userPass.username());
                        userRepresentation.setEnabled(true);
                        userRepresentation.setCredentials(List.of(credentialRepresentation));
                        userRepresentation.setClientRoles(getClientRoles(userPass));

                        return userRepresentation;
                    })
                    .toList();
            realmRepresentation.setUsers(userRepresentations);

            // Create Realm
            keycloakAdmin.realms().create(realmRepresentation);

            // Testing
            UserPass admin = config.getUsers().get(0);
            log.info("Testing getting token for '{}' ...", admin.username());

            try (Keycloak keycloakRealmAccess = KeycloakBuilder.builder().serverUrl(config.getServerUrl())
                    .realm(config.getRealmName()).username(admin.username()).password(admin.password())
                    .clientId(config.getClientId()).clientSecret(config.getSecret()).build()) {

                log.info("'{}' token: {}", admin.username(), keycloakRealmAccess.tokenManager().grantToken().getToken());
                log.info("'{}' initialization completed successfully!", config.getRealmName());
            }
        } catch (ProcessingException pe) {
            log.info("Could not update realm", pe);
        }
    }

    private Map<String, List<String>> getClientRoles(UserPass userPass) {
        List<String> roles = new ArrayList<>();
        roles.add(WebSecurityConfig.USER);
        if ("admin".equals(userPass.username())) {
            roles.add(WebSecurityConfig.ADMIN);
        }
        return Map.of(config.getClientId(), roles);
    }
}
