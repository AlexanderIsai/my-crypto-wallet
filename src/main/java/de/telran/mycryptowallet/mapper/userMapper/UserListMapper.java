package de.telran.mycryptowallet.mapper.userMapper;

import de.telran.mycryptowallet.dto.userDTO.UserOutDTO;
import de.telran.mycryptowallet.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * description
 *
 * @author Alexander Isai on 04.03.2024.
 */
@Mapper(componentModel = "spring")
public interface UserListMapper {

    List<UserOutDTO> toDtoList(List<User> userList);
}
