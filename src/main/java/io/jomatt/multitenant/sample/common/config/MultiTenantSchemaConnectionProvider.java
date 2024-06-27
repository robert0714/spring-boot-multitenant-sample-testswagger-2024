package io.jomatt.multitenant.sample.common.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import io.quantics.multitenant.tenantdetails.TenantSchemaDetails;
import io.quantics.multitenant.tenantdetails.TenantSchemaDetailsService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Provider that provides tenant-specific connection handling in a multi-tenant application. The tenant
 * distinction is realized by using separate schemas, i.e. each tenant uses its own schema in a shared
 * (common) database.
 */
@Component
@Slf4j
public class MultiTenantSchemaConnectionProvider implements MultiTenantConnectionProvider<String> {

    private final DataSource dataSource; 
    private Map<String,String>  tenantSchemaCache ; 
     
	public MultiTenantSchemaConnectionProvider(DataSource dataSource ) {
		this.dataSource = dataSource; 
	} 
	@Autowired
	@Lazy
	public void setTenantSchemaCache(Map<String, String> tenantSchemaCache) {
		this.tenantSchemaCache = tenantSchemaCache;
	}

	@Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        log.trace("Get connection for tenant '{}'", tenantIdentifier);
        String  tenantSchema  = null;
        if(this.tenantSchemaCache!=null) {
        	tenantSchema= this.tenantSchemaCache.get(tenantIdentifier);
        } 
        var schema = tenantSchema != null ? tenantSchema : CurrentTenantResolver.DEFAULT_SCHEMA;        
        final Connection connection = getAnyConnection();
        log.info("DB vender '{}'", connection.getMetaData().getDatabaseProductName());
        connection.setSchema(schema);
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        log.trace("Release connection for tenant '{}'", tenantIdentifier);
        connection.setSchema(CurrentTenantResolver.DEFAULT_SCHEMA);
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }

}
