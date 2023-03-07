package vn.topwines.core.query;

import io.quarkus.panache.common.Sort;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequest {
    private PageRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    private PageRequest(int page, int size, Sort sort) {
        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size);
    }

    public static PageRequest of(int page, int size, Sort sort) {
        return new PageRequest(page, size, sort);
    }

    private int page;
    private int size;
    private Sort sort;
}
