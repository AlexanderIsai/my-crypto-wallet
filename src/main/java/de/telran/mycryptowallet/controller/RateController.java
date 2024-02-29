package de.telran.mycryptowallet.controller;

import de.telran.mycryptowallet.entity.Rate;
import de.telran.mycryptowallet.exceptions.EntityNotFoundException;
import de.telran.mycryptowallet.service.interfaces.RateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description
 *
 * @author Alexander Isai on 26.02.2024.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/rate")
@Slf4j
public class RateController {

    private final RateService rateService;

    @GetMapping(value = "/{code}")
    @Operation(summary = "Show the rate of currency", description = "Returns current values of rate")
    public ResponseEntity<Rate> showRateForBuyByCurrency(@PathVariable (value = "code") String code){
        Rate rate = rateService.getFreshRate(code);
        if (rate != null) {
            return ResponseEntity.ok(rate);
        } else {
            throw new EntityNotFoundException("Rate is not found");
        }
    }
}
