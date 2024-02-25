package de.telran.mycryptowallet.controller;
import de.telran.mycryptowallet.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Provides centralized exception handling across all {@code @RequestMapping} methods through {@code @ExceptionHandler} methods.
 * This class handles custom exceptions thrown by the application and returns appropriate HTTP responses to the client.
 * It leverages Spring's {@code @RestControllerAdvice} to apply globally across all controllers.
 * Each method is tailored to handle a specific type of custom exception, ensuring that the client receives a meaningful response.
 * Logging is applied via {@code @Slf4j} for monitoring and debugging purposes.
 *
 * @author Alexander Isai
 * @version 31.01.2024
 */
@RestControllerAdvice
@Slf4j
public class ExceptionController {

    /**
     * Handles {@link NotEnoughFundsException} by returning a BAD_REQUEST response.
     *
     * @param exception The caught {@link NotEnoughFundsException}.
     * @return A {@link ResponseEntity} with an error message and HTTP status code.
     */
    @ExceptionHandler(NotEnoughFundsException.class)
    public ResponseEntity<Object> handleNotEnoughFunds(NotEnoughFundsException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link NotActiveOrderException} by returning a BAD_REQUEST response.
     *
     * @param exception The caught {@link NotActiveOrderException}.
     * @return A {@link ResponseEntity} with an error message and HTTP status code.
     */
    @ExceptionHandler(NotActiveOrderException.class)
    public ResponseEntity<Object> handleNotActiveOrder(NotActiveOrderException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link UserIsBlockedException} by returning a FORBIDDEN response.
     *
     * @param exception The caught {@link UserIsBlockedException}.
     * @return A {@link ResponseEntity} with an error message and HTTP status code.
     */
    @ExceptionHandler(UserIsBlockedException.class)
    public ResponseEntity<String> handleUserIsBlocked(UserIsBlockedException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    /**
     * Handles {@link ExistAccountException} by returning a BAD_REQUEST response.
     *
     * @param exception The caught {@link ExistAccountException}.
     * @return A {@link ResponseEntity} with an error message and HTTP status code.
     */
    @ExceptionHandler(ExistAccountException.class)
    public ResponseEntity<String> handleExistAccount(ExistAccountException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link IncorrectInputException} by returning a BAD_REQUEST response.
     *
     * @param exception The caught {@link IncorrectInputException}.
     * @return A {@link ResponseEntity} with an error message and HTTP status code.
     */
    @ExceptionHandler(IncorrectInputException.class)
    public ResponseEntity<String> handleIncorrectInput(IncorrectInputException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link EntityNotFoundException} by returning a BAD_REQUEST response.
     *
     * @param exception The caught {@link EntityNotFoundException}.
     * @return A {@link ResponseEntity} with an error message and HTTP status code.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(EntityNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}