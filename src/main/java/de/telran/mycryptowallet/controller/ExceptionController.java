package de.telran.mycryptowallet.controller;

import de.telran.mycryptowallet.exceptions.NotActiveOrder;
import de.telran.mycryptowallet.exceptions.NotEnoughFundsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * description
 *
 * @author Alexander Isai on 31.01.2024.
 */
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotEnoughFundsException.class)
    public ResponseEntity<Object> handleNotEnoughFunds(NotEnoughFundsException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotActiveOrder.class)
    public ResponseEntity<Object> handleNotActiveOrder(NotActiveOrder exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
