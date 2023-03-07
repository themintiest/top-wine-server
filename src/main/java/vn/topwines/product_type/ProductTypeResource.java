package vn.topwines.product_type;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import vn.topwines.core.constants.Roles;
import vn.topwines.product_type.domain.CreateProductTypeRQ;
import vn.topwines.product_type.domain.GetProductTypeRQ;
import vn.topwines.product_type.domain.UpdateProductTypeRQ;

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

@Path("/product_types")
@Tag(name = "Product Type")
@ApplicationScoped
@RequiredArgsConstructor
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductTypeResource {

    private final ProductTypeService productTypeService;

    @POST
    @RolesAllowed(Roles.ADMIN)
    @Path("/management")
    @Operation(summary = "Create Product Type", description = "Create new Product Type")
    public Response create(@RequestBody @Valid CreateProductTypeRQ createProductTypeRQ) {
        return Response.ok(productTypeService.create(createProductTypeRQ)).build();
    }

    @PUT
    @RolesAllowed(Roles.ADMIN)
    @Path("/management/{id}")
    @Operation(summary = "Update Product Type", description = "Update Product Type")
    public Response update(@PathParam("id") Long id, @Valid UpdateProductTypeRQ updateProductTypeRQ) {
        return Response.ok(productTypeService.update(id, updateProductTypeRQ)).build();
    }

    @DELETE
    @RolesAllowed(Roles.ADMIN)
    @Path("management/{id}")
    @Operation(summary = "Delete Product Type", description = "Delete Product Type")
    public void delete(@PathParam("id") Long id) {
        productTypeService.delete(id);
    }

    @GET
    @RolesAllowed(Roles.ADMIN)
    @Path("/management/{id}")
    @Operation(summary = "Get Product Type by id", description = "Get Product Type by Id")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(productTypeService.getProductTypeRSById(id)).build();
    }

    @GET
    @PermitAll
    @Path("/{id}")
    @Operation(summary = "Get active Product Type by id", description = "Get active Product Type by Id")
    public Response getActiveById(@PathParam("id") Long id) {
        return Response.ok(productTypeService.getActiveProductTypeRSById(id)).build();
    }

    @GET
    @PermitAll
    @Path("/get-by-code")
    @Operation(summary = "Get Product Type by code", description = "Get Product Type by code")
    public Response getByCode(@QueryParam("code") String code) {
        return Response.ok(productTypeService.getActiveProductTypeRSByCode(code)).build();
    }

    @POST
    @RolesAllowed(Roles.ADMIN)
    @Path("/management/get-product_types")
    @Operation(summary = "Get Product Types", description = "Get Product Types")
    public Response getProductTypes(@RequestBody GetProductTypeRQ getProductTypeRQ) {
        return Response.ok(productTypeService.getProductTypes(getProductTypeRQ)).build();
    }

    @POST
    @PermitAll
    @Path("/get-product_types")
    @Operation(summary = "Get active Product Types", description = "Get active Product Types")
    public Response getActiveProductTypes(@RequestBody GetProductTypeRQ getProductTypeRQ) {
        return Response.ok(productTypeService.getActiveProductTypes(getProductTypeRQ)).build();
    }
}
