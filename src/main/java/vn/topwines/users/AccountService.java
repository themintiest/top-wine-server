package vn.topwines.users;

import lombok.RequiredArgsConstructor;
import vn.topwines.core.domain.Account;
import vn.topwines.core.domain.UserStatus;
import vn.topwines.core.mappers.AccountMapper;
import vn.topwines.core.repository.AccountRepository;
import vn.topwines.exception.UserException;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account getAccountByEmailAndStatus(String email, UserStatus status) {
        vn.topwines.core.entity.Account account = accountRepository.findByEmailAndStatus(email, status)
                .orElseThrow(UserException::userNotFound);
        return AccountMapper.INSTANCE.map(account);
    }
}
