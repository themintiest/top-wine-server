package vn.topwines.grapes.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateGrapeRQ {
    private String name;
    private String code;
    private String keywords;
    private String description;
    private String image;
    private String seoTitle;
    private String seoDescription;
    private String canonical;
    private String robotTag;
}
