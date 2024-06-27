package io.jomatt.multitenant.sample.demo;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import io.jomatt.multitenant.sample.common.user.User;
import io.jomatt.multitenant.sample.common.user.UserRepository ;
import io.quantics.multitenant.TenantContext;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "spring", name = "flyway.enabled", matchIfMissing = true)
public class DataConfig {
	private final UserRepository userRepository;
	
	public DataConfig( UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void loadTestData() {
		TenantContext.setTenantId("dukes");
		if (this.userRepository.count() == 0) {
			var piano = new User("alice");
			var cello = new User("Cello");
			var guitar = new User("Gibson Firebird");
			this.userRepository.saveAll(List.of(piano, cello, guitar));
		}
		TenantContext.clear();

		TenantContext.setTenantId("beans");
		if (this.userRepository.count() == 0) {
			var organ = new User("Hammond B3");
			var viola = new User("Viola");
			var guitarFake = new User("Gibson Firebird (Fake)");
			this.userRepository.saveAll(List.of(organ, viola, guitarFake));
		}
		TenantContext.clear();
	}
}
