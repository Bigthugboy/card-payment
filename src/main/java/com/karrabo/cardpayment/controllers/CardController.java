package com.karrabo.cardpayment.controllers;

import com.karrabo.cardpayment.services.CardService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("api/karrabo/card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping("/zone/card")
    public ResponseEntity<?> cardWithZone() {
        return ResponseEntity.ok(cardService.cardWithZone());
    }

}
