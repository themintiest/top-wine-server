package vn.topwines.users.domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class GrantRoleRQ {
    @NotNull
    private Set<String> roles;
}
