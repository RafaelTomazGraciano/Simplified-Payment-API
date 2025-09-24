package com.simplifiedpaymentapi.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuthorizationService authorizationService;

    private final String AUTH_URL = "https://util.devi.tools/api/v2/authorize";

    @Test
    @DisplayName("Should return true when the authorization service is running")
    void shouldReturnTrueWhenServiceIsRunning() throws Exception {
        ResponseEntity<Void> successResponse = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.getForEntity(AUTH_URL, Void.class)).thenReturn(successResponse);

        boolean isAuthorized = authorizationService.authorizedTransaction();

        assertTrue(isAuthorized);
    }

    @Test
    @DisplayName("Should return false when the authorization service does not authorize the transaction")
    void shouldReturnFalseWhenNotAuthorized() throws Exception {
        when(restTemplate.getForEntity(AUTH_URL, Void.class)).thenThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN));

        boolean isAuthorized = authorizationService.authorizedTransaction();

        assertFalse(isAuthorized);
    }

    @Test
    @DisplayName("Should return false when the authorization service is down")
    void shouldReturnFalseWhenServiceIsDown() throws Exception {
        when(restTemplate.getForEntity(AUTH_URL, Void.class)).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        boolean isAuthorized = authorizationService.authorizedTransaction();

        assertFalse(isAuthorized);
    }
}
