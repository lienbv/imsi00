package com.vibee.enums;

import lombok.Getter;

public enum CatalogEnum {
// @formatter:off
	CAT_GENDER("gender"),
	CAT_PROVINCE("province");
// @formatter:on

    @Getter
    private final String sourceId;

    private CatalogEnum(String sourceId) {
        this.sourceId = sourceId;
    }
}
