package com.simplifiedpaymentapi.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.net.URI;


@Component
public class SwaggerOpener {

    private static final Logger LOG = LoggerFactory.getLogger(SwaggerOpener.class);

    @EventListener(ApplicationReadyEvent.class)
    public void openSwaggerUrl(){
        String url = "http://localhost:8080/swagger-ui/index.html";
        if(Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            try{
                desktop.browse(new URI(url));
            }catch (Exception e){
                LOG.error("Error opening swagger url",e);
            }
        }

    }
}
