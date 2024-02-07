package de.telran.mycryptowallet.exceptions;

/**
 * description
 *
 * @author Alexander Isai on 31.01.2024.
 */
public class NotActiveOrderException extends RuntimeException{

    public NotActiveOrderException(String message){
        super(message);
    }
}
