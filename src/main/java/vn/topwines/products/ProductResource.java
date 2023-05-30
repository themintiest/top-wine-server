package vn.topwines.products;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import vn.topwines.core.constants.Roles;
import vn.topwines.core.query.Pageable;
import vn.topwines.products.domain.CreateProductRQ;
import vn.topwines.products.domain.GetProductRQ;
import vn.topwines.products.domain.ProductRS;
import vn.topwines.products.domain.ProductSimpleRS;
import vn.topwines.products.domain.UpdateProductRQ;

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

@Path("/products")
@Tag(name = "Product")
@ApplicationScoped
@RequiredArgsConstructor
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    private final ProductService productService;

    @POST
    @RolesAllowed(Roles.ADMIN)
    @Path("/")
    @Operation(summary = "Create product", description = "Create product")
    public Response create(@RequestBody @Valid CreateProductRQ createProductRQ) {
        return Response.ok(productService.createProduct(createProductRQ)).build();
    }

    @PUT
    @RolesAllowed(Roles.ADMIN)
    @Path("/{id}")
    @Operation(summary = "Update product", description = "Update product")
    public Response update(@PathParam("id") Long id, @RequestBody @Valid UpdateProductRQ updateProductRQ) {
        return Response.ok(productService.updateProduct(id, updateProductRQ)).build();
    }

    @DELETE
    @RolesAllowed(Roles.ADMIN)
    @Path("/{id}")
    @Operation(summary = "Delete product", description = "Delete product")
    public Response delete(@PathParam("id") Long id) {
        productService.deleteProduct(id);
        return Response.noContent().build();
    }

    @GET
    @RolesAllowed(Roles.ADMIN)
    @Path("/{id}")
    @Operation(summary = "Get product by id", description = "Get product by id")
    public Response getDetailById(@PathParam("id") Long id) {
        return Response.ok(productService.getProductDetailById(id)).build();
    }

    @GET
    @PermitAll
    @Path("/get-by-code")
    @Operation(summary = "Get product by code", description = "Get product by code")
    public Response getDetailByCode(@QueryParam("code") String code) {
        return Response.ok(productService.getProductDetailByCode(code)).build();
    }


    @POST
    @RolesAllowed(Roles.ADMIN)
    @Path("/search")
    @Operation(summary = "Get products", description = "Get products")
    public Response findAll(@RequestBody GetProductRQ getProductRQ) {
        Pageable<ProductSimpleRS> productRSPageable = productService.getActiveProducts(getProductRQ);
        return Response.ok(productRSPageable).build();
    }

    @POST
    @PermitAll
    @Path("/active/search")
    @Operation(summary = "Get active products", description = "Get active products")
    public Response findAllActive(@RequestBody GetProductRQ getProductRQ) {
        Pageable<ProductRS> productRSPageable = productService.getProducts(getProductRQ);
        return Response.ok(productRSPageable).build();
    }
}
