package vn.topwines.nations;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import vn.topwines.core.constants.Roles;
import vn.topwines.nations.domain.CreateNationRQ;
import vn.topwines.nations.domain.GetNationRQ;
import vn.topwines.nations.domain.UpdateNationRQ;

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

@Path("/nations")
@Tag(name = "Nation")
@ApplicationScoped
@RequiredArgsConstructor
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NationResource {

    private final NationService nationService;

    @POST
    @RolesAllowed(Roles.ADMIN)
    @Path("/management")
    @Operation(summary = "Create nation", description = "Create new nation")
    public Response create(@RequestBody @Valid CreateNationRQ createNationRQ) {
        return Response.ok(nationService.create(createNationRQ)).build();
    }

    @PUT
    @RolesAllowed(Roles.ADMIN)
    @Path("/management/{id}")
    @Operation(summary = "Update nation", description = "Update nation")
    public Response update(@PathParam("id") Long id, @Valid UpdateNationRQ updateNationRQ) {
        return Response.ok(nationService.update(id, updateNationRQ)).build();
    }

    @DELETE
    @RolesAllowed(Roles.ADMIN)
    @Path("management/{id}")
    @Operation(summary = "Delete nation", description = "Delete nation")
    public void delete(@PathParam("id") Long id) {
        nationService.delete(id);
    }

    @GET
    @RolesAllowed(Roles.ADMIN)
    @Path("/management/{id}")
    @Operation(summary = "Get nation by id", description = "Get nation by Id")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(nationService.getNationRSById(id)).build();
    }

    @GET
    @PermitAll
    @Path("/{id}")
    @Operation(summary = "Get active nation by id", description = "Get active nation by Id")
    public Response getActiveById(@PathParam("id") Long id) {
        return Response.ok(nationService.getActiveNationRSById(id)).build();
    }

    @GET
    @PermitAll
    @Path("/get-by-code")
    @Operation(summary = "Get nation by code", description = "Get nation by code")
    public Response getByCode(@QueryParam("code") String code) {
        return Response.ok(nationService.getActiveNationRSByCode(code)).build();
    }

    @POST
    @RolesAllowed(Roles.ADMIN)
    @Path("/management/get-nations")
    @Operation(summary = "Get nations", description = "Get nations")
    public Response getNations(@RequestBody GetNationRQ getNationRQ) {
        return Response.ok(nationService.getNations(getNationRQ)).build();
    }

    @POST
    @PermitAll
    @Path("/get-nations")
    @Operation(summary = "Get active nations", description = "Get active nations")
    public Response getActiveNations(@RequestBody GetNationRQ getNationRQ) {
        return Response.ok(nationService.getActiveNations(getNationRQ)).build();
    }
}
