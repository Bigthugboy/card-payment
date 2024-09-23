package com.karrabo.cardpayment.utils;

public class Constants {
    private Constants() {
        throw new IllegalStateException("Constants class");
    }
    public static final String COVA_VERSION = "1.0.1";
    public static final String APP_VERSION = "v1";
    public static final String SUCCESS = "Success";
    public static final String FAILED = "Failed";
    public static final Boolean Success = true;
    public static final Boolean Failed = false;
    public static final long JWT_TOKEN_EXPIRE_IN_SECONDS = 60 * 60;//60 minutes
    public static final long JWT_KUDA_TOKEN_EXPIRE_IN_SECONDS = 59 * 60;//59 mins
    public static final String JWT_TOKEN_SECRET = "COVAKARRA3CC519ADF0D91EC5BFB32719OLIVER61031135F3956514BRIGHT###";
    public static final String JWT_ISSUER = "Karrabo Cova";
    public static final String EMAIL_TYPE_INCORRECT_DETAILS = "INCORRECT DETAILS";
    public static final String EMAIL_TYPE_SUCCESSFUL_LOGIN = "SUCCESSFUL LOGIN";
    public static final String EMAIL_TYPE_SUCCESSFUL_SIGNUP = "SUCCESSFUL SIGNUP";
    public static final String EMAIL_TYPE_PASSWORD_TOKEN = "PASSWORD TOKEN";
    public static final String EMAIL_TYPE_SUCCESSFUL_INFLOW = "SUCCESSFUL INFLOW";
    public static final String CONTACT_EMAIL = "commercial@karrabo.com";
    public static final String CONTACT_PHONE = "09088910002";
    public static final String PROJECT_NAME = "COVA";
    public static final String EMAIL_LAST_MESSAGE = "KARRABO COVA";
    public static final String NO_REDIRECT = "";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    public static final int SUCCESS_CODE = 200;
    public static final int FAILURE_CODE = 500;
    public static final int PASSWORD_EXPIRY_IN_MONTHS = 3;
    public static final int FORGET_PASSWORD_RECOVERY_TIME = 72;
    public static final String CREATEDBY = "SYSTEM CREATED";

    public static final int EMAIL_TOKEN_LENGTH = 10;

    public static final int PRIVATE_KEY_LENGTH = 16;
    public static final int PUBLIC_KEY_LENGTH = 32;
    public static final int VERIFIED = 1;
    public static final int NOT_VERIFIED = 0;
    public static final int RETRY_DEFAULT = 0;
    public static final int PARENT_NODE = 1;
    public static final int CHILD_NODE = 0;
    public static final int SYSTEM_ID_LENGTH = 10;
    public static final int WALLET_ID_LENGTH = 7;
    public static final int LOGIN_RETRY_LIMIT = 5;
    public static final int ERROR_CODE = 500;
    public static final int NOT_FOUND_CODE = 404;
    public static final int UNKNOWN_ERROR_CODE = 501;
    public static final int DATA_INPUT_LENGTH = 80;
}
