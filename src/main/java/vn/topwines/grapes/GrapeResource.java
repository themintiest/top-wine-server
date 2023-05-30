package vn.topwines.grapes;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import vn.topwines.core.constants.Roles;
import vn.topwines.grapes.domain.CreateGrapeRQ;
import vn.topwines.grapes.domain.GetGrapeRQ;
import vn.topwines.grapes.domain.UpdateGrapeRQ;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/grapes")
@Tag(name = "Grape")
@ApplicationScoped
@RequiredArgsConstructor
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GrapeResource {

    private final GrapeService grapeService;

    @POST
    @RolesAllowed(Roles.ADMIN)
    @Path("/management")
    @Operation(summary = "Create grape", description = "Create new grape")
    public Response create(@RequestBody @Valid CreateGrapeRQ createGrapeRQ) {
        return Response.ok(grapeService.create(createGrapeRQ)).build();
    }

    @PUT
    @RolesAllowed(Roles.ADMIN)
    @Path("/management/{id}")
    @Operation(summary = "Update grape", description = "Update grape")
    public Response update(@PathParam("id") Long id, @Valid UpdateGrapeRQ updateGrapeRQ) {
        return Response.ok(grapeService.update(id, updateGrapeRQ)).build();
    }

    @DELETE
    @RolesAllowed(Roles.ADMIN)
    @Path("management/{id}")
    @Operation(summary = "Delete grape", description = "Delete grape")
    public void delete(@PathParam("id") Long id) {
        grapeService.delete(id);
    }

    @GET
    @RolesAllowed(Roles.ADMIN)
    @Path("/management/{id}")
    @Operation(summary = "Get grape by id", description = "Get grape by Id")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(grapeService.getGrapeRSById(id)).build();
    }

    @GET
    @PermitAll
    @Path("/{id}")
    @Operation(summary = "Get active grape by id", description = "Get active grape by Id")
    public Response getActiveById(@PathParam("id") Long id) {
        return Response.ok(grapeService.getActiveGrapeRSById(id)).build();
    }

    @GET
    @PermitAll
    @Path("/get-by-code")
    @Operation(summary = "Get grape by code", description = "Get grape by code")
    public Response getByCode(@QueryParam("code") String code) {
        return Response.ok(grapeService.getActiveGrapeRSByCode(code)).build();
    }

    @POST
    @RolesAllowed(Roles.ADMIN)
    @Path("/management/get-grapes")
    @Operation(summary = "Get grapes", description = "Get grapes")
    public Response getGrapes(@RequestBody GetGrapeRQ getGrapeRQ) {
        return Response.ok(grapeService.getGrapes(getGrapeRQ)).build();
    }

    @POST
    @PermitAll
    @Path("/get-grapes")
    @Operation(summary = "Get active grapes", description = "Get active grapes")
    public Response getActiveGrapes(@RequestBody GetGrapeRQ getGrapeRQ) {
        return Response.ok(grapeService.getActiveGrapes(getGrapeRQ)).build();
    }
}
