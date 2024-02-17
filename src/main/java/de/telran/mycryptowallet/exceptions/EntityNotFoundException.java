package de.telran.mycryptowallet.exceptions;

/**
 * description
 *
 * @author Alexander Isai on 12.02.2024.
 */
public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String message){
        super(message);
    }

}
