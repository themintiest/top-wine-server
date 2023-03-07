package vn.topwines.categories.domain;

import lombok.Getter;
import lombok.Setter;
import vn.topwines.core.query.PagingRQ;

@Getter
@Setter
public class GetCategoryRQ extends PagingRQ {
    private String name;
    private String code;
    private Boolean withOutParent = true;
    private Boolean isDeleted;
}
