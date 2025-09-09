package com.simplifiedpicpay.services;

import com.simplifiedpicpay.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class AuthorizationService {

    private RestTemplate restTemplate;

    public boolean authorizedTransaction() throws Exception{
        String url = "https://util.devi.tools/api/v2/authorize";

        try{
            ResponseEntity<Void> response = restTemplate.getForEntity(url, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        }catch(HttpClientErrorException | HttpServerErrorException e){
            return false;
        }
    }
}
