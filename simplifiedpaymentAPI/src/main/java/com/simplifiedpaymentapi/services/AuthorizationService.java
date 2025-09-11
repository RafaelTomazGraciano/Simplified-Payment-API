package com.simplifiedpaymentapi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


@RequiredArgsConstructor
@Service
public class AuthorizationService {

    private final RestTemplate restTemplate;

    public boolean authorizedTransaction() {
        String url = "https://util.devi.tools/api/v2/authorize";

        try{
            ResponseEntity<Void> response = restTemplate.getForEntity(url, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        }catch(HttpClientErrorException | HttpServerErrorException e){
            return false;
        }
    }
}
