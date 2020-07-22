package com.m3bi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
						.apiInfo(apiInfo()).select().paths(paths()).build();
	}

	private Predicate<String> paths() {
		return Predicates.or(PathSelectors.regex("/user.*"), PathSelectors.regex("/hotel.*"));
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Hotel booking endpoints list")
					.description("Swagger UI for hotel booking")
					.version("1.0").build();
	}
}