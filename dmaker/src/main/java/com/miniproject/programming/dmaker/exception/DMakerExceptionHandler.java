package com.miniproject.programming.dmaker.exception;


import com.miniproject.programming.dmaker.dto.DMakerErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.miniproject.programming.dmaker.exception.DMakerErrorCode.INTERNAL_SERVER_ERROR;
import static com.miniproject.programming.dmaker.exception.DMakerErrorCode.INVALID_REQUEST;

@Slf4j
@RestControllerAdvice
public class DMakerExceptionHandler {


    @ExceptionHandler(DMakerException.class)
    public DMakerErrorResponse handleException(DMakerException e, HttpServletRequest request) {
        log.error("errorCode: {}, url: {}, message: {}", e.getDMakerErrorCode(), request.getRequestURI(), e.getDetailMessage());
        return DMakerErrorResponse.builder().errorCode(e.getDMakerErrorCode()).errorMessage(e.getDetailMessage()).build();
    }

    //DMakerException만으로는 예외처리가 불가능할 경우 더 추가해줄 수 있다.
    @ExceptionHandler(value = {
            //컨트롤러의 매핑이랑 매치가 안됐을 때(POST인데 GET을 한 경우)
            HttpRequestMethodNotSupportedException.class,
            //Request안에 validation을 하고 있는데 Validation에서 문제가 나왔을 때 나오는 예외이다.
            MethodArgumentNotValidException.class
    })
    public DMakerErrorResponse handleBadRequest(
            Exception e, HttpServletRequest request
    ) {
        log.error("url: {}, message: {}", request.getRequestURI(), e.getMessage());
        return DMakerErrorResponse.builder().errorCode(INVALID_REQUEST).errorMessage(INVALID_REQUEST.getMessage()).build();
    }

    //최후의 보루로 모든 예외처리를 받는 Exception
    //진짜 모르는 예외처리일 경우
    //되도록 이런 에러는 좋지 않기 때문에 이런 에러가 뜨면 try catch나 DMakerErrorResopnse를 만들어 좀 더 구체적인 오류 메시지를 만드는 것이 좋다.
    @ExceptionHandler(Exception.class)
    public DMakerErrorResponse handleException(
            Exception e, HttpServletRequest request
    ){
        log.error("url: {}, message: {}", request.getRequestURI(), e.getMessage());
        return DMakerErrorResponse.builder().errorCode(INTERNAL_SERVER_ERROR).errorMessage(INTERNAL_SERVER_ERROR.getMessage()).build();
    }
}
