package de.telran.mycryptowallet.controller;

import de.telran.mycryptowallet.dto.operationDTO.OperationAddDTO;
import de.telran.mycryptowallet.entity.Operation;
import de.telran.mycryptowallet.service.interfaces.ActiveUserService;
import de.telran.mycryptowallet.service.interfaces.OperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code OperationController} class provides endpoints for managing operations such as deposits, withdrawals, buys, and sells within the cryptocurrency wallet platform.
 * It interacts with the {@code OperationService} to perform the underlying business logic associated with each operation type.
 * The active user's operations are determined based on their current authentication status, ensuring that transactions are securely and correctly associated with the user's account.
 *
 * @author Alexander Isai
 * @version 24.01.2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/operations")
@Slf4j
public class OperationController {

    private final OperationService operationService;
    private final ActiveUserService activeUserService;

    /**
     * Endpoint to add a new operation (deposit, withdrawal, buy, sell) for the active user.
     * This method creates a new transaction based on the input from {@code OperationAddDTO}, which includes the currency code, amount, and type of operation.
     * The actual execution of the operation is handled by {@code operationService}, which performs the necessary business logic and updates the user's account accordingly.
     *
     * @param operationAddDTO Data Transfer Object containing details of the operation such as currency code, amount, and operation type.
     */
    @PostMapping(value = "/add")
    @io.swagger.v3.oas.annotations.Operation(summary = "Add operation", description = "Creates a new transaction for the active user - deposit/withdrawal/buy/sell")
    public void addOperation(@RequestBody OperationAddDTO operationAddDTO)  {
        Operation operation = operationService.getExchangeOperation(activeUserService.getActiveUser(), operationAddDTO);
        operationService.cashFlow(operation);
    }

}
