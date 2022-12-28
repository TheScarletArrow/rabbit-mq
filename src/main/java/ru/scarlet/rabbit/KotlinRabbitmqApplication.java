package ru.scarlet.rabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KotlinRabbitmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(KotlinRabbitmqApplication.class, args);
    }

}
