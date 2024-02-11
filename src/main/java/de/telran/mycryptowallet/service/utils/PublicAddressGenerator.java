package de.telran.mycryptowallet.service.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * description
 *
 * @author Alexander Isai on 23.01.2024.
 */
@Service
@RequiredArgsConstructor
public class PublicAddressGenerator {
    private final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private final int ADDRESS_LENGTH = 34;
    private final String[] START_ADDRESS = {"1", "3", "0x"};

    public String generatePublicAddress(String code){
        return switch (code) {
            case "USDT" -> generateTetherAddress();
            case "BTC" -> generateBitcoinAddress();
            default -> "";
        };
    }

    public String generateBitcoinAddress() {
        StringBuilder address = new StringBuilder();
        Random random = new Random();

        address.append(random.nextBoolean() ? START_ADDRESS[0] : START_ADDRESS[1]);

        for (int i = 1; i < ADDRESS_LENGTH; i++) {
            address.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }

        return address.toString();
    }

    public String generateTetherAddress() {
        StringBuilder address = new StringBuilder();
        Random random = new Random();

        address.append(START_ADDRESS[2]);

        for (int i = 1; i < ADDRESS_LENGTH; i++) {
            address.append(ALPHABET.charAt(random.nextInt(ALPHABET.length() - 1)));
        }

        return address.toString();
    }

}


