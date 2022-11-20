package com.vibee.enums;

import lombok.Getter;

public enum HeaderEnum {
    CLIENT_MESSAGE_ID("clientMessageId"), CLIENT_TIME("clientTime");

    @Getter
    private final String label;

    private HeaderEnum(String label) {
        this.label = label;
    }
}
