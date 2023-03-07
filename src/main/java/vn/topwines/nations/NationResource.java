package vn.topwines.nations;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import vn.topwines.core.constants.Roles;
import vn.topwines.nations.domain.CreateNationRQ;
import vn.topwines.nations.domain.GetNationRQ;
import vn.topwines.nations.domain.UpdateNationRQ;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
