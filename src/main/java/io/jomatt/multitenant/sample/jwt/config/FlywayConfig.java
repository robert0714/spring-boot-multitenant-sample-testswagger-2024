package io.jomatt.multitenant.sample.jwt.config;

import io.jomatt.multitenant.sample.common.config.CurrentTenantResolver;
import io.quantics.multitenant.tenantdetails.TenantSchemaDetailsService;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(prefix = "spring", name = "flyway.enabled", matchIfMissing = true)
public class FlywayConfig {

    private final boolean outOfOrder;
    private final boolean baselineOnMigrate;
    private static final String DEFAULT_MIGRATION_LOCATION = "db/migration/default";
    private static final String TENANT_MIGRATION_LOCATION = "db/migration/tenant";

    public FlywayConfig(@Value("${spring.flyway.out-of-order:false}") boolean outOfOrder,
                        @Value("${spring.flyway.baseline-on-migrate:false}") boolean baselineOnMigrate) {
        this.outOfOrder = outOfOrder;
        this.baselineOnMigrate = baselineOnMigrate;
    }

    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .outOfOrder(outOfOrder)
                .baselineOnMigrate(baselineOnMigrate)
                .locations(DEFAULT_MIGRATION_LOCATION)
                .dataSource(dataSource)
                .schemas(CurrentTenantResolver.DEFAULT_SCHEMA)
                .load();
        flyway.migrate();
        return flyway;
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring", name = "flyway.repair-on-migrate", havingValue = "false",
            matchIfMissing = true)
    public Boolean tenantsFlyway(TenantSchemaDetailsService tenantService, DataSource dataSource) {
        tenantService.getAll().forEach(tenant -> {
            Flyway flyway = Flyway.configure()
                    .outOfOrder(outOfOrder)
                    .baselineOnMigrate(baselineOnMigrate)
                    .locations(TENANT_MIGRATION_LOCATION)
                    .dataSource(dataSource)
                    .schemas(tenant.getSchema())
                    .load();
            flyway.migrate();
        });
        return true;
    }

}
