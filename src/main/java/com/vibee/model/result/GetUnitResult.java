package com.vibee.model.result;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetUnitResult {
    private int id;
    private String name;
    private String description;
    private int statusCode;
    private String statusName;
    private int parentId;
}
