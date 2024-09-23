package com.karrabo.cardpayment.iso;

import com.karrabo.cardpayment.config.CardProperties;
import com.karrabo.cardpayment.inputs.CardInput;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOUtil;


import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class PackIso {
    public static String[] sending;
    public static String[] receiving;
    public static byte[] formIso(CardInput cardInput, String clrsesskey, String ip, CardProperties cardProperties)
    {
        log.info("Forming Iso");
        ISO8583 packISO8583  = new ISO8583();
        packISO8583.setMit(cardInput.getField0());//Field 0
        packISO8583.clearBit();

        byte[] field2 = cardInput.getField2().getBytes();//Field 2
        packISO8583.setBit(2, field2, field2.length);

        byte[] field3 = cardInput.getField3().getBytes();//Field 3
        packISO8583.setBit(3, field3, field3.length);

        byte[] field4 = cardInput.getField4().getBytes();//Field 4
        packISO8583.setBit(4, field4, field4.length);
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddhhmmss");
        String datetime = simpleDateFormat.format(new Date());
        byte[] field7 = datetime.getBytes();
        packISO8583.setBit(7, field7, field7.length);
        
        simpleDateFormat = new SimpleDateFormat("HHmmss");
        String stan = simpleDateFormat.format(new Date());
        byte[] field11 = cardInput.getField11().getBytes();//Field 11
        packISO8583.setBit(11, field11, field11.length);

        byte[] field12 = stan.getBytes();
        packISO8583.setBit(12, field12, field12.length);
        simpleDateFormat = new SimpleDateFormat("MMdd");
        String date = simpleDateFormat.format(new Date());
        byte[] field13 = date.getBytes();
        packISO8583.setBit(13, field13, field13.length);

        byte[] field14 = cardInput.getField14().getBytes();//Field 14
        packISO8583.setBit(14, field14, field14.length);

        byte[] field18 = cardInput.getField18().getBytes();//Field 18
        packISO8583.setBit(18, field18, field18.length);

        byte[] field22 = cardInput.getField22().getBytes();//Field 22
        packISO8583.setBit(22, field22, field22.length);

        byte[] field23 = cardInput.getField23().getBytes();//Field 23
        packISO8583.setBit(23, field23, field23.length);
        log.info("Forming Iso check");

        byte[] field25 = cardInput.getField25().getBytes();//Field 25
        packISO8583.setBit(25, field25, field25.length);

        byte[] field26 = cardInput.getField26().getBytes();//Field 26
        packISO8583.setBit(26, field26, field26.length);

        byte[] field28 = cardInput.getField28().getBytes();//Field 28
        packISO8583.setBit(28, field28, field28.length);

        byte[] field32 = cardInput.getField32().getBytes();//Field 32
        packISO8583.setBit(32, field32, field32.length);

        byte[] field35 = cardInput.getField35().getBytes();//Field 35
        packISO8583.setBit(35, field35, field35.length);

        byte[] field37 = cardInput.getField37().getBytes();//Field 37
        packISO8583.setBit(37, field37, field37.length);

        log.info("Forming Iso Check 2");

//        byte[] field38 = cardInput.getField38().getBytes();//Field 38
//        packISO8583.setBit(38, field38, field38.length);

        byte[] field40 = cardInput.getField40().getBytes();//Field 40
        packISO8583.setBit(40, field40, field40.length);

        byte[] field41 = cardInput.getField41().getBytes();//Field 41
        packISO8583.setBit(41, field41, field41.length);

        byte[] field42 = cardInput.getField42().getBytes();//Field 42
        packISO8583.setBit(42, field42, field42.length);

        byte[] field43 = cardInput.getField43().getBytes();//Field 43
        packISO8583.setBit(43, field43, field43.length);

        byte[] field49 = cardInput.getField49().toString().getBytes();//Field 49
        packISO8583.setBit(49, field49, field49.length);

        log.info("Forming Iso Checking 3");

        
        if(cardInput.getField52() != null && cardInput.getField52().length() > 4)
        {
            byte[] field52 = cardInput.getField52().getBytes();//Field 52
            packISO8583.setBit(52, field52, field52.length);
        }

        if(cardInput.getField55() != null) {
            byte[] field55 = cardInput.getField55().getBytes();//Field 55
            packISO8583.setBit(55, field55, field55.length);
        }

        if(cardInput.getField56() != null){
            byte[] field56 = cardInput.getField56().getBytes();//Field 56
            packISO8583.setBit(56, field56, field56.length);
        }

        if(cardInput.getField59() != null){
            byte[] field59 = cardInput.getField59().getBytes();//Field 59
            packISO8583.setBit(59, field59, field59.length);
        }

        if(cardInput.getField60() != null){
//            byte[] field60 = cardInput.getField60().getBytes();
//            packISO8583.setBit(60, field60, field60.length);
        }

        if(ip.equalsIgnoreCase(cardProperties.getUpip())) {
            byte[] field62 = cardProperties.getUpcustom().getBytes();
            packISO8583.setBit(62, field62, field62.length);
        }

        if(cardInput.getField90() != null){
            byte[] field90 = cardInput.getField90().getBytes();
            packISO8583.setBit(90, field90, field90.length);
        }

        if(cardInput.getField95() != null){
            byte[] field95 = cardInput.getField95().getBytes();
            packISO8583.setBit(95, field95, field95.length);
        }

        byte[] field123 = cardInput.getField123().getBytes();//Field 123
        packISO8583.setBit(123, field123, field123.length);
        byte use = 0x0;
        char ch = (char)use;
        byte[] field128 = Character.toString(ch).getBytes();
        packISO8583.setBit(128, field128, field128.length);
        ISO8583.sec = true;


        byte[] preUnmac = packISO8583.getMacIso();
        byte[] unMac = new byte[preUnmac.length - 64];
        System.arraycopy(preUnmac, 0, unMac, 0, preUnmac.length - 64);

        log.info("ISO BEFORE MAC: " + new String(unMac));
        EncDec enc = new EncDec();
        String gotten = null;
        try {
            log.info("CLEAR SESSION KEY USED: " + clrsesskey);
            gotten = enc.getMacNibss(clrsesskey, unMac);
            log.info("MAC: " + gotten);
        } catch (Exception e) {
            e.printStackTrace();
        }
        field128 = gotten.getBytes();
        packISO8583.setBit(128, field128, field128.length);
        ISO8583.sec = true;
        byte[] packData =  packISO8583.isotostr();

        log.info("ISO TO HOST: " + ISOUtil.hexString(packData));

        log.info("Storing for database sake");
        byte[] getSending = new byte[packData.length - 2];
        System.arraycopy(packData, 2, getSending, 0, packData.length - 2);
        ISO8583.sec = true;
        ISO8583 unpackISO8583  = new ISO8583();
        unpackISO8583.strtoiso(getSending);
        PackIso.sending = new String[128];
        PackIso.receiving = new String[128];
        Utilities.logISOMsgMute(unpackISO8583, PackIso.sending);
        return packData;


    }


}

