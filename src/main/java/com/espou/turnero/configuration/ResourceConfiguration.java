package com.espou.turnero.configuration;

import com.espou.turnero.service.ResourceService;
import com.espou.turnero.storage.ResourceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResourceConfiguration {
    @Bean
    public ResourceRepository getResourceRepository(){
        return new ResourceRepository();
    }

    @Bean
    public ResourceService getResourceService(){
        return new ResourceService();
    }
}
