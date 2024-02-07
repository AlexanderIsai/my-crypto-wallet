package de.telran.mycryptowallet.controller;

import de.telran.mycryptowallet.exceptions.ExistAccountException;
import de.telran.mycryptowallet.exceptions.NotActiveOrderException;
import de.telran.mycryptowallet.exceptions.NotEnoughFundsException;
import de.telran.mycryptowallet.exceptions.UserIsBlockedException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
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
@Slf4j
public class ExceptionController {

    @ExceptionHandler(NotEnoughFundsException.class)
    public ResponseEntity<Object> handleNotEnoughFunds(NotEnoughFundsException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotActiveOrderException.class)
    public ResponseEntity<Object> handleNotActiveOrder(NotActiveOrderException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserIsBlockedException.class)
    public ResponseEntity<String> handleUserIsBlocked(UserIsBlockedException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    @ExceptionHandler(ExistAccountException.class)
    public ResponseEntity<String> handleExistAccount(ExistAccountException exception){
        log.error("etwas geht schleht", exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);

    }
}
