package vn.topwines.brands;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import vn.topwines.brands.domain.CreateBrandRQ;
import vn.topwines.brands.domain.GetBrandRQ;
import vn.topwines.brands.domain.UpdateBrandRQ;
import vn.topwines.core.constants.Roles;

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

@Path("/brands")
@Tag(name = "Brand")
@ApplicationScoped
@RequiredArgsConstructor
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BrandResource {

    private final BrandService brandService;

    @POST
    @RolesAllowed(Roles.ADMIN)
    @Path("/management")
    @Operation(summary = "Create brand", description = "Create new brand")
    public Response create(@RequestBody @Valid CreateBrandRQ createBrandRQ) {
        return Response.ok(brandService.create(createBrandRQ)).build();
    }

    @PUT
    @RolesAllowed(Roles.ADMIN)
    @Path("/management/{id}")
    @Operation(summary = "Update brand", description = "Update brand")
    public Response update(@PathParam("id") Long id, @Valid UpdateBrandRQ updateBrandRQ) {
        return Response.ok(brandService.update(id, updateBrandRQ)).build();
    }

    @DELETE
    @RolesAllowed(Roles.ADMIN)
    @Path("management/{id}")
    @Operation(summary = "Delete brand", description = "Delete brand")
    public void delete(@PathParam("id") Long id) {
        brandService.delete(id);
    }

    @GET
    @RolesAllowed(Roles.ADMIN)
    @Path("/management/{id}")
    @Operation(summary = "Get brand by id", description = "Get brand by Id")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(brandService.getBrandRSById(id)).build();
    }

    @GET
    @PermitAll
    @Path("/{id}")
    @Operation(summary = "Get active brand by id", description = "Get active brand by Id")
    public Response getActiveById(@PathParam("id") Long id) {
        return Response.ok(brandService.getActiveBrandRSById(id)).build();
    }

    @GET
    @PermitAll
    @Path("/get-by-code")
    @Operation(summary = "Get brand by code", description = "Get brand by code")
    public Response getByCode(@QueryParam("code") String code) {
        return Response.ok(brandService.getActiveBrandRSByCode(code)).build();
    }

    @POST
    @RolesAllowed(Roles.ADMIN)
    @Path("/management/get-brands")
    @Operation(summary = "Get brands", description = "Get brands")
    public Response getBrands(@RequestBody GetBrandRQ getBrandRQ) {
        return Response.ok(brandService.getBrands(getBrandRQ)).build();
    }

    @POST
    @PermitAll
    @Path("/get-brands")
    @Operation(summary = "Get active brands", description = "Get active brands")
    public Response getActiveBrands(@RequestBody GetBrandRQ getBrandRQ) {
        return Response.ok(brandService.getActiveBrands(getBrandRQ)).build();
    }
}
