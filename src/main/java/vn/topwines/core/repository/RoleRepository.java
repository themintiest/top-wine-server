package vn.topwines.core.repository;


import vn.topwines.core.entity.Role;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class RoleRepository extends BaseRepository<Role, Long> {
    public Optional<Role> findByName(String name) {
        return find("name", name).firstResultOptional();
    }

    public Set<Role> findByNameIn(Set<String> name) {
        return findIn("name", name);
    }

}
