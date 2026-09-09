package com.szakdolgozat;

import com.szakdolgozat.Service.ExcelImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VerkepelemzoApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                VerkepelemzoApplication.class,
                args
        );
    }


}