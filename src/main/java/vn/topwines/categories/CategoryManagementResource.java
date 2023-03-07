package vn.topwines.categories;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import vn.topwines.categories.domain.CategoryDetail;
import vn.topwines.categories.domain.CategoryRQ;
import vn.topwines.categories.domain.GetCategoryRQ;
import vn.topwines.core.constants.Roles;
import vn.topwines.core.query.Pageable;

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
import javax.ws.rs.core.MediaType;

@Path("/management/categories")
@Tag(name = "Category management")
@ApplicationScoped
@RequiredArgsConstructor
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoryManagementResource {
    private final CategoryService categoryService;

    @POST
    @RolesAllowed(Roles.ADMIN)
    @Path("")
    @Operation(summary = "Create category", description = "Create new category")
    public CategoryDetail create(@RequestBody @Valid CategoryRQ categoryRQ) {
        return categoryService.create(categoryRQ);
    }

    @PUT
    @RolesAllowed(Roles.ADMIN)
    @Path("/{id}")
    @Operation(summary = "Update category", description = "Update category")
    public CategoryDetail update(@PathParam("id") Long id, @Valid CategoryRQ categoryRQ) {
        return categoryService.update(id, categoryRQ);
    }

    @DELETE
    @RolesAllowed(Roles.ADMIN)
    @Path("/{id}")
    @Operation(summary = "Delete category", description = "Delete category")
    public void delete(@PathParam("id") Long id) {
        categoryService.delete(id);
    }

    @GET
    @PermitAll
    @Path("/{id}")
    @Operation(summary = "Get category by id", description = "Get category by Id")
    public CategoryDetail getById(@PathParam("id") Long id) {
        return categoryService.getCategoryRSById(id);
    }


    @POST
    @PermitAll
    @Path("/get-categories")
    @Operation(summary = "Get categories", description = "Get category by Id")
    public Pageable<CategoryDetail> findAll(@RequestBody GetCategoryRQ getCategoryRQ) {
        return categoryService.getCategories(getCategoryRQ);
    }
}
