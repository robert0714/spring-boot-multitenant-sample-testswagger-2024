package io.jomatt.multitenant.sample.header;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.NonNull;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import io.jomatt.multitenant.sample.common.user.User;
import io.jomatt.multitenant.sample.common.user.UserRepository;
import io.quantics.multitenant.TenantContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc 
@Sql(scripts = { "/insert-data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = { "/delete-data.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MultiTenantHeaderApplicationTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    UserRepository userRepository; 
    
    @BeforeEach
    protected void setUp() throws Exception { 
    	Thread.sleep(1000L);
    	
    	TenantContext.setTenantId("tenant1");
		if (this.userRepository.count() == 0) {
			var alice = new User("alice");
			var alex = new User("alex"); 
			this.userRepository.saveAll(List.of(alice, alex));
		}
		TenantContext.clear();

		TenantContext.setTenantId("tenant2");
		if (this.userRepository.count() == 0) {
			var bob = new User("bob");
			var bella = new User("bella"); 
			this.userRepository.saveAll(List.of(bob, bella  ));
		}
		TenantContext.clear();
    }
    @AfterEach
    protected void teardown() throws Exception { 
    	Thread.sleep(1000L);
    	TenantContext.setTenantId("tenant1");
    	this.userRepository.deleteAll(); 
		TenantContext.clear();

		TenantContext.setTenantId("tenant2");
		this.userRepository.deleteAll(); 
		TenantContext.clear();
    }
    @Test     
    void tenant1() throws Exception {
        sendRequest("tenant1")
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("alice")))
                .andExpect(content().string(containsString("alex")));
    }

    @Test    
    void tenant2() throws Exception {
        sendRequest("tenant2")
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("bob")))
                .andExpect(content().string(containsString("bella")));
    }

    @Test
    void unknownTenant() throws Exception {
        sendRequest("unknown-tenant")
                .andExpect(status().isUnauthorized());
    }

    @Test
    void noTenant() throws Exception {
        sendRequest("")
                .andExpect(status().isUnauthorized());
    }

    @Test
    void ignoredPath() throws Exception {
        mvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    private ResultActions sendRequest(String tenantId) throws Exception {
    	 
        return mvc.perform(get("/users").with(tenantHeader(tenantId)));
    }

    private static TenantHeaderRequestPostProcessor tenantHeader(String tenantId) {
        return new TenantHeaderRequestPostProcessor(tenantId);
    }

    private static class TenantHeaderRequestPostProcessor implements RequestPostProcessor {

        private final String tenantId;

        TenantHeaderRequestPostProcessor(String tenantId) {
            this.tenantId = tenantId;
        }

        @Override
        @NonNull
        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
            request.addHeader("X-TENANT-ID", tenantId);
            return request;
        }

    }

}
