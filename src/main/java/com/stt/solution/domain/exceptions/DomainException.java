package com.stt.solution.domain.exceptions;

import lombok.Getter;

@Getter
public class DomainException extends Exception {

    public DomainException(ErrorCode code) {
        super("Error Code: " + code.toString());
        this.code = code;
    }

    public DomainException(String message, ErrorCode code) {
        super(message);
        this.code = code;
    }

    private final ErrorCode code;
}

