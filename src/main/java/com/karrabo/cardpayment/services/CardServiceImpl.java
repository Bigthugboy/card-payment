package com.karrabo.cardpayment.services;

import com.karrabo.cardpayment.config.CardProperties;
import com.karrabo.cardpayment.iso.*;
import com.karrabo.cardpayment.models.KeysResponse;
import com.karrabo.cardpayment.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardProperties cardProperties;

//    @Scheduled(fixedRate = 15000)
    @Override
    public KeysResponse cardWithZone() {
        log.info("Running Scheduled Zone Card TRANSACTION @  {}", LocalDateTime.now());
        ISO8583 packISO8583 = new ISO8583();
        packISO8583.setMit("0200");

        packISO8583.clearBit();

        byte[] field2 = "5663975048128222".getBytes();
        packISO8583.setBit(2, field2, field2.length);

        byte[] field3 = "500000".getBytes();
        packISO8583.setBit(3, field3, field3.length);

        byte[] field4 = "000000001000".getBytes();
        packISO8583.setBit(4, field4, field4.length);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddhhmmss");
        String datetime = simpleDateFormat.format(new Date());
        byte[] field7 = datetime.getBytes();
        packISO8583.setBit(7, field7, field7.length);

        simpleDateFormat = new SimpleDateFormat("hhmmss");
        String stan = simpleDateFormat.format(new Date());
        byte[] field11 = stan.getBytes();
        packISO8583.setBit(11, field11, field11.length);

//        byte[] field12 = stan.getBytes();
        byte[] field12 = "142529".getBytes();
        packISO8583.setBit(12, field12, field12.length);

        simpleDateFormat = new SimpleDateFormat("MMdd");
        String date = simpleDateFormat.format(new Date());
        byte[] field13 = date.getBytes();
        packISO8583.setBit(13, field13, field13.length);

        byte[] field14 = "2704".getBytes();
        packISO8583.setBit(14, field14, field14.length);

        byte[] field18 = "6011".getBytes();
        packISO8583.setBit(18, field18, field18.length);

        byte[] field22 = "051".getBytes();
        packISO8583.setBit(22, field22, field22.length);

        byte[] field23 = "000".getBytes();
        packISO8583.setBit(23, field23, field23.length);

        byte[] field25 = "00".getBytes();
        packISO8583.setBit(25, field25, field25.length);

        byte[] field26 = "12".getBytes();
        packISO8583.setBit(26, field26, field26.length);

        byte[] field28 = "D00000000".getBytes();
        packISO8583.setBit(28, field28, field28.length);

        byte[] field30 = "D00000000".getBytes();
        packISO8583.setBit(30, field30, field30.length);

        byte[] field32 = "111129".getBytes();
        packISO8583.setBit(32, field32, field32.length);

        byte[] field35 = "5399412014329580D2408201167985550".getBytes();
        packISO8583.setBit(35, field35, field35.length);

        simpleDateFormat = new SimpleDateFormat("yyMMddhhmmss");
        String rrn = simpleDateFormat.format(new Date());
        byte[] field37 = rrn.getBytes();
        packISO8583.setBit(37, field37, field37.length);

        byte[] field40 = "221".getBytes();
        packISO8583.setBit(40, field40, field40.length);

        byte[] field41 = "20351254".getBytes();
        packISO8583.setBit(41, field41, field41.length);

        byte[] field42 = "Z52990C010049".getBytes();
        packISO8583.setBit(42, field42, field42.length);

        byte[] field43 = "T ASIWAJU VENTURE   000511 2TEPQP12 LANG".getBytes();
        packISO8583.setBit(43, field43, field43.length);

        byte[] field49 = "566".getBytes();
        packISO8583.setBit(49, field49, field49.length);

//        byte[] field52 = "9F2CD8FE3753CE2D".getBytes();
//        packISO8583.setBit(52, field52, field52.length);

//        byte[] field55 = ("9F260850E5378A92DAFBAB9F2701409F10200FA501620200100000000000000000000" +
//                "F0100000000000000000000000000009F37046667C4D59F360202A3950502002488009A032405309C01009F020600000" +
//                "17000005F2A020566820258008407A00000037100019F0306000000000000" +
//                "5F3401019F1A0205669F3303E0F8C89F34030203009F3501229F410400000001").getBytes();
//        packISO8583.setBit(55, field55, field55.length);

        byte[] field56 = "1510".getBytes();
        packISO8583.setBit(56, field56, field56.length);

        byte[] field59 = "19106295265".getBytes();
        packISO8583.setBit(59, field59, field59.length);


        byte[] field123 = "51120151114C001".getBytes();
        packISO8583.setBit(123, field123, field123.length);


        return getMasterKeySignOnWithIsoPayload("20351254", "44.238.1.233", "12000", "false", packISO8583);
    }

    private KeysResponse getMasterKeySignOnWithIsoPayload(String tid, String ip, String port, String ssl, ISO8583 packISO8583) {

        log.info("packISO8583: : {}", packISO8583);
        ISO8583.sec = true;
        byte[] packData = packISO8583.isotostr();
        byte[] recvarr;
        ISO8583 unpackISO8583 = new ISO8583();
        log.info("Checking SSL........");
        //Check if it is ssl here
        if (ssl.equals("true")) {
            try {
                if (port.equalsIgnoreCase(cardProperties.getNusport())) {
                    recvarr = SSLTLS.secureISOMessageSenderReceiver(ip, port, packData);
                } else {
                    recvarr = SSLTLS.doSSL(ip, port, packData);
                }
            } catch (Exception e) {
                log.info("Error: {}", e.getMessage());
                return KeysResponse.builder()
                        .code(Constants.FAILURE_CODE)
                        .build();
            }
        } else {
            CommSocket send = new CommSocket();
            if (send.open(ip, port) == true) {
                int count = send.send(packData);
                recvarr = send.recv();
                send.close();
            } else {
                log.info("MASTERKEY DOWNLAOD FAILED");
                return KeysResponse.builder()
                        .code(Constants.FAILURE_CODE)
                        .build();
            }
        }

        log.info("recvarr length: : {}", recvarr == null ? "null" : recvarr.length);

        if (recvarr == null || recvarr.length < 10) {
            return KeysResponse.builder()
                    .code(Constants.FAILURE_CODE)
                    .build();
        }

        if (ip.equalsIgnoreCase(cardProperties.getUpip()) || ip.equalsIgnoreCase(cardProperties.getIswip())) {
            ProfileParser.mskhost1 = new byte[recvarr.length - 2];
            System.arraycopy(recvarr, 2, ProfileParser.mskhost1, 0, recvarr.length - 2);
            unpackISO8583.strtoiso(ProfileParser.mskhost1);
        } else {
            ProfileParser.mskhost1 = new byte[recvarr.length - 4];
            System.arraycopy(recvarr, 4, ProfileParser.mskhost1, 0, recvarr.length - 4);
            unpackISO8583.strtoiso(ProfileParser.mskhost1);
        }
        ISO8583.sec = true;
        ProfileParser.mskhost1 = new byte[recvarr.length - 2];
        System.arraycopy(recvarr, 2, ProfileParser.mskhost1, 0, recvarr.length - 2);
        unpackISO8583.strtoiso(ProfileParser.mskhost1);
        log.info("1. RECV: " + new String(recvarr));
        log.info("DONE WITH CONNECTION");

        ProfileParser.receiving = new String[128];
        Utilities.logISOMsg(unpackISO8583, ProfileParser.receiving);
        log.info("ProfileParser.receiving {}", ProfileParser.receiving);
        if (ProfileParser.receiving[39].equals("00")) {

            try {
                return KeysResponse.builder()
                        .code(Constants.SUCCESS_CODE)

                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return KeysResponse.builder()
                    .code(Constants.FAILURE_CODE)
                    .build();
        }
        return KeysResponse.builder()
                .code(Constants.FAILURE_CODE)
                .build();
    }

}
