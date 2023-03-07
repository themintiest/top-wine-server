package vn.topwines.nations.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateNationRQ {
    @NotBlank
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
