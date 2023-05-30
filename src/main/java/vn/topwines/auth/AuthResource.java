package vn.topwines.auth;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import vn.topwines.auth.domain.Session;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

@Path("/sessions")
@Tag(name = "Authentication")
@ApplicationScoped
@RequiredArgsConstructor
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final AuthService authService;

    @POST
    @PermitAll
    @Path("/login")
    @Operation(summary = "Login", description = "Login and get Bearer jwt token.")
    public Session login(@Context SecurityContext ctx, LoginRequest request) {
        return authService.authenticate(request.getEmail(), request.getPassword());
    }

    @GET
    @Path("/logout")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public String logout(@Context SecurityContext ctx) {
        return "OK";
    }
}
