package de.telran.mycryptowallet.exceptions;

/**
 * description
 *
 * @author Alexander Isai on 31.01.2024.
 */
public class NotEnoughFundsException extends RuntimeException{

    public NotEnoughFundsException(String message){
        super(message);
    }
}
