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


    public static boolean isCreditScoreRequired(LocalDateTime creditScoreExpDate) {

        if (creditScoreExpDate == null) {
            return true;
        }
        LocalDateTime currentDate = LocalDateTime.now();

        // Returns false if current date is less than creditScoreExpDate, else true
        return currentDate.isAfter(creditScoreExpDate);
    }

    public static String excelSheetNameGenerator(){

        Instant timestamp = Instant.now();
        OffsetDateTime utc = timestamp.atOffset(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String timestampAsString = utc.format(formatter);

        return ("Gasolina-"+timestampAsString+".xlsx");
    }

    public static String accountNoGenerator(String name, Long clientId){

        String alphabet = "1234567890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }

        String randomString = sb.toString();

        long timestamp = Instant.now().getEpochSecond();
        if(!ObjectUtils.isEmpty(clientId)){
            return name+clientId.toString()+timestamp+randomString;
        }

        return name+timestamp+randomString;
    }

    public static String tempFileNameGenerate(){

        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }

        String randomString = sb.toString();

        Instant timestamp = Instant.now();
        OffsetDateTime utc = timestamp.atOffset(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String timestampAsString = utc.format(formatter);

        return ("agreement"+timestampAsString+randomString);
    }


    public static String generateUUID() {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();
        // Convert the UUID to a string
        String uuidString = uuid.toString();
        // Define the length of the desired string
        int desiredLength = 128;
        // Generate a string with additional characters to reach the desired length
        StringBuilder extendedString = new StringBuilder(uuidString);
        while (extendedString.length() < desiredLength) {
            extendedString.append(UUID.randomUUID().toString());
        }
        // If the string is longer than desired, trim it
        String finalString = extendedString.substring(0, desiredLength);
        return finalString;
    }

    public static String fileNameGenerate(){

        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }

        String randomString = sb.toString();

        Instant timestamp = Instant.now();
        OffsetDateTime utc = timestamp.atOffset(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String timestampAsString = utc.format(formatter);

        return ("gasolina-"+timestampAsString+randomString);
    }

    public static String dateTimeFormatter(LocalDateTime timestamp){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM, yyyy HH:mm:ss");
        return timestamp.format(formatter);
    }

    public static byte[] toByteArray(ByteArrayInputStream input) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int nRead;
        while ((nRead = input.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
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
