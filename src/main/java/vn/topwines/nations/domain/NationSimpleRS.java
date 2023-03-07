package vn.topwines.nations.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import vn.topwines.core.utils.InstantEpochMillisSerializer;

import java.time.Instant;

@Getter
@Setter
public class NationSimpleRS {
    private Long id;
    private String name;
    private String code;
    private String image;

    @JsonSerialize(using = InstantEpochMillisSerializer.class)
    private Instant createdTime;

    @JsonSerialize(using = InstantEpochMillisSerializer.class)
    private Instant updatedTime;
}
