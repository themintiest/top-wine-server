package vn.topwines.brands.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import vn.topwines.core.utils.InstantEpochMillisSerializer;

import java.time.Instant;

@Getter
@Setter
public class BrandRS {
    private Long id;
    private String name;
    private String code;
    private String keywords;
    private String image;
    private String canonical;
    private String robotTag;

    @JsonSerialize(using = InstantEpochMillisSerializer.class)
    private Instant createdTime;

    @JsonSerialize(using = InstantEpochMillisSerializer.class)
    private Instant updatedTime;
}
