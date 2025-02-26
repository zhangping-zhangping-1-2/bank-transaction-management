package com.hsbc.banktransaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HsbcBankTransactionManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(HsbcBankTransactionManagementApplication.class, args);
    }

}
