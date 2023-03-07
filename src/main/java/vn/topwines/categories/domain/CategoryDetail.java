package vn.topwines.categories.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import vn.topwines.core.utils.InstantEpochMillisSerializer;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class CategoryDetail {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String image;
    private String seoTitle;
    private String seoDescription;
    private String robotTag;
    private Boolean isDeleted;

    @JsonSerialize(using = InstantEpochMillisSerializer.class)
    private Instant createdTime;

    @JsonSerialize(using = InstantEpochMillisSerializer.class)
    private Instant updatedTime;

    private List<CategoryDetail> children;
}
