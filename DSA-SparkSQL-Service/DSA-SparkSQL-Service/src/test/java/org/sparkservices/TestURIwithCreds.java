package org.sparkservices;

import org.junit.jupiter.api.Test;
import org.spark.service.rest.QueryRESTDataService;

import static org.junit.jupiter.api.Assertions.*;

public class TestURIwithCreds {

    @Test
    public void testUrlWithoutCredentialsReturnsNull() {
        String urlString = "http://localhost:8090/DSA-SQL-PostgreSQLService/rest/payments/TransactionsView";

        String[] credentials = assertDoesNotThrow(
                () -> QueryRESTDataService.parseCredentials(urlString)
        );

        assertNull(credentials);
    }

    @Test
    public void testUrlWithCredentialsExtractsUsernameAndPassword() {
        String urlString = "http://developer:iis@localhost:8090/DSA-SQL-PostgreSQLService/rest/payments/TransactionsView";

        String[] credentials = QueryRESTDataService.parseCredentials(urlString);

        assertNotNull(credentials);
        assertEquals("developer", credentials[0]);
        assertEquals("iis", credentials[1]);
    }

    @Test
    public void testUrlWithCredentialsCanBeCleaned() {
        String urlString = "http://developer:iis@localhost:8090/DSA-SQL-PostgreSQLService/rest/payments/TransactionsView";

        String[] credentials = QueryRESTDataService.parseCredentials(urlString);

        assertNotNull(credentials);

        String cleanUrl = urlString.replace(credentials[0] + ":" + credentials[1] + "@", "");

        assertEquals(
                "http://localhost:8090/DSA-SQL-PostgreSQLService/rest/payments/TransactionsView",
                cleanUrl
        );
    }

    @Test
    public void testInvalidUrlDoesNotCrashAndReturnsNull() {
        String urlString = "12345";

        String[] credentials = assertDoesNotThrow(
                () -> QueryRESTDataService.parseCredentials(urlString)
        );

        assertNull(credentials);
    }
}