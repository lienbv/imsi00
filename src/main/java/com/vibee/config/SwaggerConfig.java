package com.vibee.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
//													  .apis(RequestHandlerSelectors.basePackage("com.mbbank.main"))
				.apis(RequestHandlerSelectors.any())
				.build()
				.securityContexts(Collections.singletonList(securityContext()))
				.securitySchemes(Collections.singletonList(securitySchema()))
				.apiInfo(metaData());
	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("Vibee REST API")
				.description("Vibee REST API: Vibee").version("1.0")
				.license("Vibee Version 1.0")
				.licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
				.contact(new Contact("Integration Team", "", "lambcph16980@fpt.edu.vn")).build();
	}


	private SecurityScheme securitySchema() {
		return new ApiKey("api_key", "Authorization", "Header");
	}

	private SecurityReference securityReference() {
		return new SecurityReference("api_key", new AuthorizationScope[0]);
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(Collections.singletonList(securityReference())).build();
	}


}
