package org.lisasp.competition.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.authorization.service.users.UserRole;
import org.lisasp.competition.security.Rights;
import org.lisasp.competition.security.CompetitionServerAuthorizationService;
import org.lisasp.competition.security.Roles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("authorization")
@RequiredArgsConstructor
public class AuthorizationController {

    private final CompetitionServerAuthorizationService service;

    @ApiResponses({@ApiResponse(responseCode = "200", useReturnTypeSchema = true), @ApiResponse(responseCode = "403", description = "user is not authorized"), @ApiResponse(responseCode = "404", description = "role not found")})
    @PutMapping("user/{username}/role/{role}/competition/{competitionId}")
    @Transactional
    public void grantAccess(@PathVariable("username") String username, @PathVariable("role") String role, @PathVariable("competitionId") String competitionId) {
        service.addUserRole(username, Roles.fromString(role), competitionId);
    }

    @ApiResponses({@ApiResponse(responseCode = "200", useReturnTypeSchema = true), @ApiResponse(responseCode = "403", description = "user is not authorized")})
    @DeleteMapping("user/{username}/role/{role}/competition/{competitionId}")
    @Transactional
    public void revokeAccess(@PathVariable("username") String username, @PathVariable("role") String role, @PathVariable("competitionId") String competitionId) {
        service.removeUserRole(username, Roles.fromString(role), competitionId);
    }

    @ApiResponses({@ApiResponse(responseCode = "200", useReturnTypeSchema = true), @ApiResponse(responseCode = "403", description = "user is not authorized")})
    @GetMapping("me/role")
    @Transactional
    public UserRole[] findUserRoles() {
        return service.findRoles();
    }

    @ApiResponses({@ApiResponse(responseCode = "200", useReturnTypeSchema = true), @ApiResponse(responseCode = "403", description = "user is not authorized")})
    @GetMapping("user/{username}/role")
    @Transactional
    public UserRole[] findUserRoles(@PathVariable("username") String username) {
        return service.findRoles(username);
    }
}
