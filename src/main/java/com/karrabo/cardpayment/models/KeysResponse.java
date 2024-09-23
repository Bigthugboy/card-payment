package com.karrabo.cardpayment.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeysResponse {

    private Integer code;
    private String clear;
    private String encrypted;

    private String paramdownload;
    private String ctmkdatetime;
    private String mid;
    private String timeout;
    private String currencycode;
    private String countrycode;
    private String callhome;
    private String mcc;
    private String mnl;

}