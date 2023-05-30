package vn.topwines.core.repository;

import io.quarkus.panache.common.Parameters;
import vn.topwines.core.domain.UserStatus;
import vn.topwines.core.entity.Account;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AccountRepository extends BaseRepository<Account, Long> {
    public Optional<Account> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public Optional<Account> findByEmailAndStatus(String email, UserStatus status) {
        Parameters parameters = Parameters.with("email", email).and("status", status);
        return find("email = :email and status = :status", parameters).firstResultOptional();
    }

    public List<Account> test() {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Account> cq = criteriaBuilder.createQuery(Account.class);
        Root<Account> root = cq.from(Account.class);
        TypedQuery<Account> typedQuery = getEntityManager().createQuery(cq);
        return Collections.emptyList();
    }
}
