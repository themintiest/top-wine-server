package vn.topwines.users;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import vn.topwines.core.constants.Roles;
import vn.topwines.core.query.PageQueryBuilder;
import vn.topwines.core.query.PageQueryParams;
import vn.topwines.core.query.Pageable;
import vn.topwines.users.domain.GrantRoleRQ;
import vn.topwines.users.domain.RegisterRequest;
import vn.topwines.users.domain.UpdateProfileRequest;
import vn.topwines.users.domain.UserDto;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/users")
@Tag(name = "User")
@ApplicationScoped
@RequiredArgsConstructor
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserService userService;

    @POST
    @PermitAll
    @Path("/register")
    @Operation(summary = "Register", description = "Create new account")
    public UserDto register(RegisterRequest registerRequest) {
        return userService.createUser(registerRequest);
    }

    @GET
    @RolesAllowed({Roles.USER})
    @Path("/current")
    @Operation(summary = "Get current", description = "Get current user info")
    public UserDto getCurrent() {
        return userService.getCurrentLoginUser();
    }

    @GET
    @PermitAll
    @Path("/{id}")
    @Operation(summary = "Get user info by id", description = "Get user info by id")
    public UserDto getUserById(@PathParam("id") Long id) {
        return userService.getUserRSById(id);
    }

    @PUT
    @RolesAllowed({Roles.ADMIN})
    @Path("/{id}/roles")
    @Operation(summary = "Grant role for user", description = "Grant role for user")
    public UserDto grantRole(@PathParam("id") Long id, @Valid GrantRoleRQ grantRoleRQ) {
        return userService.grantRoleForUser(id, grantRoleRQ);
    }

    @PUT
    @RolesAllowed({Roles.USER})
    @Operation(summary = "Update profile", description = "Update profile")
    public UserDto updateProfile(@Valid UpdateProfileRequest updateProfileRequest) {
        return userService.updateMyProfile(updateProfileRequest);
    }

    @GET
    @PermitAll
    @Path("/search")
    @Operation(summary = "Search", description = "Search users")
    public Pageable<UserDto> search(@BeanParam PageQueryParams pageQueryParams) {
        return userService.search(PageQueryBuilder.of(pageQueryParams));
    }

}
