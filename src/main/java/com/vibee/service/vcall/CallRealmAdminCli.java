package com.vibee.service.vcall;

import com.vibee.config.auth.AuthenticationAdminRealmConfig;
import com.vibee.model.item.CredentialItem;
import com.vibee.model.request.auth.CreateAccountRequest;
import com.vibee.model.response.auth.TokenResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Component
@FeignClient(name="CallRealmAdminCli", url = "${integration.keycloak.url}",path = "/auth/admin/realms/${vibee.auth.realm.name}", configuration = {AuthenticationAdminRealmConfig.class})
public interface CallRealmAdminCli {

    @PostMapping(value="/users")
    void createAccount(@RequestBody CreateAccountRequest request)throws Exception;

    @PostMapping(value="/groups/{id}/role-mappings/realm",consumes = "application/x-www-form-urlencoded")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    void setRoleByUser(@PathVariable("id") String userId, @RequestBody Map<String, ?> request)throws Exception;

    @PutMapping(value="/users/{id}/reset-password")
    void resetPassword(@PathVariable("id") String userId, @RequestBody CredentialItem item)throws Exception;
}
