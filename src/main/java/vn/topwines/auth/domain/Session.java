package vn.topwines.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import vn.topwines.core.domain.Role;
import vn.topwines.core.domain.UserProfile;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Schema(name = "auth_token")
    private String authToken;

    private UserProfile profile;

    private Set<Role> roles;
}
