package de.telran.mycryptowallet.service.interfaces;

import java.util.Map;

/**
 * description
 *
 * @author Alexander Isai on 22.01.2024.
 */
public interface RateService {

    Map<String, Object> getRate();
    void addRate();

}
