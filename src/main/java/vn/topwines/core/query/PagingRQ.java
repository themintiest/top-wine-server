package vn.topwines.core.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingRQ {
    private int page = 0;
    private int size = 10;
    private String sortBy;
    private String direction;
}
