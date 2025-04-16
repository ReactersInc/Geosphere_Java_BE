package com.tridev.geoSphere.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tridev.geoSphere.constant.CommonValidationConstant;
import com.tridev.geoSphere.config.ValidationFailureException;
import com.tridev.geoSphere.enums.ApplicationError;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.response.ErrorBaseResponse;
import com.tridev.geoSphere.response.InputKeyError;
import com.tridev.geoSphere.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


@RestControllerAdvice
@Slf4j
@Component("globalException")
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
    @SuppressWarnings("java:S2259")
    public ResponseEntity<BaseResponse> resourceNotFoundExceptionHandler(
            ResourceNotFoundException ex, WebRequest request) {
        log.error("resourceNotFoundExceptionHandler {} ", ex);
        BaseResponse baseResponse = new BaseResponse();
        Result result = new Result();
        if(ApplicationError.resolve(404)!=null) {
	        result.setResponseCode(ApplicationError.resolve(404).getCode());
	        result.setResponseDescription(ApplicationError.resolve(404).getCodeString());
        }
        baseResponse.setResult(result);
        return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleMethodArgsNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        log.error("MethodArgumentNotValidException {} ", ex);
        BaseResponse baseResponse = new BaseResponse();
        Result result = new Result();
        BindingResult bindingResult = ex.getBindingResult();
        String responseDescription = bindingResult.getFieldErrors().get(0).getDefaultMessage();
        ApplicationError code = ApplicationError.getApplicationErrorByReason(responseDescription);
        result.setResponseCode(code.getCode());
        result.setResponseDescription(responseDescription);
        baseResponse.setResult(result);
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FieldNullOrBlankException.class)
    public BaseResponse handleFieldNullOrBlankException(FieldNullOrBlankException ex,
                                                        WebRequest request) {
        log.error("handleBadRequestException {} ", ex);
        BaseResponse baseResponse = new BaseResponse();
        Result result = new Result();
        log.info("error {}", ex.getMessage());
        result.setResponseCode(ApplicationError.getCodeByReason(ex.getMessage()));
        result.setResponseDescription(ex.getMessage());
        baseResponse.setResult(result);
        return baseResponse;
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public BaseResponse handleBadRequestException(BadRequestException ex, WebRequest request) {
        log.error("handleBadRequestException {} ", ex);
        Result result = new Result();
        String responseDescription = ex.getMessage();
        ApplicationError code = ApplicationError.getApplicationErrorByReason(responseDescription);
        result.setResponseCode(code.getCode());
        result.setResponseDescription(responseDescription);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(result);
        return baseResponse;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    public BaseResponse invalidFormatException(InvalidFormatException ex, WebRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        Result result = new Result();
        log.error("invalid format {} ", ex);
        String responseDescription = ex.getMessage();
        ApplicationError code = ApplicationError.getApplicationErrorByReason(responseDescription);
        result.setResponseCode(code.getCode());
        result.setResponseDescription(responseDescription);
        baseResponse.setResult(result);
        return baseResponse;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        Result result = new Result();
        log.error("exception {}", ex);
        ApplicationError applicationError = ApplicationError.getApplicationErrorByReason(CommonValidationConstant.INVALID_REQUEST_EXCEPTION);
        result.setResponseCode(applicationError.getCode());
        result.setResponseDescription(ex.getMessage());
        baseResponse.setResult(result);
        return baseResponse;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidInputException.class)
    public BaseResponse handleInvalidInputException(InvalidInputException ex, WebRequest request) {
        log.error("handleInvalidInputException {} ", ex);
        BaseResponse baseResponse = new BaseResponse();
        Result result = new Result();
        result.setResponseCode(ApplicationError.getCodeByReason(ex.getMessage()));
        result.setResponseDescription(ex.getMessage());
        baseResponse.setResult(result);
        return baseResponse;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        log.error("handleIllegalArgumentException {} ", ex);
        BaseResponse baseResponse = new BaseResponse();
        Result result = new Result();
        result.setResponseCode(100016);
        result.setResponseDescription(ex.getMessage());
        baseResponse.setResult(result);
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<BaseResponse> handleConstraintViolationException(
//            ConstraintViolationException ex, WebRequest request) {
//        String message = null;
//        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
//            message = violation.getMessage();
//        }
//        BaseResponse baseResponse = new BaseResponse();
//        Result result = new Result();
//        ApplicationError applicationError = ApplicationError.getApplicationErrorByReason(message);
//        result.setResponseCode(applicationError.getCode());
//        result.setResponseDescription(applicationError.getReason());
//        baseResponse.setResult(result);
//        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex, WebRequest request) {
        String message = ex.getMessage();
        log.error("handleMissingServletRequestParameterException {}", message);
        BaseResponse baseResponse = new BaseResponse();
        Result result = new Result();
        result.setResponseCode(100014);
        result.setResponseDescription(message);
        baseResponse.setResult(result);
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<BaseResponse> handleFileSizeLimitExceededException(
            FileSizeLimitExceededException ex, WebRequest request) {
        String message = ex.getMessage();
        log.error("handleFileSizeLimitExceededException {}", message);
        BaseResponse baseResponse = new BaseResponse();
        Result result = new Result();
        result.setResponseCode(100070);
        result.setResponseDescription(message);
        baseResponse.setResult(result);
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }


//    @ExceptionHandler(UnexpectedTypeException.class)
//    @SuppressWarnings("java:S2259")
//    public ResponseEntity<BaseResponse>  handleValidationExceptions(
//            MethodArgumentNotValidException ex) {
//        String message = ex.getMessage();
//        log.error("handleValidationExceptions {}", message);
//        BaseResponse baseResponse = new BaseResponse();
//        Result result = new Result();
//        if(ApplicationError.resolve(404)!=null) {
//	        result.setResponseCode(ApplicationError.resolve(404).getCode());
//	        result.setResponseDescription(ApplicationError.resolve(404).getCodeString());
//        }
//        baseResponse.setResult(result);
//        return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
//    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<BaseResponse> handleBindException(BindException ex, WebRequest request){
        String message = ex.getMessage();
        BaseResponse baseResponse = new BaseResponse();
        Result result = new Result();
        result.setResponseCode(400);
        result.setResponseDescription(message);
        baseResponse.setResult(result);
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InputFieldMissingException.class)
    public ResponseEntity<ErrorBaseResponse> handleInputFieldMissingException(InputFieldMissingException ex, WebRequest request){
        ErrorBaseResponse errBaseResponse = new ErrorBaseResponse();
        InputKeyError result=new InputKeyError();
        result.setKey(ex.getMessage());
        result.setResponseCode(400);
        result.setErrorDescription("Above key is mandatory");
        errBaseResponse.setResult(result);
        return new ResponseEntity<>(errBaseResponse, HttpStatus.BAD_REQUEST);
    }
    


    @ExceptionHandler(InvalidInputRegexException.class)
    public ResponseEntity<ErrorBaseResponse> handleInvalidInputRegexException(InvalidInputRegexException ex){
        ErrorBaseResponse errBaseResponse = new ErrorBaseResponse();
        InputKeyError result=new InputKeyError();
        result.setKey(ex.getMessage());
        result.setResponseCode(400);
        result.setErrorDescription("Above key is Invalid");
        errBaseResponse.setResult(result);
        return new ResponseEntity<>(errBaseResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnAuthorizedAccessException.class)
    protected ResponseEntity<Object> handleUnAuthorizedAccessException(UnAuthorizedAccessException ex) {
        log.error("UnAuthorizedAccessException: {}", ex);
        Result result = new Result();
        result.setResponseCode(401);
        result.setResponseDescription("UnAuthorized");
        return new ResponseEntity<>(result,
                HttpStatus.UNAUTHORIZED);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    protected BaseResponse handleRuntimeException(RuntimeException ex) {
        log.error("handleRuntimeException {} ", ex);
        Result result = new Result();
        result.setResponseCode(10000);
        result.setResponseDescription("Runtime Exception : Please connect with IT Team");
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(result);
        return baseResponse;
    }




    @ExceptionHandler(ValidationFailureException.class)
    protected ResponseEntity<Result> handleValidationFailureException(ValidationFailureException ex) {
        log.error("handleValidationFailureException {} {}", ex.getErrorCode(), ex.getErrorDescription());
        Result result = new Result();
        result.setResponseCode(Integer.parseInt(ex.getErrorCode()));
        result.setResponseDescription(ex.getErrorDescription());
        return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
    }




}
