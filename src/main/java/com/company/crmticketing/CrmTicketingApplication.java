package com.company.crmticketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CrmTicketingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmTicketingApplication.class, args);
    }

}
