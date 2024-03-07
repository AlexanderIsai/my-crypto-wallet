package de.telran.mycryptowallet.dto.userDTO;

import de.telran.mycryptowallet.entity.entityEnum.UserRole;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

/**
 * description
 *
 * @author Alexander Isai on 04.03.2024.
 */
@Data
@AllArgsConstructor
public class UserOutDTO {

    private BigInteger id;
    private String name;
    private String email;
    private UserStatus status;
    private UserRole role;

}
