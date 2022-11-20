package com.vibee.enums;

import lombok.Getter;

public enum ErrorEnum {
// @formatter:off
	VALIDATION_ERROR("1000", "bl.common.validation-error"),
	ERROR_LOGIN("1000", "vibee.msg.error.login"),
	DUPPLICATE_ENTITY("1001", "bl.common.dupplicate-entity");
// @formatter:on

    @Getter
    private final String messageId;
    @Getter
    private final String code;

    private ErrorEnum(String code, String messageId) {
        this.messageId = messageId;
        this.code = code;
    }
}
