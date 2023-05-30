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
import jakarta.ws.rs.core.MediaType;

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
