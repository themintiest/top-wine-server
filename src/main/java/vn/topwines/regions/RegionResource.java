package vn.topwines.regions;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import vn.topwines.core.constants.Roles;
import vn.topwines.regions.domain.CreateRegionRQ;
import vn.topwines.regions.domain.GetRegionRQ;
import vn.topwines.regions.domain.UpdateRegionRQ;

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

@Path("/regions")
@Tag(name = "Region")
@ApplicationScoped
@RequiredArgsConstructor
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RegionResource {

    private final RegionService regionService;

    @POST
    @RolesAllowed(Roles.ADMIN)
    @Path("/management")
    @Operation(summary = "Create brand", description = "Create new brand")
    public Response create(@RequestBody @Valid CreateRegionRQ createRegionRQ) {
        return Response.ok(regionService.create(createRegionRQ)).build();
    }

    @PUT
    @RolesAllowed(Roles.ADMIN)
    @Path("/management/{id}")
    @Operation(summary = "Update brand", description = "Update brand")
    public Response update(@PathParam("id") Long id, @Valid UpdateRegionRQ updateRegionRQ) {
        return Response.ok(regionService.update(id, updateRegionRQ)).build();
    }

    @DELETE
    @RolesAllowed(Roles.ADMIN)
    @Path("management/{id}")
    @Operation(summary = "Delete brand", description = "Delete brand")
    public void delete(@PathParam("id") Long id) {
        regionService.delete(id);
    }

    @GET
    @RolesAllowed(Roles.ADMIN)
    @Path("/management/{id}")
    @Operation(summary = "Get brand by id", description = "Get brand by Id")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(regionService.getRegionRSById(id)).build();
    }

    @GET
    @PermitAll
    @Path("/{id}")
    @Operation(summary = "Get active brand by id", description = "Get active brand by Id")
    public Response getActiveById(@PathParam("id") Long id) {
        return Response.ok(regionService.getActiveRegionRSById(id)).build();
    }

    @GET
    @PermitAll
    @Path("/get-by-code")
    @Operation(summary = "Get brand by code", description = "Get brand by code")
    public Response getByCode(@QueryParam("code") String code) {
        return Response.ok(regionService.getActiveRegionRSByCode(code)).build();
    }

    @POST
    @RolesAllowed(Roles.ADMIN)
    @Path("/management/get-regions")
    @Operation(summary = "Get regions", description = "Get regions")
    public Response getRegions(@RequestBody GetRegionRQ getRegionRQ) {
        return Response.ok(regionService.getRegions(getRegionRQ)).build();
    }

    @POST
    @PermitAll
    @Path("/get-regions")
    @Operation(summary = "Get active regions", description = "Get active regions")
    public Response getActiveRegions(@RequestBody GetRegionRQ getRegionRQ) {
        return Response.ok(regionService.getActiveRegions(getRegionRQ)).build();
    }
}
