package com.vibee.model.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetTypeProductResult {
    private int id;
    private String name;
    private int parentId;
    private int statusCode;
    private String statusName;
}
