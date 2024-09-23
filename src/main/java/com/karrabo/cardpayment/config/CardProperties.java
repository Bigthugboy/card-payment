package com.karrabo.cardpayment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Component
@ConfigurationProperties(prefix = "card")
@Data
public class CardProperties {

    @NotBlank(message = "upip should not be empty")
    private String upip;
    @NotBlank(message = "upcustom should not be empty")
    private String upcustom;
    @NotBlank(message = "iswip should not be empty")
    private String iswip;
    @NotBlank(message = "nusport should not be empty")
    private String nusport;
}