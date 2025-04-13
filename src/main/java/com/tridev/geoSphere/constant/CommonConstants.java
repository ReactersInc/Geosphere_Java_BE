package com.tridev.geoSphere.constant;

public class CommonConstants {

    //regex common expression
    public static final String UAE_COUNTRY_CODE_REGEX = "\\+\\d{1,4}";
    public static final String PRODUCT_ID_REGEX = "^[0-9]*$";
    public static final String ORDER_ID_REGEX = "^[0-9A-Za-z]*$";
    public static final String STORE_ID_REGEX = "^[0-9]{1,}$";
    public static final String COMMON_REGEX = "^[0-9]*$";
    public static final String AMOUNT_REGEX = "^[0-9]\\d*(\\.\\d+)?$";
    public static final String IS_ORDERED = "1";
    public static final String PHONENO_REGEX ="^\\d{9}$";
    public static final String TRN_NUMBER_REGEX="^100\\d{12}$";
    public static final String ORN_NUMBER_REGEX="^[a-zA-Z0-9]*$";
    public static final String BRN_NUMBER_REGEX ="^[a-zA-Z0-9]*$";

    public static final String FNAME_REGEX = "^[A-Za-z0-9\\s]+$";

    public static final String LNAME_REGEX = "^[A-Za-z\\s0-9]*$";

    public  static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static final String CREATED_BY_REGEX = "^[A-Za-z][A-Za-z'-]*$";

    public static final String APPLICATION_NO_REGEX = "APP\\d{17}[a-zA-Z]{5}";

    public static final String APPLICATION_JSON = "application/json";
    public static final String DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";

    public static final String SORT_ORDER_REGEX = "^(ASC|DESC)$";


    //JWT extractor
    public static final String END_USER_CLAIMS_PATH = "http://wso2.org/claims/enduser";
    public static final String AT_THE_RATE_SEPARATOR = "@";
    public static final String FORWARD_SLASH_SEPARATOR = "/";
    public static final String INVALID_JWT = "Invalid JWT Token";
    public static final String MISSING_JWT = "JWT Token not present in request";
    public static final String END_USER = "endUser";
    public static final String REQUEST = "requestId";
    public static final String X_JWT_HEADER = "X-JWT-Assertion";
    public static final String USER_ID = "userid";
    public static final String USER_NAME = "userName";

    public static final String ROLE = "role";
    public static final String AUTHORITIES = "authorities";
    public static final String ACCEPT = "Accept";
    public static final String INITIATOR = "initiator";
    public static final String Authorization = "Authorization";
    public static final String BELGIAN_DATE_FORMATTER = "HH:mm:ss dd-MMM-yyyy z";

    public static final String ACTIVE = "Active";
    public static final String CART = "Cart";
    public static final String SUCCESS_RESPONSE_CODE = "0";
    public static final String SUCCESS_RESPONSE_DESCRIPTION = "Success";
    public static final Boolean PRODUCT_IN_STOCK = true;
    public static final String REMOVAL_REASON = "Product deleted from cart";


    public static final String ERROR_CODE_MISSING = "Error Code not present in the system";
    public static final String DATE_FORMAT_WITH_SEC = "yyyy-MM-dd HH:mm:ss";
    public static final String FAILUER_CODE = "-1";
    public static final String FAILUER_DESCRIPTION = "Mismatch in stock or price calculation";
    public static final String ACTIVE_STATUS_CODE = "2";
    public static final String STORE = "Online";
    public static final String ZERO_VALUE = "0";
    public static final String TENANT_HEADER = "X-TenantID";
    public static final String TENANT_WM = "OPL_WM";
    public static final String CART_ACTION_AT = "cartActionAt";
    public static final int PAGE_NO = 0;
    public static final Boolean PRESERVE_CONSTANT = true;
    public static final String ANY_COUNTRY = "ANY";
    public static final String COMMA_SEPRATOR = " , ";
    public static final Boolean NOT_ACTIVE = false;

}
