package de.telran.mycryptowallet.exceptions;

/**
 * description
 *
 * @author Alexander Isai on 03.02.2024.
 */
public class ExistAccountException extends RuntimeException{

    public ExistAccountException(String message){
        super(message);
    }
}
