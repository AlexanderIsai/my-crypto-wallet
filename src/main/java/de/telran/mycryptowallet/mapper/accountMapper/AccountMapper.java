package de.telran.mycryptowallet.mapper.accountMapper;

import de.telran.mycryptowallet.dto.accountDTO.AccountOutDTO;
import de.telran.mycryptowallet.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * description
 *
 * @author Alexander Isai on 04.03.2024.
 */
@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "currency.code", target = "code")
    AccountOutDTO toDto(Account account);
    List<AccountOutDTO> toDtoList(List<Account> accounts);
}
