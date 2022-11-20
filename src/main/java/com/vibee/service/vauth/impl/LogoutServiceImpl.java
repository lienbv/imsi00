package com.vibee.service.vauth.impl;

import com.vibee.config.redis.RedisAdapter;
import com.vibee.model.Status;
import com.vibee.model.response.BaseResponse;
import com.vibee.service.vauth.LogoutService;
import com.vibee.service.vcall.CallKeycloakService;
import com.vibee.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class LogoutServiceImpl implements LogoutService {

    private final RedisAdapter redisAdapter;
    private final CallKeycloakService callKeycloakService;

    @Autowired
    public LogoutServiceImpl(RedisAdapter redisAdapter,
                             CallKeycloakService callKeycloakService){
        this.redisAdapter=redisAdapter;
        this.callKeycloakService=callKeycloakService;
    }
    @Override
    public BaseResponse logout(String refreshToken, String token, String language) {
        String key="accessToken::"+token;
        BaseResponse response=new BaseResponse();
        if (!redisAdapter.exists(key)){
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language,"msg.token.is.not.exist"));
            return response;
        }

        return null;
    }
}
