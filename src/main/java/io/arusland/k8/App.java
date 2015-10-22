package io.arusland.k8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class App {
    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(App.class, args);
    }
}