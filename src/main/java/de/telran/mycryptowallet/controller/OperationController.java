package de.telran.mycryptowallet.controller;

import de.telran.mycryptowallet.dto.OperationAddDTO;
import de.telran.mycryptowallet.exceptions.NotEnoughFundsException;
import de.telran.mycryptowallet.service.interfaces.OperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description
 *
 * @author Alexander Isai on 24.01.2024.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/operations")
@Slf4j
public class OperationController {

    private final OperationService operationService;

    @PostMapping(value = "/add")
    public void addOperation(@RequestBody OperationAddDTO operationAddDTO) throws NotEnoughFundsException {
        operationService.addExchangeOperation(operationAddDTO);
    }

}
