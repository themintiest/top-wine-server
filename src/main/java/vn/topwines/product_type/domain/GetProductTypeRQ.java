package vn.topwines.product_type.domain;

import lombok.Getter;
import lombok.Setter;
import vn.topwines.core.query.PagingRQ;

@Getter
@Setter
public class GetProductTypeRQ extends PagingRQ {
    private String name;
    private String code;
    private Boolean isDeleted;
}
