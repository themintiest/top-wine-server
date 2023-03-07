package vn.topwines.categories;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import vn.topwines.categories.domain.CategoryDetail;
import vn.topwines.categories.domain.GetCategoryRQ;
import vn.topwines.core.query.Pageable;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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
