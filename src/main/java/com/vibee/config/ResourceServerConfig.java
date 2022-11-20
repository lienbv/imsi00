package com.vibee.config;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;

import com.vibee.config.redis.RedisAdapter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends GlobalMethodSecurityConfiguration {
	private final RedisAdapter redisAdapter;
	@Autowired
	public ResourceServerConfig(RedisAdapter redisAdapter){
		this.redisAdapter=redisAdapter;
	}
	@Bean
	Converter<Jwt, Collection<GrantedAuthority>> customerJwtGrantedAuthoritiesConverter() {
		return new CustomJwtGrantedAuthoritiesConverter();
	}

	@Bean
	JwtAuthenticationConverter customJwtAuthenticationConverter() {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(customerJwtGrantedAuthoritiesConverter());
		return converter;
	}

	@Bean
	SecurityFilterChain oauth2AuthFilterChain(HttpSecurity http) throws Exception {
// @formatter:off
		http.addFilterBefore(new SimpleCORSFilter(), DisableEncodeUrlFilter.class);
		http.addFilterBefore(new SimpleCORSFilter(), ChannelProcessingFilter.class);
        http.authorizeRequests().anyRequest().authenticated().and().addFilterAfter(new CustomTokenFilter(this.redisAdapter), BearerTokenAuthenticationFilter.class).logout()
    	.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    	.and().oauth2ResourceServer().jwt().jwtAuthenticationConverter(customJwtAuthenticationConverter());
		return http.build();
	}

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/swagger-ui.html/**",
				                        "/swagger-resources/**",
				                        "/webjars/**",
				                       "/swagger-ui.html#!/**",
				                        "/v2/**",
				                        "/actuator/**",
				                        "/",
				                       "/vibee/api/v1/auth/**");
	}
	
}
