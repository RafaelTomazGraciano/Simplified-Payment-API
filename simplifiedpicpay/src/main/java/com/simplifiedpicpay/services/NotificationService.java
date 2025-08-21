package com.simplifiedpicpay.services;

import com.simplifiedpicpay.dtos.NotificationDTO;
import com.simplifiedpicpay.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    @Autowired
    private RestTemplate restTemplate;

    public void sendNotification(User user, String message){
        String email = user.getEmail();
        NotificationDTO notificationRequest = new NotificationDTO(email, message);

        String url = " https://util.devi.tools/api/v1/notify";

        try {

            ResponseEntity<String> notificationResponse = restTemplate.postForEntity(url, notificationRequest, String.class);

            if(!notificationResponse.getStatusCode().is2xxSuccessful()){
                System.out.println("Notification service down");
            }
        }catch(Exception e) {
            System.out.println("Notification service unavailable: " + e.getMessage());
        }
    }

}
