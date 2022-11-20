package com.vibee.config.exception;

import com.vibee.enums.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorException extends Exception{
    private ErrorEnum error;
    private String additionalData;
    private static final long serialVersionUID = 8442358956304045349L;
}
