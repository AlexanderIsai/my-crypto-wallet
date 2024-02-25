package de.telran.mycryptowallet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class represents a Data Transfer Object (DTO) for adding a new user to the system.
 * It encapsulates the necessary information required to create a user, including the user's name,
 * email, and password. Validation annotations ensure that the provided data meets the system's requirements
 * for user creation.
 * This DTO is typically used in the process of registering a new user, where the client-side application collects
 * user information and sends it to the server-side application for processing.
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
