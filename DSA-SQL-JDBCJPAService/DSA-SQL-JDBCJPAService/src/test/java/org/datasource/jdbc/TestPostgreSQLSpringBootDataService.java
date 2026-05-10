package org.datasource.jdbc;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestPostgreSQLSpringBootDataService {

	private static final Logger logger =
			Logger.getLogger(TestPostgreSQLSpringBootDataService.class.getName());

	private static final String SERVICE_URL =
			"http://localhost:8090/DSA-SQL-PostgreSQLService/rest";

	private final RestTemplate restTemplate = new RestTemplate();

	@Test
	public void test1_get_CardLimitsView() {
		String endpoint = SERVICE_URL + "/payments/CardLimitsView";
		executeGetAndAssertJson(endpoint, "CardLimitsView");
	}

	@Test
	public void test2_get_CardSecurityView() {
		String endpoint = SERVICE_URL + "/payments/CardSecurityView";
		executeGetAndAssertJson(endpoint, "CardSecurityView");
	}

	@Test
	public void test3_get_TransactionsView() {
		String endpoint = SERVICE_URL + "/payments/TransactionsView";
		executeGetAndAssertJson(endpoint, "TransactionsView");
	}

	@Test
	public void test4_get_CardJpaView() {
		String endpoint = SERVICE_URL + "/cards/CardJpaView";
		executeGetAndAssertJson(endpoint, "CardJpaView");
	}

	@Test
	public void test5_get_MerchantJpaView() {
		String endpoint = SERVICE_URL + "/cards/MerchantJpaView";
		executeGetAndAssertJson(endpoint, "MerchantJpaView");
	}

	private void executeGetAndAssertJson(String endpoint, String viewName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

		/*
		 * If Spring Security is disabled in application.properties,
		 * these credentials are not required. Keeping them here does not hurt
		 * if the service ignores security.
		 */
		headers.setBasicAuth("developer", "iis");

		logger.info(">>> Testing REST endpoint for " + viewName + ": " + endpoint);

		ResponseEntity<String> responseEntity = restTemplate.exchange(
				endpoint,
				HttpMethod.GET,
				new HttpEntity<>(null, headers),
				String.class
		);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertFalse(responseEntity.getBody().isBlank());

		logger.info("Status code (" + viewName + "): " + responseEntity.getStatusCode());
		logger.info("ResultSet JSON (" + viewName + "): " + preview(responseEntity.getBody()));
	}

	private String preview(String json) {
		int maxLength = 500;
		return json.length() <= maxLength ? json : json.substring(0, maxLength) + "...";
	}
}