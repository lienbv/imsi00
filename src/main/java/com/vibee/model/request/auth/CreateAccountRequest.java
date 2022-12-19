package com.vibee.model.request.auth;

import com.vibee.model.item.CredentialItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {
    private String username;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private String email;
    private List<CredentialItem> credentials;
    private List<String> realmRoles;
}
