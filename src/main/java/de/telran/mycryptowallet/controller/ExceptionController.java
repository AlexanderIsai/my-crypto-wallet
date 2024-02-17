package de.telran.mycryptowallet.controller;

import de.telran.mycryptowallet.exceptions.*;
import lombok.extern.slf4j.Slf4j;
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
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectInputException.class)
    public ResponseEntity<String> handleIncorrectInput(IncorrectInputException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(EntityNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
