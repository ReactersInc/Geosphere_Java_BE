package com.tridev.geoSphere.utils;


import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.response.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseBuilder {

    public static ResponseEntity<BaseResponse> success(Object data, String message) {
        Result result = new Result(HttpStatus.OK.value(), message);
        BaseResponse response = new BaseResponse(data, result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static ResponseEntity<BaseResponse> error(HttpStatus status, String message) {
        Result result = new Result(status.value(), message);
        BaseResponse response = new BaseResponse(null, result);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<BaseResponse> custom(Object data, HttpStatus status, String message) {
        Result result = new Result(status.value(), message);
        BaseResponse response = new BaseResponse(data, result);
        return new ResponseEntity<>(response, status);
    }
}
