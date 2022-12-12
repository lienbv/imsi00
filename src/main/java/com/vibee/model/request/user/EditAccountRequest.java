package com.vibee.model.request.user;

import com.vibee.model.request.BaseRequest;
import lombok.Data;

@Data
public class EditAccountRequest extends BaseRequest {
    private Integer idUserRoles;
}
