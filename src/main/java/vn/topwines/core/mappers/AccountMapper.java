package vn.topwines.core.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.topwines.core.entity.Account;
import vn.topwines.users.domain.RegisterRequest;

@Mapper(
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL
)
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "username", source = "email")
    Account mapFromRegisterRequestToAccount(RegisterRequest registerRequest);

    vn.topwines.core.domain.Account map(Account account);

    Account map(vn.topwines.core.domain.Account account);
}
