package vn.topwines.categories.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CategoryRQ {
    @NotEmpty
    private String name;
    private String code;
    private String description;
    private String image;
    private String seoTitle;
    private String seoDescription;
    private String robotTag;
    private Long parentId;
}
