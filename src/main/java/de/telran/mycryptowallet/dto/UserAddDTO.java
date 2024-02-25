package de.telran.mycryptowallet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * description
 *
 * @author Alexander Isai on 23.02.2024.
 */
@Data
@AllArgsConstructor
public class UserAddDTO {

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 25, message = "Name should be between 2 and 25 letters")
    private String name;
    @Email
    @NotEmpty(message = "Email should not be empty")
    private String email;
    @Size(min = 4, max = 60, message = "Password should be between 4 and 60 symbols")
    private String password;
}
