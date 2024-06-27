package io.jomatt.multitenant.sample.cache;
 
 
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
 
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import io.jomatt.multitenant.sample.common.config.CurrentTenantResolver; 
import io.quantics.multitenant.TenantContext; 
import io.quantics.multitenant.tenantdetails.TenantSchemaDetailsService;

@Configuration(proxyBeanMethods = false)
public class TenantSchemaCacheConfig {
	private TenantSchemaDetailsService tenantService ;
	
	public TenantSchemaCacheConfig( TenantSchemaDetailsService tenantService) {
		this.tenantService = tenantService;
	}
	@Bean
	public Map<String,String> tenantSchemaDetailsCache(){ 
		cache = new ConcurrentHashMap<>();
		return cache;
	}
	 private Map<String,String> cache ;
	
	@EventListener(ApplicationReadyEvent.class)
	public void loadData() { 
		TenantContext.setTenantId(CurrentTenantResolver.DEFAULT_SCHEMA);
		tenantService.getAll().forEach(tenant -> {
			cache.put(tenant.getId(), tenant.getSchema());
			 
        }); 
		TenantContext.clear();
 
	}
}
