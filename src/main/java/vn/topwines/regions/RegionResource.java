package vn.topwines.regions;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import vn.topwines.core.constants.Roles;
import vn.topwines.regions.domain.CreateRegionRQ;
import vn.topwines.regions.domain.GetRegionRQ;
import vn.topwines.regions.domain.UpdateRegionRQ;

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
