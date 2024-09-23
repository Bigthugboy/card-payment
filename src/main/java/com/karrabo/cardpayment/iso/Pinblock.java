package com.karrabo.cardpayment.iso;

import io.netty.handler.codec.DecoderException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;

@Slf4j
public class Pinblock {

    private int fromHex(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        }
        throw new IllegalArgumentException();
    }

    private char toHex(int nybble) {
        if (nybble < 0 || nybble > 15) {
            throw new IllegalArgumentException();
        }
        return "0123456789ABCDEF".charAt(nybble);
    }

    public String generateXorPinBlock(String pin, String cardNumber) throws Exception {
        if(pin.length() < 4 || pin.length() > 6) { // Throw exception here
        }
        //  as an alternative to below pinBlock formation, you can execute: String pinBlock = StringUtils.rightPad("0" + pin, 16, 'F');
        String pinBlock = String.format("%s%d%s", "0", pin.length(), pin);
        while(pinBlock.length() != 16) {
            pinBlock += "F";
        }

        int cardLen = cardNumber.length();
        String pan = "0000" + cardNumber.substring(cardLen - 13, cardLen - 1);
        return xorHex(pinBlock,pan);
    }
    public String xorHex(String a, String b) {
        char[] chars = new char[a.length()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = toHex(fromHex(a.charAt(i)) ^ fromHex(b.charAt(i)));
        }
        return new String(chars).toUpperCase();
    }

    public byte[] encryptData(String clrPinKey, byte[] plainText) throws GeneralSecurityException, DecoderException, org.apache.commons.codec.DecoderException {
        Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Hex.decodeHex(clrPinKey.toCharArray()), "DESede"));
        return cipher.doFinal(plainText);
    }

    public byte[] decryptData(String clrPinKey, byte[] base64EncodedEncryptedData) throws Exception {
        byte[] encryptedData = Base64.decodeBase64(base64EncodedEncryptedData, 0, base64EncodedEncryptedData.length);
        Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Hex.decodeHex(clrPinKey.toCharArray()), "DESede"));
        return cipher.doFinal(encryptedData);
    }



    public String generatePinblock(String clrPin, String cardNumber, String pin){

        log.info("generatePinblock: {} - {} - {}", clrPin,
                cardNumber, pin);

        if(clrPin == null || pin == null || cardNumber == null){
            log.info("Null Inputs for generatePinblock");
            return null;
        }

        try {
            String clrPinblock = new Pinblock().generateXorPinBlock(pin,
                    cardNumber);
            return new EncDec().encryptECB(clrPinblock, clrPin);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getInputtedPin(String clrPin, String pBlock, String cardNumber){

        log.info("getInputtedPin: {} - {} - {}", clrPin,
                pBlock, cardNumber);

        if(clrPin == null || pBlock == null || cardNumber == null){
            log.info("Null Inputs for getInputtedPin");
            return null;
        }

        EncDec encDec = new EncDec();
        try {
            String clrPinblock = encDec.decryptECB(pBlock, clrPin);
            int cardLen = cardNumber.length();
            String pan = "0000" + cardNumber.substring(cardLen - 13, cardLen - 1);
            String pinBlock = new Pinblock().xorHex(clrPinblock, pan);
            log.info("Pin block: {}", pinBlock);
            if(pinBlock.charAt(1) == '4'){
                return pinBlock.substring(2, 6);
            }else if(pinBlock.charAt(1) == '6'){
                return pinBlock.substring(2, 8);
            }else{
                log.info("Not supported");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
            return null;
        }
    }

}
