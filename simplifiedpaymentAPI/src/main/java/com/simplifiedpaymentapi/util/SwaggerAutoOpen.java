package com.simplifiedpaymentapi.util;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class SwaggerAutoOpen {

    private final AtomicBoolean hasExecuted = new AtomicBoolean(false);

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        if (!hasExecuted.compareAndSet(false, true)) {
            return;
        }

        new Thread(() -> {
            try {
                Thread.sleep(3000);
                openSwaggerUI();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void openSwaggerUI() {
        String url = "http://localhost:8080/swagger-ui/index.html";

        System.out.println("Opening Swagger UI: " + url);

        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder pb;

            if (os.contains("win")) {
                pb = new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", url);
            } else if (os.contains("mac")) {
                pb = new ProcessBuilder("open", url);
            } else {
                pb = new ProcessBuilder("xdg-open", url);
            }
            pb.start();
        } catch (Exception e) {
            System.out.println("Could not open browser. Access manually: " + url);
        }
    }
}
