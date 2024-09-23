package com.karrabo.cardpayment.services;

import com.karrabo.cardpayment.models.KeysResponse;
import org.springframework.scheduling.annotation.Scheduled;

public interface CardService {

    KeysResponse cardWithZone();

}
