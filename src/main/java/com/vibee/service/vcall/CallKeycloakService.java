package com.vibee.service.vcall;

import com.vibee.model.request.auth.GetAccessTokenRequest;
import com.vibee.model.response.auth.TokenResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Component
@FeignClient(name="CallKeycloak", url = "${integration.keycloak.url}",path = "/realms/${vibee.auth.realm.name}/protocol/openid-connect")
public interface CallKeycloakService {
    @PostMapping(value="/token",consumes = "application/x-www-form-urlencoded")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    public TokenResponse getAccessToken(@RequestBody Map<String, ?> request);

    @PostMapping("/logout")
    public void logout(@RequestBody GetAccessTokenRequest request);
}
