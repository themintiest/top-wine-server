package vn.topwines.grapes.domain;

import lombok.Getter;
import lombok.Setter;
import vn.topwines.core.query.PagingRQ;

@Getter
@Setter
public class GetGrapeRQ extends PagingRQ {
    private String name;
    private String code;
    private Boolean isDeleted;
}
