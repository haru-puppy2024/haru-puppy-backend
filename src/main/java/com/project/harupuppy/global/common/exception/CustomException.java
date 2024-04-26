package com.project.harupuppy.global.common.exception;

import com.project.harupuppy.global.common.response.Response;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final Response.ErrorCode errorCode;

    public CustomException(Response.ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomException(Response.ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}
