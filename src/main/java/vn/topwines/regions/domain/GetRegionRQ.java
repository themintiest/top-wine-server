package vn.topwines.regions.domain;

import lombok.Getter;
import lombok.Setter;
import vn.topwines.core.query.PagingRQ;

@Getter
@Setter
public class GetRegionRQ extends PagingRQ {
    private String name;
    private String code;
    private Boolean isDeleted;
}
