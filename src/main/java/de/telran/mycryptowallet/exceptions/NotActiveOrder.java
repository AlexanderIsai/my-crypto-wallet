package de.telran.mycryptowallet.exceptions;

/**
 * description
 *
 * @author Alexander Isai on 31.01.2024.
 */
public class NotActiveOrder extends Exception{

    public NotActiveOrder(String message){
        super(message);
    }
}
