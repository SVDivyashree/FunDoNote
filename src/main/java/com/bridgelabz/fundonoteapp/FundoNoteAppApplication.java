package com.bridgelabz.fundonoteapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.bridgelabz.fundonoteapp")
@EnableAutoConfiguration
@SpringBootApplication

public class FundoNoteAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundoNoteAppApplication.class, args);
	}

}
