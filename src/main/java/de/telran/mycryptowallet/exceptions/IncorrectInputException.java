package de.telran.mycryptowallet.exceptions;

import java.math.BigDecimal;

/**
 * description
 *
 * @author Alexander Isai on 12.02.2024.
 */
public class IncorrectInputException extends RuntimeException{

    public IncorrectInputException(String message){
        super(message);
    }

}
