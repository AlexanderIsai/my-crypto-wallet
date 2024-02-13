package de.telran.mycryptowallet.service.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * description
 *
 * @author Alexander Isai on 23.01.2024.
 */
@Service
@RequiredArgsConstructor
public class RateGenerator {

    @Value("${url.bitcoin}")
    private  String bitcoinUrl;

    public Map<String, Object> getBitcoinPrice() {
        RestTemplate restTemplate = new RestTemplate();
        String url = bitcoinUrl;
        return restTemplate.getForObject(url, Map.class);
    }
    //TODO расширить функционал (добавить другие валюты)

}
