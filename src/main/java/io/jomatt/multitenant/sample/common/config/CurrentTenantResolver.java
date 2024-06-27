package io.jomatt.multitenant.sample.common.config;

import java.util.Map;
import java.util.Objects;
import io.quantics.multitenant.TenantContext;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

/**
 * Resolver for translating the current tenant-id into the schema to be used for the data source.
 */
@Component
public class CurrentTenantResolver implements CurrentTenantIdentifierResolver<String>, HibernatePropertiesCustomizer {

    public static final String DEFAULT_SCHEMA = "public";

    @Override
    public String resolveCurrentTenantIdentifier() {
        // return TenantContext.getTenantId() != null
        //         ? TenantContext.getTenantId()
        //         : DEFAULT_SCHEMA;
        return Objects.requireNonNullElse(TenantContext.getTenantId(), DEFAULT_SCHEMA);        
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
	@Override
	public void customize(Map<String, Object> hibernateProperties) {
		hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
	}
}
