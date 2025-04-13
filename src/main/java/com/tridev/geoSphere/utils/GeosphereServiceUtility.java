package com.tridev.geoSphere.utils;

import com.tridev.geoSphere.constant.RSConstant;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.response.Result;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeosphereServiceUtility {
    public static Result getResult(){
        return new Result(RSConstant.RESPONSE_CODE, RSConstant.RESPONSE_DESCRIPTION);
    }

    public static BaseResponse getBaseResponse(Object data){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(data);
        baseResponse.setResult(getResult());
        return baseResponse;
    }

    public static BaseResponse getBaseResponseWithoutData(){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(getResult());
        return baseResponse;
    }

    public static boolean matchesRegex(String input, String regex) {
        if (input == null) return false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }


    public static String dateTimeFormatter(LocalDateTime timestamp){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM, yyyy HH:mm:ss");
        return timestamp.format(formatter);
    }
    public static String generateOtp(){
        String digits = "0123456789";
        StringBuilder sb = new StringBuilder();
        Random randomKey = new Random();

        for (int i = 0; i < 4; i++) {
            int index = randomKey.nextInt(digits.length());
            char randomChar = digits.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

}
