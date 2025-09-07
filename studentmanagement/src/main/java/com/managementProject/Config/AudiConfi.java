package com.managementProject.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Configuration
public class AudiConfi {
    @Bean
    public AuditorAware auditProvider(){
        return (()-> Optional.of("System"));
    }

}
