package com.karrabo.cardpayment.inputs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyExchange {

    private String username;
    private String tid;
    private String component;
    private String ip;
    private Integer port;
    private Boolean ssl;

}