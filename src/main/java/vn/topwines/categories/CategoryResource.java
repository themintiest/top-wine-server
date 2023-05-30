package vn.topwines.categories;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import vn.topwines.categories.domain.CategoryDetail;
import vn.topwines.categories.domain.GetCategoryRQ;
import vn.topwines.core.query.Pageable;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/categories")
@Tag(name = "Category")
@ApplicationScoped
@RequiredArgsConstructor
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource {

    private final CategoryService categoryService;

    @POST
    @PermitAll
    @Path("")
    @Operation(summary = "Get active categories", description = "Get active categories")
    public Pageable<CategoryDetail> findAll(@RequestBody GetCategoryRQ getCategoryRQ) {
        return categoryService.getActiveCategories(getCategoryRQ);
    }

    @GET
    @PermitAll
    @Path("/get-by-code")
    @Operation(summary = "Get active category by code", description = "Get active category by code")
    public CategoryDetail getById(@QueryParam("code") String code) {
        return categoryService.getActiveCategoryRSByCode(code);
    }
}
