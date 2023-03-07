package vn.topwines.core.utils;

import io.quarkus.panache.common.Sort;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import vn.topwines.core.query.PageRequest;
import vn.topwines.core.query.Pageable;
import vn.topwines.core.query.PagingRQ;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageUtils {
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";

    public static PageRequest createPageRequest(PagingRQ pagingRQ) {
        if (StringUtils.isBlank(pagingRQ.getSortBy())) {
            return PageRequest.of(pagingRQ.getPage(), pagingRQ.getSize());
        }
        Sort sort = DESC.equalsIgnoreCase(pagingRQ.getDirection())
                ? Sort.by(pagingRQ.getSortBy(), Sort.Direction.Descending)
                : Sort.by(pagingRQ.getSortBy(), Sort.Direction.Ascending);
        return PageRequest.of(pagingRQ.getPage(), pagingRQ.getSize(), sort);
    }

    public static <T, E> Pageable<T> createPageResponse(List<E> items, Long total, Function<E, T> mapper) {
        return Pageable.<T>builder()
                .items(items.stream().map(mapper).collect(Collectors.toList()))
                .total(total)
                .build();
    }

    public static <T, E> Pageable<T> createPageResponse(Pageable<E> page, Function<E, T> mapper) {
        return Pageable.<T>builder()
                .items(page.getItems().stream().map(mapper).collect(Collectors.toList()))
                .total(page.getTotal())
                .build();
    }

    public static <T> Pageable<T> createPageResponse(List<T> items, Long total) {
        return Pageable.<T>builder()
                .items(items)
                .total(total)
                .build();
    }
}
