package com.main.stpaul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import io.github.cdimascio.dotenv.Dotenv;


@ComponentScan(basePackages = "com.main.stpaul")
@SpringBootApplication
public class StpaulApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(entry->System.setProperty(entry.getKey(),entry.getValue()));
		SpringApplication.run(StpaulApplication.class, args);
	}
}