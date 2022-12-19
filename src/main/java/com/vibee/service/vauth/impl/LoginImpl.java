package com.vibee.service.vauth.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.vibee.config.redis.RedisAdapter;
import com.vibee.entity.VRole;
import com.vibee.entity.VUser;
import com.vibee.model.Status;
import com.vibee.model.info.UserInfo;
import com.vibee.model.request.auth.GetAccessTokenRequest;
import com.vibee.model.response.auth.LoginResponse;
import com.vibee.model.response.auth.TokenResponse;
import com.vibee.repo.VRoleRepo;
import com.vibee.repo.VUserRepo;
import com.vibee.service.vauth.LoginService;
import com.vibee.service.vcall.CallKeycloakService;
import com.vibee.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Log4j2
@Service
public class LoginImpl implements LoginService {
    private final VUserRepo userRepo;
    private final RedisAdapter redisAdapter;
    @Value("${vibee.auth.realm.name}")
    private String clientId;
    @Value("${vibee.auth.client.secret}")
    private String clientSecret;
    private final String BASIC_TOKEN = "Basic ";
    private final CallKeycloakService callKeycloakService;
    private final VRoleRepo roleRepo;

    @Autowired
    public LoginImpl(VUserRepo userRepo,
                     CallKeycloakService callKeycloakService,
                     RedisAdapter redisAdapter,
                     VRoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.callKeycloakService = callKeycloakService;
        this.redisAdapter=redisAdapter;
        this.roleRepo=roleRepo;
    }

    @Override
    public LoginResponse login(String token, String language) {
        log.info("LoginSerivce :: BEGIN");
        String username = "";
        String password = "";
        String role="";
        LoginResponse response = new LoginResponse();
        if (token.startsWith(BASIC_TOKEN)) {
            String account = new String(Base64.getDecoder().decode(token.substring(6)));
            username = account.split(":")[0];
            password = account.split(":")[1];
        }

        VUser user = this.userRepo.findByUsername(username);
        if (user == null) {
            log.error("login is fail :: username=" + username + "\t password: " + password);
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.login.is.failed"));
            return response;
        }
        GetAccessTokenRequest request = new GetAccessTokenRequest();
        request.setPassword(password);
        request.setUsername(username);
        Map<String, String> requestXpres = new HashMap<>();
        requestXpres.put("grant_type", "password");
        requestXpres.put("client_secret", clientSecret);
        requestXpres.put("client_id", clientId);
        requestXpres.put("username", username);
        requestXpres.put("password", password);
        TokenResponse tokenResponse = this.callKeycloakService.getAccessToken(requestXpres);
        if (tokenResponse == null) {
            log.error("call api keycloak is fail :: username=" + username + "\t password: " + password);
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.login.is.failed"));
            return response;
        }
//        try {
//            DecodedJWT jwt = JWT.decode(tokenResponse.getAccess_token().replace("Bearer", "").trim());
//
//            // check JWT role is correct
//            List<String> roles = ((List)jwt.getClaim("realm_access").asMap().get("roles"));
//            role=roles.get(0);
//            // check JWT is still active
//            Date expiryDate = jwt.getExpiresAt();
//            if(expiryDate.before(new Date()))
//                throw new Exception("token is expired");
//
//        } catch (Exception e) {
//            log.error("exception : {} ", e.getMessage());
//            response.getStatus().setStatus(Status.Fail);
//            response.getStatus().setMessage(MessageUtils.get(language, "msg.login.is.failed"));
//            return response;
//        }
        UserInfo info = new UserInfo();
        info.setUsername(user.getUsername());
        response.getStatus().setMessage(MessageUtils.get(language, "msg.login.is.success"));
        response.getStatus().setStatus(Status.Success);
        response.setAccessToken(tokenResponse.getAccess_token());
        response.setRefreshToken(tokenResponse.getRefresh_token());
        response.setUsername(user.getUsername());
        role=this.roleRepo.findByUserId(user.getId());
        response.setRole(role);
        if (tokenResponse.getAccess_token() != null) {
            String key = "expireToken::" + tokenResponse.getAccess_token().hashCode();
            this.redisAdapter.set(key, 3600, response);
            response=this.redisAdapter.get(key,LoginResponse.class);
        }
        log.info("LoginSerivce :: END");
        return response;
    }
}
