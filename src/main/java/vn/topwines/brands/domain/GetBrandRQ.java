package vn.topwines.brands.domain;

import lombok.Getter;
import lombok.Setter;
import vn.topwines.core.query.PagingRQ;

@Getter
@Setter
public class GetBrandRQ extends PagingRQ {
    private String name;
    private String code;
    private Boolean isDeleted;
}
