package io.jomatt.multitenant.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.quantics.multitenant.oauth2.config.MultiTenantResourceServerAutoConfiguration;
import io.quantics.multitenant.oauth2.config.MultiTenantResourceServerWebSecurityConfiguration;

@SpringBootApplication
//(exclude = { MultiTenantResourceServerAutoConfiguration.class })
public class MultiTenantApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiTenantApplication.class, args);
    }

}
