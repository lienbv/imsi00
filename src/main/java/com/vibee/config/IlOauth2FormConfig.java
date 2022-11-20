package com.vibee.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import feign.RequestInterceptor;

/**
 * @author manhpt
 * @Date 2022
 */
@EnableFeignClients
public class IlOauth2FormConfig {

    @Value("${bl.keycloak.accessTokenUri}")
    private String accessTokenUri;
    @Value("${bl.keycloak.username}")
    private String coreAccount;
    @Value("${bl.keycloak.secret}")
    private String coreSecret;


    @Bean
    RequestInterceptor oauth2FeignRequestInterceptor() {
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), resource());
    }

    private OAuth2ProtectedResourceDetails resource() {
        var resourceDetails = new ClientCredentialsResourceDetails();
        resourceDetails.setAccessTokenUri(accessTokenUri);
        resourceDetails.setClientId(coreAccount);
        resourceDetails.setClientSecret(coreSecret);
        resourceDetails.setGrantType("client_credentials");
        return resourceDetails;
    }

}
