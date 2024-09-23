package com.karrabo.cardpayment.inputs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddTids {

    private String host;
    private String tid;
    private String component;
    private String ip;
    private Integer port;
    private Boolean ssl;

}