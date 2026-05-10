package org.datasource.jdbc;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestSpringBootWEBService {

	private static final Logger logger =
			Logger.getLogger(TestSpringBootWEBService.class.getName());

	private static final String SERVICE_URL =
			"http://localhost:8096/DSA-WEB-RESTService/rest/OLAP";

	private final RestTemplate restTemplate = new RestTemplate();

	@Test
	public void test1_ping() {
		String endpoint = SERVICE_URL + "/ping";
		executeGetAndAssertResponse(endpoint, "Ping");
	}

	@Test
	public void test2_get_DimClientView() {
		String endpoint = SERVICE_URL + "/dimensional/client";
		executeGetAndAssertJson(endpoint, "DIM_CLIENT_V");
	}

	@Test
	public void test3_get_DimCardView() {
		String endpoint = SERVICE_URL + "/dimensional/card";
		executeGetAndAssertJson(endpoint, "DIM_CARD_V");
	}

	@Test
	public void test4_get_FactTransactionsEnrichedView() {
		String endpoint = SERVICE_URL + "/fact/transactions-enriched";
		executeGetAndAssertJson(endpoint, "FACT_TRANSACTIONS_ENRICHED_V");
	}

	@Test
	public void test5_get_OlapSourceChannelView() {
		String endpoint = SERVICE_URL + "/analytical/source-channel";
		executeGetAndAssertJson(endpoint, "OLAP_VIEW_TXN_SOURCE_CHANNEL_V");
	}

	@Test
	public void test6_get_OlapCardSecurityCubeView() {
		String endpoint = SERVICE_URL + "/analytical/card-security-cube";
		executeGetAndAssertJson(endpoint, "OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V");
	}

	@Test
	public void test7_get_WindowCardTotalRankView() {
		String endpoint = SERVICE_URL + "/window/card-total-rank";
		executeGetAndAssertJson(endpoint, "WV_CARD_TOTAL_RANK_V");
	}

	@Test
	public void test8_get_WindowMerchantStateRankView() {
		String endpoint = SERVICE_URL + "/window/merchant-state-rank";
		executeGetAndAssertJson(endpoint, "WV_MERCHANT_STATE_RANK_V");
	}

	@Test
	public void test9_get_AdvancedPivotTxnSourceMonthView() {
		String endpoint = SERVICE_URL + "/advanced/pivot-txn-source-month";
		executeGetAndAssertJson(endpoint, "ADV_PIVOT_TXN_SOURCE_MONTH_V");
	}

	@Test
	public void test10_get_AdvancedTxnRiskStatisticsView() {
		String endpoint = SERVICE_URL + "/advanced/txn-risk-statistics";
		executeGetAndAssertJson(endpoint, "ADV_TXN_RISK_STATISTICS_V");
	}

	private void executeGetAndAssertJson(String endpoint, String viewName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

		/*
		 * Security is disabled in the current local configuration,
		 * but credentials are kept here because the professor's model used them.
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

	private void executeGetAndAssertResponse(String endpoint, String endpointName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
		headers.setBasicAuth("developer", "iis");

		logger.info(">>> Testing REST endpoint for " + endpointName + ": " + endpoint);

		ResponseEntity<String> responseEntity = restTemplate.exchange(
				endpoint,
				HttpMethod.GET,
				new HttpEntity<>(null, headers),
				String.class
		);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertFalse(responseEntity.getBody().isBlank());

		logger.info("Status code (" + endpointName + "): " + responseEntity.getStatusCode());
		logger.info("Response (" + endpointName + "): " + preview(responseEntity.getBody()));
	}

	private String preview(String response) {
		int maxLength = 500;
		return response.length() <= maxLength
				? response
				: response.substring(0, maxLength) + "...";
	}
}