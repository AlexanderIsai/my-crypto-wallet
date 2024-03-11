package de.telran.mycryptowallet.dto.operationDTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * description
 *
 * @author Alexander Isai on 07.03.2024.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationTransferDTO {

    @NotBlank(message = "Currency code cannot be empty")
    private String code;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "PublicAddress code cannot be empty")
    private String address;
}
