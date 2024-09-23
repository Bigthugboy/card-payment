package com.karrabo.cardpayment.iso;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Slf4j
public class Utilities {

    public static String formatExp(String dat)
    {
        if(dat == null)
            return "**/**";
        if(dat.length() < 1)
            return "**/**";
        String db = dat.substring(2, 4) + "/" + dat.substring(0, 2);
        return db;
    }

    public static byte[] StrToHexByte(String str) {
        if (str == null)
            return null;
        else if (str.length() < 2)
            return null;
        else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(
                        str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    public static String getField4(String amountStr) {
        log.info("begin amount: " + amountStr);
        int index = amountStr.indexOf(".");
        if (amountStr.substring(index + 1, amountStr.length()).length() < 2) {
            amountStr = amountStr + "0";
        }
        amountStr = amountStr.replace(".", "");
        int amtlen = amountStr.length();
        StringBuilder amtBuilder = new StringBuilder();
        if (amtlen < 12) {
            for (int i = 0; i < (12 - amtlen); i++) {
                amtBuilder.append("0");
            }
        }
        amtBuilder.append(amountStr);
        amountStr = amtBuilder.toString();
        log.info("begin amount: " + amountStr);
        return amountStr;
    }

    public static String bytes2HexString(byte[] b) {
        String ret = "";
        for (byte element : b) {
            String hex = Integer.toHexString(element & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }


    public static String getSerialNumber() {
        String serialNumber = "00005555";
        return serialNumber;
    }

    
    public static void logISOMsg(ISO8583 msg, String[] storage)
    {
        log.info("----ISO MESSAGE-----");
        try {
            for (int i = 0; i < 129; i++)
            {
                try
                {
                    String slog = new String(msg.getBit(i));
                    if (slog != null)
                    {
                        log.info("Field " + i + " : " + slog);
                        storage[i] = slog;
                    }
                }catch (Exception e)
                {
                    //Do nothing about it
                }
            }
        } finally {
            log.info("--------------------");
        }

    }

    public static String maskedPan(String cardNo)
    {
        if(cardNo == null)
            return "*****";
        int cardLength = cardNo.length();
        String firCardNo = cardNo.substring(0,6);
        String lastCardNo = cardNo.substring(cardLength - 4);
        String mid = "******";
        return (firCardNo + mid + lastCardNo);
    }

    public static String getDATEYYYYMMDD(String date)
    {
        if(date == null)
            return "*****";
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String format = String.valueOf(year) + date;
        return format;
    }

    public static void logISOMsgMute(ISO8583 msg, String[] storage)
    {
        //logger.info("----ISO STORAGE-----");
        try {
            for (int i = 0; i < 129; i++)
            {
                try
                {
                    String log = new String(msg.getBit(i));
                    if (log != null)
                    {
                        storage[i] = log;
                    }
                }catch (Exception e)
                {
                    //Do nothing about it
                }
            }
        } finally {
            //logger.info("--------------------");
        }

    }


    public static byte[] getCustomPacketHeader(byte[] isobyte) {
        String cs = String.format("%04X", ISO8583Util.byteArrayAdd(isobyte, null).length);
        byte[] bcdlen = BCDASCII.hexStringToBytes(cs);
        log.info("Length 2: " + bcdlen.length);
        return ISO8583Util.byteArrayAdd(bcdlen, ISO8583Util.byteArrayAdd(null, null, null), isobyte);
    }

    public static String getCurrentTime()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");
        return sdf.format(cal.getTime());
    }
}
