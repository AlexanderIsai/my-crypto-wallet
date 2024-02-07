package de.telran.mycryptowallet.exceptions;

/**
 * description
 *
 * @author Alexander Isai on 03.02.2024.
 */
public class UserIsBlockedException extends RuntimeException{

    public UserIsBlockedException(String message){
        super(message);
    }
}
