package de.telran.mycryptowallet.service.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * description
 *
 * @author Alexander Isai on 23.01.2024.
 */
public class RateGenerator {

    @Scheduled(cron = "0 */5 * * * *")
    public static Map<String, Object> getBitcoinPrice() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd";
        return restTemplate.getForObject(url, Map.class);
    }

}
