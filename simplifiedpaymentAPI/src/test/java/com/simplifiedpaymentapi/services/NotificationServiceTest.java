package com.simplifiedpaymentapi.services;

import com.simplifiedpaymentapi.dtos.NotificationDTO;
import com.simplifiedpaymentapi.entities.User;
import com.simplifiedpaymentapi.entities.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    NotificationService notificationService;

    User user = new User(1L, "Maria", "12345678910", "123456", "maria@gmail.com", new BigDecimal(100), UserType.COMMON);
    String message = "Transaction completed successfully";

    @Test
    @DisplayName("Should send notification Successfully")
    void sendNotificationSuccessfully() {
        when(restTemplate.postForEntity(
                anyString(),
                any(NotificationDTO.class),
                any(Class.class)
        )).thenReturn(new ResponseEntity<>("Notification sent successfully", HttpStatus.OK));
        notificationService.sendNotification(user,message);
        verify(restTemplate, times(1)).postForEntity(
                anyString(),
                any(NotificationDTO.class),
                any(Class.class)
        );
    }

    @Test
    @DisplayName("Should print 'Notification service down' when the service returns a non-2xx status")
    void whenNotificationServiceDown(){
        when(restTemplate.postForEntity(
                anyString(),
                any(NotificationDTO.class),
                any(Class.class)
        )).thenReturn(new ResponseEntity<>("Error response", HttpStatus.SERVICE_UNAVAILABLE));
        notificationService.sendNotification(user,message);
        verify(restTemplate, times(1)).postForEntity(
                anyString(),
                any(NotificationDTO.class),
                any(Class.class)
        );
    }

    @Test
    @DisplayName("Should print 'Notification service unavailable' when an exception is thrown")
    void whenNotificationServiceUnavailable(){
        when(restTemplate.postForEntity(
                anyString(),
                any(NotificationDTO.class),
                any(Class.class)
        )).thenThrow(new RuntimeException("Simulated exception"));
        notificationService.sendNotification(user,message);
        verify(restTemplate, times(1)).postForEntity(
                anyString(),
                any(NotificationDTO.class),
                any(Class.class)
        );
    }

}