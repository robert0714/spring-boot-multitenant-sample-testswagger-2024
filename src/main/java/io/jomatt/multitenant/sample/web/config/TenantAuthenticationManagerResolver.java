package io.jomatt.multitenant.sample.web.config;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter;
import org.springframework.stereotype.Component;

import io.quantics.multitenant.TenantContext; 
import io.quantics.multitenant.tenantdetails.TenantDetailsService;

//@Component
public class TenantAuthenticationManagerResolver  implements AuthenticationManagerResolver<HttpServletRequest> {

	private static final Map<String,AuthenticationManager> authenticationManagers = new ConcurrentHashMap<>();
	private final TenantDetailsService tenantDetailsService;

	public TenantAuthenticationManagerResolver(TenantDetailsService tenantDetailsService) {
        this.tenantDetailsService = tenantDetailsService;
	}

	@Override
	public AuthenticationManager resolve(HttpServletRequest request) {
		var tenantId = TenantContext.getTenantId();
		return authenticationManagers.computeIfAbsent(tenantId, this::buildAuthenticationManager);
//		return authenticationManagers.computeIfAbsent(tenantId, this::buildJwtAuthenticationManager); 
	}

	private AuthenticationManager buildAuthenticationManager(String tenantId) {
		var issuerUri = tenantDetailsService.getById(tenantId).get().getIssuer();
		var jwtAuthenticationprovider = new JwtAuthenticationProvider(JwtDecoders.fromIssuerLocation(issuerUri));
		return jwtAuthenticationprovider::authenticate;
	}
	
	// Mimic the default configuration for JWT validation.	
	private AuthenticationManager  buildJwtAuthenticationManager(String tenantId) {
	
	    // this is the keys endpoint for okta
	    var issuerUri = tenantDetailsService.getById(tenantId).get().getIssuer();
	    
	    // see http://localhost:8080/realms/dukes/.well-known/openid-configuration
	    String jwkSetUri = issuerUri + "/protocol/openid-connect/certs";
	    
	    // This is basically the default jwt logic	
	    JwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
	    
	    JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
	 
	    authenticationProvider.setJwtAuthenticationConverter(new JwtBearerTokenAuthenticationConverter());
	 
	    return authenticationProvider::authenticate;	 
	}
	    
	 

}

