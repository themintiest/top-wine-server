package vn.topwines.categories.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import vn.topwines.core.utils.InstantEpochMillisSerializer;

import java.time.Instant;

@Getter
@Setter
public class CategorySimple {
    private Long id;
    private String name;
    private String code;
    private String image;
    private Boolean isDeleted;

    @JsonSerialize(using = InstantEpochMillisSerializer.class)
    private Instant createdTime;

    @JsonSerialize(using = InstantEpochMillisSerializer.class)
    private Instant updatedTime;
}
