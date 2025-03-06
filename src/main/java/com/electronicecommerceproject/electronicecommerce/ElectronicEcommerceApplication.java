package com.electronicecommerceproject.electronicecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.electronicecommerceproject.electronicecommerce"})
public class ElectronicEcommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElectronicEcommerceApplication.class, args);
    }
}