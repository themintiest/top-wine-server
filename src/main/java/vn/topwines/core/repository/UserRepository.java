package vn.topwines.core.repository;

import io.quarkus.panache.common.Parameters;
import vn.topwines.core.entity.Profile;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class UserRepository extends BaseRepository<Profile, Long> {
    public Optional<Profile> findByEmail(String email) {
        Parameters parameters = Parameters.with("email", email);
        return find("email = :email", parameters).firstResultOptional();
    }
    public Boolean existsByEmail(String email) {
        return exists("email", email);
    }
}
