package io.jomatt.multitenant.sample.jwt.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows; 
import io.swagger.v3.oas.annotations.security.SecurityScheme;
 
import io.swagger.v3.oas.annotations.security.SecurityRequirement; 
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 

@OpenAPIDefinition(
        security = {@SecurityRequirement(name = "security_auth"), @SecurityRequirement(name = "jwt_auth")}, // Global security requirement
        info = @Info(
            title = "Multi-Tenant API",
            description = "user API",
            version = "v1"))
@SecurityScheme(
        name = "security_auth",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(authorizationCode =
                    @OAuthFlow(authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}",
                               tokenUrl = "${springdoc.oAuthFlow.tokenUrl}")))
@SecurityScheme(
        name = "jwt_auth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer")
@Configuration
public class OpenApiConfig {
 

    private static final String[] authPaths = {
    		"/users/**"            
    }; 

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch(authPaths)
                .build();
    }


     
 
}
