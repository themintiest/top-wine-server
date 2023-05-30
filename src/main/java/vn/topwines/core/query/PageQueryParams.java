package vn.topwines.core.query;

import lombok.Data;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;

@Data
public class PageQueryParams {
    @QueryParam("q")
    private String query;

    @QueryParam("sort")
    private String sortBy;

    @QueryParam("page")
    private int page;

    @QueryParam("size")
    @DefaultValue("10")
    private int size;
}
