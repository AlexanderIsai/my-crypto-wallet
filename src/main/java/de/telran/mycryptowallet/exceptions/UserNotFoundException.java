package de.telran.mycryptowallet.exceptions;

/**
 * description
 *
 * @author Alexander Isai on 12.02.2024.
 */
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){
        super(message);
    }

}
