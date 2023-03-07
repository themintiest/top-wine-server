package vn.topwines.users;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.identity.SecurityIdentity;
import lombok.RequiredArgsConstructor;
import vn.topwines.core.constants.Roles;
import vn.topwines.core.entity.Account;
import vn.topwines.core.entity.AccountRole;
import vn.topwines.core.entity.Profile;
import vn.topwines.core.entity.Role;
import vn.topwines.core.mappers.AccountMapper;
import vn.topwines.core.mappers.ProfileMapper;
import vn.topwines.core.query.Pageable;
import vn.topwines.core.query.PagingQuery;
import vn.topwines.core.repository.AccountRepository;
import vn.topwines.core.repository.AccountRoleRepository;
import vn.topwines.core.repository.RoleRepository;
import vn.topwines.core.repository.UserRepository;
import vn.topwines.exception.RoleException;
import vn.topwines.exception.UserException;
import vn.topwines.security.SecurityIdentityHolder;
import vn.topwines.users.domain.GrantRoleRQ;
import vn.topwines.users.domain.RegisterRequest;
import vn.topwines.users.domain.UpdateProfileRequest;
import vn.topwines.users.domain.UserDto;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ApplicationScoped
@RequiredArgsConstructor
public class UserService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;

    public Pageable<UserDto> search(PagingQuery pagingQuery) {
        return userRepository.search(pagingQuery, ProfileMapper.INSTANCE::mapFromUserToUserDto);
    }

    public Profile getById(Long id) {
        return userRepository.findByIdOptional(id).orElseThrow(UserException::userNotFound);
    }

    public Profile getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserException::userNotFound);
    }

    @Transactional
    public UserDto createUser(RegisterRequest registerRequest) {
        validateRegisterRequest(registerRequest);
        Role role = roleRepository.findByName(Roles.USER).orElseThrow(RoleException::roleNotFound);
        Account account = createAccount(registerRequest, role);
        Profile profile = ProfileMapper.INSTANCE.mapFromRegisterRequestToUser(registerRequest);
        profile.setAccount(account);
        return ProfileMapper.INSTANCE.mapFromUserToUserDto(userRepository.save(profile));
    }

    public UserDto getCurrentLoginUser() {
        SecurityIdentity identity = SecurityIdentityHolder.getIdentity();
        Profile profile = userRepository.findByEmail(identity.getPrincipal().getName())
                .orElseThrow(UserException::userNotFound);
        return ProfileMapper.INSTANCE.mapFromUserToUserDto(profile);
    }

    @Transactional
    public UserDto updateMyProfile(UpdateProfileRequest updateProfileRequest) {
        SecurityIdentity identity = SecurityIdentityHolder.getIdentity();
        Profile profile = userRepository.findByEmail(identity.getPrincipal().getName())
                .orElseThrow(UserException::userNotFound);
        ProfileMapper.INSTANCE.merge(profile, updateProfileRequest);
        return ProfileMapper.INSTANCE.mapFromUserToUserDto(userRepository.save(profile));
    }

    @Transactional
    public UserDto grantRoleForUser(Long id, GrantRoleRQ grantRoleRQ) {
        Profile profile = userRepository.findByIdOptional(id).orElseThrow(UserException::userNotFound);
        Account account = accountRepository.findByEmail(profile.getEmail()).orElseThrow(UserException::userNotFound);
        Set<Role> roles = roleRepository.findByNameIn(grantRoleRQ.getRoles());
        List<AccountRole> accountRoles = new ArrayList<>();
        accountRoleRepository.deleteByAccountId(account.getId());
        for (Role role : roles) {
            AccountRole accountRole = new AccountRole();
            accountRole.setRole(role);
            accountRole.setAccount(account);
            accountRoles.add(accountRole);
        }
        accountRoleRepository.persist(accountRoles);
        return ProfileMapper.INSTANCE.mapFromUserToUserDto(profile);
    }

    public UserDto getUserRSById(Long id) {
        Profile profile = userRepository.findByIdOptional(id).orElseThrow(UserException::userNotFound);
        return ProfileMapper.INSTANCE.mapFromUserToUserDto(profile);
    }

    private Account createAccount(RegisterRequest registerRequest, Role role) {
        Account account = AccountMapper.INSTANCE.mapFromRegisterRequestToAccount(registerRequest);
        account.setPassword(BcryptUtil.bcryptHash(registerRequest.getPassword()));
        Account entity = accountRepository.save(account);
        AccountRole accountRole = new AccountRole();
        accountRole.setAccount(account);
        accountRole.setRole(role);
        accountRoleRepository.save(accountRole);
        return entity;
    }

    private void validateRegisterRequest(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw UserException.emailHasBeenInUsed();
        }
    }
}
