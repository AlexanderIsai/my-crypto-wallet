package de.telran.mycryptowallet.mapper.userMapper;

import de.telran.mycryptowallet.dto.userDTO.UserAddDTO;
import de.telran.mycryptowallet.dto.userDTO.UserOutDTO;
import de.telran.mycryptowallet.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * description
 *
 * @author Alexander Isai on 03.03.2024.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity (UserAddDTO userAddDTO);

    UserOutDTO toDto (User user);
}
